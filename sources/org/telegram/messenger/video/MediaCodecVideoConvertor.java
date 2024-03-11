package org.telegram.messenger.video;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.view.Surface;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.video.audio_input.AudioInput;
import org.telegram.messenger.video.audio_input.BlankAudioInput;
import org.telegram.messenger.video.audio_input.GeneralAudioInput;
import org.telegram.ui.Components.RLottieDrawable;
import org.telegram.ui.Stories.recorder.StoryEntry;
/* loaded from: classes3.dex */
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
    private String outputMimeType;

    public boolean convertVideo(ConvertVideoParams convertVideoParams) {
        this.callback = convertVideoParams.callback;
        return convertVideoInternal(convertVideoParams, false, 0);
    }

    public long getLastFrameTimestamp() {
        return this.endPresentationTime;
    }

    /* JADX WARN: Can't wrap try/catch for region: R(13:(9:(4:17|18|19|(30:21|22|23|24|(2:528|529)|26|27|28|30|31|32|33|34|35|36|37|38|(3:487|488|(12:490|41|42|43|45|46|47|(2:474|475)(1:49)|50|(4:52|53|54|55)(1:472)|56|(2:(5:190|191|(2:451|452)|193|(1:(9:198|(1:200)(1:450)|201|202|203|(1:205)(3:273|(4:438|439|(1:441)|442)(2:275|(5:277|(1:279)|280|(1:321)(7:283|284|285|286|(2:288|(8:290|291|292|293|(1:295)(1:303)|(1:297)(1:302)|298|299))(1:314)|313|299)|300)(2:322|(4:324|325|(1:327)(1:434)|(10:329|330|(6:345|346|347|(5:(1:389)(3:352|353|354)|(2:358|359)|360|361|(1:376)(6:365|366|(1:368)|369|370|371))(2:390|(2:392|(6:394|(3:398|(2:404|(2:406|407)(1:413))|414)|419|408|(1:411)|412)(1:421)))|(2:208|209)(3:(7:212|213|214|215|216|217|(1:219))(1:272)|220|221)|210)(1:332)|333|334|(1:336)(1:339)|337|338|(0)(0)|210)(3:431|432|433))(3:435|436|437)))|301)|206|(0)(0)|210)))(2:59|60)|197)))|40|41|42|43|45|46|47|(0)(0)|50|(0)(0)|56|(7:(0)|190|191|(0)|193|(10:(0)|198|(0)(0)|201|202|203|(0)(0)|206|(0)(0)|210)|197)))(1:573)|45|46|47|(0)(0)|50|(0)(0)|56|(7:(0)|190|191|(0)|193|(10:(0)|198|(0)(0)|201|202|203|(0)(0)|206|(0)(0)|210)|197))|32|33|34|35|36|37|38|(0)|40|41|42|43) */
    /* JADX WARN: Can't wrap try/catch for region: R(14:(23:(4:17|18|19|(30:21|22|23|24|(2:528|529)|26|27|28|30|31|32|33|34|35|36|37|38|(3:487|488|(12:490|41|42|43|45|46|47|(2:474|475)(1:49)|50|(4:52|53|54|55)(1:472)|56|(2:(5:190|191|(2:451|452)|193|(1:(9:198|(1:200)(1:450)|201|202|203|(1:205)(3:273|(4:438|439|(1:441)|442)(2:275|(5:277|(1:279)|280|(1:321)(7:283|284|285|286|(2:288|(8:290|291|292|293|(1:295)(1:303)|(1:297)(1:302)|298|299))(1:314)|313|299)|300)(2:322|(4:324|325|(1:327)(1:434)|(10:329|330|(6:345|346|347|(5:(1:389)(3:352|353|354)|(2:358|359)|360|361|(1:376)(6:365|366|(1:368)|369|370|371))(2:390|(2:392|(6:394|(3:398|(2:404|(2:406|407)(1:413))|414)|419|408|(1:411)|412)(1:421)))|(2:208|209)(3:(7:212|213|214|215|216|217|(1:219))(1:272)|220|221)|210)(1:332)|333|334|(1:336)(1:339)|337|338|(0)(0)|210)(3:431|432|433))(3:435|436|437)))|301)|206|(0)(0)|210)))(2:59|60)|197)))|40|41|42|43|45|46|47|(0)(0)|50|(0)(0)|56|(7:(0)|190|191|(0)|193|(10:(0)|198|(0)(0)|201|202|203|(0)(0)|206|(0)(0)|210)|197)))(1:573)|30|31|32|33|34|35|36|37|38|(0)|40|41|42|43|45|46|47|(0)(0)|50|(0)(0)|56|(7:(0)|190|191|(0)|193|(10:(0)|198|(0)(0)|201|202|203|(0)(0)|206|(0)(0)|210)|197))|540|541|542|(4:544|(1:546)(1:558)|547|548)(1:559)|549|(4:551|(1:553)|554|555)|22|23|24|(0)|26|27|28) */
    /* JADX WARN: Can't wrap try/catch for region: R(24:580|(8:581|582|583|584|585|586|(1:1616)(6:591|592|593|594|595|596)|597)|(2:599|(3:601|602|(18:625|(73:627|628|629|630|(3:632|633|634)(2:1598|1599)|635|636|(5:638|(1:640)(2:1585|(1:1587)(1:1588))|641|(2:643|644)(1:1584)|645)(3:1589|(1:1591)(1:1593)|1592)|(2:1579|1580)|647|(1:649)(1:1578)|650|(1:652)(2:1565|(2:1567|1568)(60:1569|1570|(1:1564)(4:657|(2:662|663)|1563|663)|(57:1556|1557|(2:1549|1550)|667|668|669|(5:1536|1537|1538|1539|1540)(1:671)|672|673|(1:677)|678|679|(8:681|(1:683)(1:1531)|684|(1:686)(1:1530)|687|(1:689)|690|(44:1529|695|(3:697|698|(45:701|(1:703)(1:1518)|704|705|706|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(3:1458|1459|(27:1462|(1:1464)(1:1467)|1465|725|(3:728|729|(2:731|(26:733|734|735|736|737|738|739|740|741|742|743|744|(2:1391|1392)(1:746)|747|748|(10:1329|1330|1331|(4:1333|1334|(8:1336|(1:1338)|1376|1342|(1:1344)|(1:(8:1347|1348|1349|(1:1351)|1352|(1:1354)(1:1357)|1355|1356)(6:1362|(1:1364)|1365|(1:1367)|1368|1369))(1:1371)|1370|1356)(1:1377)|1339)(1:1378)|1341|1342|(0)|(0)(0)|1370|1356)(4:750|751|(3:753|754|755)(1:1327)|756)|(1:758)(1:1326)|759|760|761|(2:(6:790|791|(3:793|794|795)(1:1316)|(7:797|798|799|800|(5:802|803|804|(4:806|(1:808)(1:1246)|809|(1:811)(1:1245))(1:1247)|812)(2:1254|(2:1302|(2:1304|(4:815|816|817|(2:819|820))(1:1244)))(11:1259|1260|(3:1262|1263|(3:1265|1266|1267))(1:1296)|1272|(1:1274)|1275|(1:1277)(1:1294)|1278|(2:1284|(4:1288|(1:1290)|1291|1292))|1293|1292))|813|(0)(0))(1:1310)|821|(1:(11:826|827|828|(1:830)(1:1232)|831|832|833|834|(1:836)(4:1067|(4:1213|1214|(1:1216)|1217)(2:1069|(3:1071|(1:1113)(7:1074|1075|1076|1077|(3:1079|1080|(7:1082|1083|1084|(1:1086)(1:1095)|(1:1088)(1:1094)|1089|1090))(1:1107)|1106|1090)|1091)(2:1114|(4:1116|1117|(1:1119)(1:1209)|(10:1121|1122|(10:1138|1139|1140|(8:(1:1145)|(1:1170)(2:1149|1150)|1151|1152|1153|1154|(3:1158|(1:1160)|1161)|1162)(2:1171|(7:1173|(3:1177|(2:1183|(2:1185|1186)(1:1193))|1194)|1199|1187|(1:1190)|1191|1192))|1126|1127|(1:1129)(1:1132)|1130|1131|1093)(1:1124)|1125|1126|1127|(0)(0)|1130|1131|1093)(3:1206|1207|1208))(3:1210|1211|1212)))|1092|1093)|(2:838|839)(4:(8:842|843|844|845|(1:847)(3:851|(17:855|(3:1047|1048|(1:1050))(1:(22:858|859|(1:861)(1:1037)|862|863|(2:867|868)|869|(3:998|999|(6:1001|1002|1003|1004|1005|(14:1007|(3:1013|1014|1015)(3:1009|1010|1011)|1012|873|(1:997)(1:879)|880|(1:882)(1:996)|883|(2:887|(7:889|(1:891)|892|(1:894)(3:985|(1:989)|990)|(9:905|906|907|(1:909)(1:979)|910|911|912|(7:914|915|916|917|918|919|920)(1:974)|921)(1:896)|897|(4:899|(1:901)|902|903)(3:904|849|850))(3:991|(1:993)|994))|995|(0)(0)|(0)(0)|897|(0)(0))(14:1021|872|873|(1:875)|997|880|(0)(0)|883|(3:885|887|(0)(0))|995|(0)(0)|(0)(0)|897|(0)(0))))|871|872|873|(0)|997|880|(0)(0)|883|(0)|995|(0)(0)|(0)(0)|897|(0)(0))(4:1043|1044|1045|1046))|1020|967|968|121|122|123|(1:125)|126|(2:149|150)|(2:145|146)|(2:141|142)|(2:137|138)|(2:133|134)|132|(2:82|83)(2:85|(3:92|(1:94)|95)(2:90|91)))|853)|848|849|850)(1:1066)|854|849|850)|840)))|825)|766|767|768|(1:770)|772)))|1423|(5:1425|1426|1427|1428|(29:1430|(1:1432)(1:1451)|1433|1434|1435|(1:1437)(1:1446)|1438|1439|1440|1441|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772)(1:1452))|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772))|724|725|(3:728|729|(0))|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772))(1:1526)|1519|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(0)|724|725|(0)|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772))(1:1532)|694|695|(0)(0)|1519|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(0)|724|725|(0)|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772)|665|(0)|667|668|669|(0)(0)|672|673|(2:675|677)|678|679|(0)(0)|694|695|(0)(0)|1519|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(0)|724|725|(0)|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772))|653|(1:655)|1564|(0)|665|(0)|667|668|669|(0)(0)|672|673|(0)|678|679|(0)(0)|694|695|(0)(0)|1519|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(0)|724|725|(0)|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772)(1:1604)|(2:785|786)|(1:775)|(1:777)|(1:779)|780|781|71|(1:73)|74|(2:112|113)|(2:108|109)|(2:104|105)|(2:100|101)|(2:96|97)|80|(0)(0))(20:605|606|607|608|609|610|(1:617)(1:613)|614|615|616|71|(0)|74|(0)|(0)|(0)|(0)|(0)|80|(0)(0))))(1:1609)|1607|602|(0)|625|(0)(0)|(0)|(0)|(0)|(0)|780|781|71|(0)|74|(0)|(0)|(0)|(0)|(0)|80|(0)(0)) */
    /* JADX WARN: Can't wrap try/catch for region: R(63:627|(11:628|629|630|(3:632|633|634)(2:1598|1599)|635|636|(5:638|(1:640)(2:1585|(1:1587)(1:1588))|641|(2:643|644)(1:1584)|645)(3:1589|(1:1591)(1:1593)|1592)|(2:1579|1580)|647|(1:649)(1:1578)|650)|(1:652)(2:1565|(2:1567|1568)(60:1569|1570|(1:1564)(4:657|(2:662|663)|1563|663)|(57:1556|1557|(2:1549|1550)|667|668|669|(5:1536|1537|1538|1539|1540)(1:671)|672|673|(1:677)|678|679|(8:681|(1:683)(1:1531)|684|(1:686)(1:1530)|687|(1:689)|690|(44:1529|695|(3:697|698|(45:701|(1:703)(1:1518)|704|705|706|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(3:1458|1459|(27:1462|(1:1464)(1:1467)|1465|725|(3:728|729|(2:731|(26:733|734|735|736|737|738|739|740|741|742|743|744|(2:1391|1392)(1:746)|747|748|(10:1329|1330|1331|(4:1333|1334|(8:1336|(1:1338)|1376|1342|(1:1344)|(1:(8:1347|1348|1349|(1:1351)|1352|(1:1354)(1:1357)|1355|1356)(6:1362|(1:1364)|1365|(1:1367)|1368|1369))(1:1371)|1370|1356)(1:1377)|1339)(1:1378)|1341|1342|(0)|(0)(0)|1370|1356)(4:750|751|(3:753|754|755)(1:1327)|756)|(1:758)(1:1326)|759|760|761|(2:(6:790|791|(3:793|794|795)(1:1316)|(7:797|798|799|800|(5:802|803|804|(4:806|(1:808)(1:1246)|809|(1:811)(1:1245))(1:1247)|812)(2:1254|(2:1302|(2:1304|(4:815|816|817|(2:819|820))(1:1244)))(11:1259|1260|(3:1262|1263|(3:1265|1266|1267))(1:1296)|1272|(1:1274)|1275|(1:1277)(1:1294)|1278|(2:1284|(4:1288|(1:1290)|1291|1292))|1293|1292))|813|(0)(0))(1:1310)|821|(1:(11:826|827|828|(1:830)(1:1232)|831|832|833|834|(1:836)(4:1067|(4:1213|1214|(1:1216)|1217)(2:1069|(3:1071|(1:1113)(7:1074|1075|1076|1077|(3:1079|1080|(7:1082|1083|1084|(1:1086)(1:1095)|(1:1088)(1:1094)|1089|1090))(1:1107)|1106|1090)|1091)(2:1114|(4:1116|1117|(1:1119)(1:1209)|(10:1121|1122|(10:1138|1139|1140|(8:(1:1145)|(1:1170)(2:1149|1150)|1151|1152|1153|1154|(3:1158|(1:1160)|1161)|1162)(2:1171|(7:1173|(3:1177|(2:1183|(2:1185|1186)(1:1193))|1194)|1199|1187|(1:1190)|1191|1192))|1126|1127|(1:1129)(1:1132)|1130|1131|1093)(1:1124)|1125|1126|1127|(0)(0)|1130|1131|1093)(3:1206|1207|1208))(3:1210|1211|1212)))|1092|1093)|(2:838|839)(4:(8:842|843|844|845|(1:847)(3:851|(17:855|(3:1047|1048|(1:1050))(1:(22:858|859|(1:861)(1:1037)|862|863|(2:867|868)|869|(3:998|999|(6:1001|1002|1003|1004|1005|(14:1007|(3:1013|1014|1015)(3:1009|1010|1011)|1012|873|(1:997)(1:879)|880|(1:882)(1:996)|883|(2:887|(7:889|(1:891)|892|(1:894)(3:985|(1:989)|990)|(9:905|906|907|(1:909)(1:979)|910|911|912|(7:914|915|916|917|918|919|920)(1:974)|921)(1:896)|897|(4:899|(1:901)|902|903)(3:904|849|850))(3:991|(1:993)|994))|995|(0)(0)|(0)(0)|897|(0)(0))(14:1021|872|873|(1:875)|997|880|(0)(0)|883|(3:885|887|(0)(0))|995|(0)(0)|(0)(0)|897|(0)(0))))|871|872|873|(0)|997|880|(0)(0)|883|(0)|995|(0)(0)|(0)(0)|897|(0)(0))(4:1043|1044|1045|1046))|1020|967|968|121|122|123|(1:125)|126|(2:149|150)|(2:145|146)|(2:141|142)|(2:137|138)|(2:133|134)|132|(2:82|83)(2:85|(3:92|(1:94)|95)(2:90|91)))|853)|848|849|850)(1:1066)|854|849|850)|840)))|825)|766|767|768|(1:770)|772)))|1423|(5:1425|1426|1427|1428|(29:1430|(1:1432)(1:1451)|1433|1434|1435|(1:1437)(1:1446)|1438|1439|1440|1441|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772)(1:1452))|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772))|724|725|(3:728|729|(0))|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772))(1:1526)|1519|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(0)|724|725|(0)|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772))(1:1532)|694|695|(0)(0)|1519|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(0)|724|725|(0)|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772)|665|(0)|667|668|669|(0)(0)|672|673|(2:675|677)|678|679|(0)(0)|694|695|(0)(0)|1519|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(0)|724|725|(0)|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772))|653|(1:655)|1564|(0)|665|(0)|667|668|669|(0)(0)|672|673|(0)|678|679|(0)(0)|694|695|(0)(0)|1519|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(0)|724|725|(0)|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772) */
    /* JADX WARN: Can't wrap try/catch for region: R(73:627|628|629|630|(3:632|633|634)(2:1598|1599)|635|636|(5:638|(1:640)(2:1585|(1:1587)(1:1588))|641|(2:643|644)(1:1584)|645)(3:1589|(1:1591)(1:1593)|1592)|(2:1579|1580)|647|(1:649)(1:1578)|650|(1:652)(2:1565|(2:1567|1568)(60:1569|1570|(1:1564)(4:657|(2:662|663)|1563|663)|(57:1556|1557|(2:1549|1550)|667|668|669|(5:1536|1537|1538|1539|1540)(1:671)|672|673|(1:677)|678|679|(8:681|(1:683)(1:1531)|684|(1:686)(1:1530)|687|(1:689)|690|(44:1529|695|(3:697|698|(45:701|(1:703)(1:1518)|704|705|706|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(3:1458|1459|(27:1462|(1:1464)(1:1467)|1465|725|(3:728|729|(2:731|(26:733|734|735|736|737|738|739|740|741|742|743|744|(2:1391|1392)(1:746)|747|748|(10:1329|1330|1331|(4:1333|1334|(8:1336|(1:1338)|1376|1342|(1:1344)|(1:(8:1347|1348|1349|(1:1351)|1352|(1:1354)(1:1357)|1355|1356)(6:1362|(1:1364)|1365|(1:1367)|1368|1369))(1:1371)|1370|1356)(1:1377)|1339)(1:1378)|1341|1342|(0)|(0)(0)|1370|1356)(4:750|751|(3:753|754|755)(1:1327)|756)|(1:758)(1:1326)|759|760|761|(2:(6:790|791|(3:793|794|795)(1:1316)|(7:797|798|799|800|(5:802|803|804|(4:806|(1:808)(1:1246)|809|(1:811)(1:1245))(1:1247)|812)(2:1254|(2:1302|(2:1304|(4:815|816|817|(2:819|820))(1:1244)))(11:1259|1260|(3:1262|1263|(3:1265|1266|1267))(1:1296)|1272|(1:1274)|1275|(1:1277)(1:1294)|1278|(2:1284|(4:1288|(1:1290)|1291|1292))|1293|1292))|813|(0)(0))(1:1310)|821|(1:(11:826|827|828|(1:830)(1:1232)|831|832|833|834|(1:836)(4:1067|(4:1213|1214|(1:1216)|1217)(2:1069|(3:1071|(1:1113)(7:1074|1075|1076|1077|(3:1079|1080|(7:1082|1083|1084|(1:1086)(1:1095)|(1:1088)(1:1094)|1089|1090))(1:1107)|1106|1090)|1091)(2:1114|(4:1116|1117|(1:1119)(1:1209)|(10:1121|1122|(10:1138|1139|1140|(8:(1:1145)|(1:1170)(2:1149|1150)|1151|1152|1153|1154|(3:1158|(1:1160)|1161)|1162)(2:1171|(7:1173|(3:1177|(2:1183|(2:1185|1186)(1:1193))|1194)|1199|1187|(1:1190)|1191|1192))|1126|1127|(1:1129)(1:1132)|1130|1131|1093)(1:1124)|1125|1126|1127|(0)(0)|1130|1131|1093)(3:1206|1207|1208))(3:1210|1211|1212)))|1092|1093)|(2:838|839)(4:(8:842|843|844|845|(1:847)(3:851|(17:855|(3:1047|1048|(1:1050))(1:(22:858|859|(1:861)(1:1037)|862|863|(2:867|868)|869|(3:998|999|(6:1001|1002|1003|1004|1005|(14:1007|(3:1013|1014|1015)(3:1009|1010|1011)|1012|873|(1:997)(1:879)|880|(1:882)(1:996)|883|(2:887|(7:889|(1:891)|892|(1:894)(3:985|(1:989)|990)|(9:905|906|907|(1:909)(1:979)|910|911|912|(7:914|915|916|917|918|919|920)(1:974)|921)(1:896)|897|(4:899|(1:901)|902|903)(3:904|849|850))(3:991|(1:993)|994))|995|(0)(0)|(0)(0)|897|(0)(0))(14:1021|872|873|(1:875)|997|880|(0)(0)|883|(3:885|887|(0)(0))|995|(0)(0)|(0)(0)|897|(0)(0))))|871|872|873|(0)|997|880|(0)(0)|883|(0)|995|(0)(0)|(0)(0)|897|(0)(0))(4:1043|1044|1045|1046))|1020|967|968|121|122|123|(1:125)|126|(2:149|150)|(2:145|146)|(2:141|142)|(2:137|138)|(2:133|134)|132|(2:82|83)(2:85|(3:92|(1:94)|95)(2:90|91)))|853)|848|849|850)(1:1066)|854|849|850)|840)))|825)|766|767|768|(1:770)|772)))|1423|(5:1425|1426|1427|1428|(29:1430|(1:1432)(1:1451)|1433|1434|1435|(1:1437)(1:1446)|1438|1439|1440|1441|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772)(1:1452))|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772))|724|725|(3:728|729|(0))|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772))(1:1526)|1519|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(0)|724|725|(0)|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772))(1:1532)|694|695|(0)(0)|1519|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(0)|724|725|(0)|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772)|665|(0)|667|668|669|(0)(0)|672|673|(2:675|677)|678|679|(0)(0)|694|695|(0)(0)|1519|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(0)|724|725|(0)|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772))|653|(1:655)|1564|(0)|665|(0)|667|668|669|(0)(0)|672|673|(0)|678|679|(0)(0)|694|695|(0)(0)|1519|707|708|710|711|712|713|714|715|716|718|719|720|721|722|(0)|724|725|(0)|1423|(0)|739|740|741|742|743|744|(0)(0)|747|748|(0)(0)|(0)(0)|759|760|761|(8:(0)|790|791|(0)(0)|(0)(0)|821|(12:(0)|826|827|828|(0)(0)|831|832|833|834|(0)(0)|(0)(0)|840)|825)|766|767|768|(0)|772) */
    /* JADX WARN: Code restructure failed: missing block: B:1214:0x1736, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1215:0x1737, code lost:
        r12 = r8;
        r51 = r9;
        r2 = r79;
        r1 = r0;
        r80 = r15;
        r6 = r55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1216:0x1748, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1217:0x1749, code lost:
        r2 = r79;
        r4 = r0;
        r8 = r84;
        r80 = r15;
        r1 = r19;
        r46 = r8;
        r15 = r33;
        r6 = r55;
        r7 = "x";
        r14 = r77;
        r12 = -5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1218:0x1763, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1219:0x1764, code lost:
        r12 = r8;
        r2 = r79;
        r1 = r0;
        r80 = r15;
        r6 = r55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1221:0x1774, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1222:0x1775, code lost:
        r2 = r79;
        r4 = r0;
        r8 = r84;
        r80 = r15;
        r1 = r19;
        r15 = r33;
        r6 = r55;
        r7 = "x";
        r14 = r77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1223:0x178b, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1224:0x178c, code lost:
        r2 = r79;
        r1 = r0;
        r80 = r15;
        r6 = r55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1225:0x179a, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1226:0x179b, code lost:
        r6 = r49;
        r18 = r6;
        r2 = r79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1227:0x17a2, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1228:0x17a3, code lost:
        r6 = r49;
        r3 = r8;
        r18 = r6;
        r2 = r79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1229:0x17aa, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1230:0x17ab, code lost:
        r6 = r49;
        r2 = r5;
        r77 = r33;
        r33 = r15;
        r18 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1231:0x17b4, code lost:
        r4 = r0;
        r1 = r6;
        r15 = r33;
        r6 = r55;
        r7 = "x";
        r14 = r77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1232:0x17c0, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1233:0x17c1, code lost:
        r6 = r49;
        r3 = r8;
        r2 = r5;
        r77 = r33;
        r33 = r15;
        r18 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1234:0x17ca, code lost:
        r1 = r0;
        r84 = r3;
        r19 = r6;
        r6 = r55;
        r8 = -5;
        r12 = null;
        r51 = null;
        r80 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1235:0x17d9, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1236:0x17da, code lost:
        r6 = r49;
        r2 = r5;
        r77 = r33;
        r33 = r15;
        r18 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1237:0x17e3, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1238:0x17e4, code lost:
        r6 = r49;
        r2 = r5;
        r77 = r33;
        r33 = r15;
        r18 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1239:0x17ed, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1240:0x17ee, code lost:
        r2 = r5;
        r77 = r33;
        r33 = r15;
        r18 = r6;
        r6 = r49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1241:0x17f7, code lost:
        r3 = r91;
        r4 = r0;
        r1 = r6;
        r15 = r33;
        r6 = r55;
        r7 = "x";
        r14 = r77;
        r8 = null;
        r12 = -5;
        r46 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1242:0x1809, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1243:0x180a, code lost:
        r2 = r5;
        r77 = r33;
        r33 = r15;
        r18 = r6;
        r6 = r49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1244:0x1813, code lost:
        r1 = r0;
        r19 = r6;
        r6 = r55;
        r8 = -5;
        r12 = null;
        r51 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1245:0x181e, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1246:0x181f, code lost:
        r2 = r5;
        r18 = r6;
        r3 = r91;
        r4 = r0;
        r1 = r49;
        r15 = r15;
        r6 = r55;
        r7 = "x";
        r14 = r33;
        r8 = null;
        r12 = -5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1247:0x1838, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1248:0x1839, code lost:
        r2 = r5;
        r77 = r33;
        r33 = r15;
        r18 = r6;
        r1 = r0;
        r19 = r49;
        r6 = r55;
        r8 = -5;
        r12 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1249:0x184b, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1250:0x184c, code lost:
        r2 = r5;
        r77 = r33;
        r3 = r91;
        r4 = r0;
        r1 = r49;
        r15 = r15;
        r6 = r55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1251:0x185c, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1252:0x185d, code lost:
        r2 = r5;
        r77 = r33;
        r33 = r15;
        r1 = r0;
        r19 = r49;
        r6 = r55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1253:0x186a, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1255:0x186c, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1256:0x186d, code lost:
        r2 = r5;
        r77 = r33;
        r3 = r91;
        r4 = r0;
        r1 = r49;
        r6 = r14;
        r15 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1257:0x187b, code lost:
        r7 = "x";
        r14 = r77;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1258:0x1881, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1259:0x1882, code lost:
        r70 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1260:0x1884, code lost:
        r2 = r5;
        r77 = r33;
        r33 = r15;
        r1 = r0;
        r19 = r49;
        r6 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1261:0x188f, code lost:
        r8 = -5;
        r12 = null;
        r18 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1293:0x1930, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:1294:0x1931, code lost:
        r4 = r0;
        r46 = r8;
        r12 = r14;
        r80 = r15;
        r1 = r19;
        r8 = r84;
        r14 = r5;
        r15 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x0579, code lost:
        throw new java.lang.RuntimeException("unsupported!!");
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x06af, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x06b0, code lost:
        r4 = r0;
        r2 = r6;
        r15 = r10;
        r8 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:311:0x06b6, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x06b7, code lost:
        r26 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x06bb, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:314:0x06bc, code lost:
        r35 = r15;
        r4 = r0;
        r15 = r10;
        r8 = r2;
        r2 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x06c6, code lost:
        r59 = r9;
        r18 = r35;
        r76 = r36;
        r6 = r37;
        r7 = "x";
        r32 = r10;
        r12 = -5;
        r46 = null;
        r78 = false;
        r80 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x06dc, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x06dd, code lost:
        r26 = r5;
        r35 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:319:0x06e2, code lost:
        r15 = r10;
        r8 = -5;
        r11 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:322:0x06eb, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x06ec, code lost:
        r4 = r0;
        r14 = r3;
        r15 = r10;
        r2 = r5;
        r59 = r9;
        r18 = r15;
        r76 = r36;
        r6 = r37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x06fe, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:328:0x0712, code lost:
        r7 = "x";
        r32 = r10;
        r8 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x0717, code lost:
        r12 = -5;
        r46 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x071f, code lost:
        r26 = r5;
        r35 = r15;
        r14 = r3;
        r15 = r10;
        r2 = null;
        r8 = -5;
        r11 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x0754, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x0755, code lost:
        r4 = r0;
        r14 = r3;
        r15 = r10;
        r59 = r9;
        r2 = r5;
        r7 = "x";
        r32 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x0763, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0764, code lost:
        r37 = r6;
        r26 = r5;
        r14 = r3;
        r15 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x076b, code lost:
        r0 = th;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x078a, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x078b, code lost:
        r37 = r6;
        r26 = r5;
        r14 = r3;
        r15 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x09e5, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x09e6, code lost:
        r3 = r91;
        r4 = r0;
        r2 = r15;
        r6 = r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:705:0x0e8d, code lost:
        if (r1.getString(r9).equals("audio/mpeg") != false) goto L1376;
     */
    /* JADX WARN: Code restructure failed: missing block: B:750:0x0f7a, code lost:
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:751:0x0f7b, code lost:
        r1 = r0;
        r12 = r8;
        r51 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:752:0x0f81, code lost:
        r0 = e;
     */
    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:1014:0x13ad  */
    /* JADX WARN: Removed duplicated region for block: B:1015:0x13b2  */
    /* JADX WARN: Removed duplicated region for block: B:1019:0x13c0  */
    /* JADX WARN: Removed duplicated region for block: B:1021:0x13da  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x0323 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:1085:0x14e7 A[Catch: all -> 0x14fa, Exception -> 0x14fc, TryCatch #14 {Exception -> 0x14fc, blocks: (B:1061:0x147f, B:1066:0x148d, B:1071:0x14a1, B:1085:0x14e7, B:1087:0x14ef, B:1105:0x1516, B:1107:0x151a, B:1126:0x156d, B:1120:0x1558, B:1109:0x153a, B:1111:0x1543, B:1068:0x1497, B:1070:0x149e), top: B:1434:0x147f }] */
    /* JADX WARN: Removed duplicated region for block: B:1097:0x1505  */
    /* JADX WARN: Removed duplicated region for block: B:1098:0x1508  */
    /* JADX WARN: Removed duplicated region for block: B:1101:0x150e  */
    /* JADX WARN: Removed duplicated region for block: B:1105:0x1516 A[Catch: all -> 0x14fa, Exception -> 0x14fc, TryCatch #14 {Exception -> 0x14fc, blocks: (B:1061:0x147f, B:1066:0x148d, B:1071:0x14a1, B:1085:0x14e7, B:1087:0x14ef, B:1105:0x1516, B:1107:0x151a, B:1126:0x156d, B:1120:0x1558, B:1109:0x153a, B:1111:0x1543, B:1068:0x1497, B:1070:0x149e), top: B:1434:0x147f }] */
    /* JADX WARN: Removed duplicated region for block: B:1109:0x153a A[Catch: all -> 0x14fa, Exception -> 0x14fc, TryCatch #14 {Exception -> 0x14fc, blocks: (B:1061:0x147f, B:1066:0x148d, B:1071:0x14a1, B:1085:0x14e7, B:1087:0x14ef, B:1105:0x1516, B:1107:0x151a, B:1126:0x156d, B:1120:0x1558, B:1109:0x153a, B:1111:0x1543, B:1068:0x1497, B:1070:0x149e), top: B:1434:0x147f }] */
    /* JADX WARN: Removed duplicated region for block: B:1115:0x154b  */
    /* JADX WARN: Removed duplicated region for block: B:1116:0x154e  */
    /* JADX WARN: Removed duplicated region for block: B:1146:0x15a9  */
    /* JADX WARN: Removed duplicated region for block: B:1149:0x15b6 A[Catch: all -> 0x15c8, Exception -> 0x15d0, TryCatch #139 {Exception -> 0x15d0, all -> 0x15c8, blocks: (B:1139:0x1593, B:1147:0x15b0, B:1149:0x15b6, B:1151:0x15ba, B:1152:0x15bf), top: B:1649:0x1593 }] */
    /* JADX WARN: Removed duplicated region for block: B:1154:0x15c5  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x036c A[ADDED_TO_REGION, EDGE_INSN: B:116:0x036c->B:1667:0x036f ?: BREAK  ] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x037b  */
    /* JADX WARN: Removed duplicated region for block: B:122:0x0386  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x0393  */
    /* JADX WARN: Removed duplicated region for block: B:1276:0x18da  */
    /* JADX WARN: Removed duplicated region for block: B:1279:0x18e1  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x03a7  */
    /* JADX WARN: Removed duplicated region for block: B:1291:0x1926 A[Catch: all -> 0x1930, TRY_LEAVE, TryCatch #107 {all -> 0x1930, blocks: (B:1289:0x191d, B:1291:0x1926), top: B:1496:0x191d }] */
    /* JADX WARN: Removed duplicated region for block: B:1307:0x1971  */
    /* JADX WARN: Removed duplicated region for block: B:1313:0x19a4 A[Catch: all -> 0x1993, TryCatch #142 {all -> 0x1993, blocks: (B:1309:0x198e, B:1313:0x19a4, B:1315:0x19ab, B:1317:0x19b5, B:1318:0x19b8), top: B:1514:0x198e }] */
    /* JADX WARN: Removed duplicated region for block: B:1315:0x19ab A[Catch: all -> 0x1993, TryCatch #142 {all -> 0x1993, blocks: (B:1309:0x198e, B:1313:0x19a4, B:1315:0x19ab, B:1317:0x19b5, B:1318:0x19b8), top: B:1514:0x198e }] */
    /* JADX WARN: Removed duplicated region for block: B:1317:0x19b5 A[Catch: all -> 0x1993, TryCatch #142 {all -> 0x1993, blocks: (B:1309:0x198e, B:1313:0x19a4, B:1315:0x19ab, B:1317:0x19b5, B:1318:0x19b8), top: B:1514:0x198e }] */
    /* JADX WARN: Removed duplicated region for block: B:1322:0x19cb  */
    /* JADX WARN: Removed duplicated region for block: B:1359:0x1a6e  */
    /* JADX WARN: Removed duplicated region for block: B:1382:0x1aa7  */
    /* JADX WARN: Removed duplicated region for block: B:1384:0x1ab3  */
    /* JADX WARN: Removed duplicated region for block: B:1428:0x0ae7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1445:0x1a9d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1449:0x1a96 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1451:0x1a75 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1457:0x0299 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1470:0x0b19 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1474:0x033a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1488:0x19ec A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1490:0x19e5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1492:0x19f3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1508:0x19fa A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1514:0x198e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1525:0x1a88 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1529:0x1a8f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1531:0x19d2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1534:0x0e0c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1539:0x0802 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1555:0x0238 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1564:0x0af0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1566:0x0c76 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1623:0x0e53 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1651:0x1563 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:1653:0x01ab A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:268:0x05b6  */
    /* JADX WARN: Removed duplicated region for block: B:270:0x05c8  */
    /* JADX WARN: Removed duplicated region for block: B:363:0x07c3 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:380:0x081f A[Catch: all -> 0x0807, TryCatch #184 {all -> 0x0807, blocks: (B:376:0x0802, B:380:0x081f, B:382:0x0825, B:384:0x082e, B:385:0x0831), top: B:1539:0x0802 }] */
    /* JADX WARN: Removed duplicated region for block: B:382:0x0825 A[Catch: all -> 0x0807, TryCatch #184 {all -> 0x0807, blocks: (B:376:0x0802, B:380:0x081f, B:382:0x0825, B:384:0x082e, B:385:0x0831), top: B:1539:0x0802 }] */
    /* JADX WARN: Removed duplicated region for block: B:384:0x082e A[Catch: all -> 0x0807, TryCatch #184 {all -> 0x0807, blocks: (B:376:0x0802, B:380:0x081f, B:382:0x0825, B:384:0x082e, B:385:0x0831), top: B:1539:0x0802 }] */
    /* JADX WARN: Removed duplicated region for block: B:448:0x09c9  */
    /* JADX WARN: Removed duplicated region for block: B:544:0x0b5b  */
    /* JADX WARN: Removed duplicated region for block: B:547:0x0b74 A[Catch: Exception -> 0x0b40, all -> 0x0b43, TRY_ENTER, TryCatch #88 {Exception -> 0x0b40, blocks: (B:533:0x0b20, B:547:0x0b74, B:549:0x0b7a, B:553:0x0b96, B:555:0x0b9e, B:557:0x0baa, B:559:0x0bb2, B:561:0x0bbe, B:563:0x0bc6, B:575:0x0be3), top: B:1480:0x0b20 }] */
    /* JADX WARN: Removed duplicated region for block: B:553:0x0b96 A[Catch: Exception -> 0x0b40, all -> 0x0b43, TRY_ENTER, TryCatch #88 {Exception -> 0x0b40, blocks: (B:533:0x0b20, B:547:0x0b74, B:549:0x0b7a, B:553:0x0b96, B:555:0x0b9e, B:557:0x0baa, B:559:0x0bb2, B:561:0x0bbe, B:563:0x0bc6, B:575:0x0be3), top: B:1480:0x0b20 }] */
    /* JADX WARN: Removed duplicated region for block: B:571:0x0bd6  */
    /* JADX WARN: Removed duplicated region for block: B:575:0x0be3 A[Catch: Exception -> 0x0b40, all -> 0x0b43, TRY_LEAVE, TryCatch #88 {Exception -> 0x0b40, blocks: (B:533:0x0b20, B:547:0x0b74, B:549:0x0b7a, B:553:0x0b96, B:555:0x0b9e, B:557:0x0baa, B:559:0x0bb2, B:561:0x0bbe, B:563:0x0bc6, B:575:0x0be3), top: B:1480:0x0b20 }] */
    /* JADX WARN: Removed duplicated region for block: B:589:0x0c0b  */
    /* JADX WARN: Removed duplicated region for block: B:621:0x0cc7 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:624:0x0ccf A[Catch: all -> 0x0d37, Exception -> 0x0d4a, TRY_LEAVE, TryCatch #153 {Exception -> 0x0d4a, all -> 0x0d37, blocks: (B:622:0x0cc9, B:624:0x0ccf), top: B:1621:0x0cc9 }] */
    /* JADX WARN: Removed duplicated region for block: B:646:0x0d79  */
    /* JADX WARN: Removed duplicated region for block: B:691:0x0e3c  */
    /* JADX WARN: Removed duplicated region for block: B:711:0x0ea2  */
    /* JADX WARN: Removed duplicated region for block: B:713:0x0ea5  */
    /* JADX WARN: Removed duplicated region for block: B:736:0x0f2c  */
    /* JADX WARN: Removed duplicated region for block: B:744:0x0f49  */
    /* JADX WARN: Removed duplicated region for block: B:757:0x0f8b  */
    /* JADX WARN: Removed duplicated region for block: B:758:0x0f8d  */
    /* JADX WARN: Removed duplicated region for block: B:762:0x0faf A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:768:0x0fcc  */
    /* JADX WARN: Removed duplicated region for block: B:781:0x0ffe  */
    /* JADX WARN: Removed duplicated region for block: B:783:0x1004  */
    /* JADX WARN: Removed duplicated region for block: B:859:0x114b  */
    /* JADX WARN: Removed duplicated region for block: B:868:0x1169  */
    /* JADX WARN: Removed duplicated region for block: B:877:0x1185  */
    /* JADX WARN: Removed duplicated region for block: B:880:0x119c A[ADDED_TO_REGION, EDGE_INSN: B:880:0x119c->B:1682:0x119f ?: BREAK  ] */
    /* JADX WARN: Removed duplicated region for block: B:886:0x11bd  */
    /* JADX WARN: Removed duplicated region for block: B:887:0x11cc  */
    /* JADX WARN: Removed duplicated region for block: B:891:0x11dd  */
    /* JADX WARN: Removed duplicated region for block: B:892:0x11f1  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x02c2  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x02e5 A[Catch: all -> 0x029e, Exception -> 0x02b9, TRY_ENTER, TRY_LEAVE, TryCatch #43 {Exception -> 0x02b9, blocks: (B:82:0x0299, B:94:0x02e5), top: B:1457:0x0299 }] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0312  */
    /* JADX WARN: Type inference failed for: r10v15 */
    /* JADX WARN: Type inference failed for: r10v16 */
    /* JADX WARN: Type inference failed for: r10v9 */
    /* JADX WARN: Type inference failed for: r12v129 */
    /* JADX WARN: Type inference failed for: r12v130 */
    /* JADX WARN: Type inference failed for: r12v131 */
    /* JADX WARN: Type inference failed for: r3v1, types: [long] */
    /* JADX WARN: Type inference failed for: r5v168 */
    /* JADX WARN: Type inference failed for: r5v169 */
    /* JADX WARN: Type inference failed for: r5v46, types: [android.media.MediaExtractor] */
    /* JADX WARN: Type inference failed for: r5v69 */
    /* JADX WARN: Type inference failed for: r5v91 */
    /* JADX WARN: Type inference failed for: r7v48, types: [org.telegram.messenger.video.MP4Builder] */
    /* JADX WARN: Type inference failed for: r89v0, types: [org.telegram.messenger.video.MediaCodecVideoConvertor] */
    /* JADX WARN: Type inference failed for: r9v37, types: [boolean] */
    @TargetApi(18)
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private boolean convertVideoInternal(ConvertVideoParams convertVideoParams, boolean z, int i) {
        boolean z2;
        File file;
        boolean z3;
        int i2;
        String str;
        Throwable th;
        boolean z4;
        MediaCodec mediaCodec;
        String str2;
        int i3;
        InputSurface inputSurface;
        MediaCodec mediaCodec2;
        boolean z5;
        OutputSurface outputSurface;
        MediaExtractor mediaExtractor;
        MP4Builder mP4Builder;
        String str3;
        boolean z6;
        boolean z7;
        MediaCodec.BufferInfo bufferInfo;
        Mp4Movie mp4Movie;
        long j;
        float f;
        long j2;
        String str4;
        String str5;
        String str6;
        int findTrack;
        int i4;
        String str7;
        int i5;
        int i6;
        boolean z8;
        int i7;
        int i8;
        MediaCodec mediaCodec3;
        OutputSurface outputSurface2;
        int i9;
        MediaCodec mediaCodec4;
        AudioRecoder audioRecoder;
        InputSurface inputSurface2;
        MediaCodec mediaCodec5;
        OutputSurface outputSurface3;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        Exception exc;
        int i16;
        MediaCodec mediaCodec6;
        MediaCodec mediaCodec7;
        OutputSurface outputSurface4;
        long j3;
        int i17;
        long j4;
        MediaFormat trackFormat;
        int i18;
        int i19;
        String str8;
        long j5;
        Object obj;
        long j6;
        long j7;
        int i20;
        int i21;
        int i22;
        MediaCodec createByCodecName;
        StringBuilder sb;
        MediaFormat createVideoFormat;
        int i23;
        String str9;
        Mp4Movie mp4Movie2;
        int i24;
        int i25;
        boolean z9;
        int i26;
        int i27;
        String name;
        int i28;
        OutputSurface outputSurface5;
        int i29;
        OutputSurface outputSurface6;
        float f2;
        int i30;
        long j8;
        Object obj2;
        int i31;
        boolean z10;
        long j9;
        long j10;
        MediaCodec.BufferInfo bufferInfo2;
        long j11;
        MediaFormat mediaFormat;
        int i32;
        MediaCodec.BufferInfo bufferInfo3;
        String createFragmentShader;
        int i33;
        int i34;
        ByteBuffer[] inputBuffers;
        ByteBuffer[] outputBuffers;
        ConvertVideoParams convertVideoParams2;
        String str10;
        ?? r10;
        MediaCodec mediaCodec8;
        long j12;
        int i35;
        AudioRecoder audioRecoder2;
        int i36;
        ByteBuffer byteBuffer;
        int i37;
        int i38;
        ByteBuffer[] byteBufferArr;
        int i39;
        boolean z11;
        ByteBuffer byteBuffer2;
        long j13;
        long j14;
        long j15;
        long j16;
        long j17;
        boolean z12;
        boolean z13;
        boolean z14;
        int i40;
        boolean z15;
        int i41;
        int i42;
        boolean z16;
        ByteBuffer[] byteBufferArr2;
        boolean z17;
        MediaCodec.BufferInfo bufferInfo4;
        int i43;
        int i44;
        MediaCodec mediaCodec9;
        MediaCodec mediaCodec10;
        Exception e;
        boolean z18;
        long j18;
        int i45;
        boolean z19;
        OutputSurface outputSurface7;
        MediaCodec mediaCodec11;
        MediaCodec mediaCodec12;
        boolean z20;
        boolean z21;
        long j19;
        int i46;
        MediaCodec mediaCodec13;
        int i47;
        long j20;
        int i48;
        String str11;
        int i49;
        int i50;
        int i51;
        ByteBuffer outputBuffer;
        int i52;
        ByteBuffer byteBuffer3;
        ByteBuffer byteBuffer4;
        boolean z22;
        MediaController.VideoConvertorListener videoConvertorListener;
        boolean z23;
        boolean z24;
        int i53;
        long j21;
        int i54;
        long j22;
        long j23;
        InputSurface inputSurface3;
        int dequeueOutputBuffer;
        boolean z25;
        boolean z26;
        long j24;
        long j25;
        long j26;
        boolean z27;
        boolean z28;
        OutputSurface outputSurface8;
        InputSurface inputSurface4;
        OutputSurface outputSurface9;
        OutputSurface outputSurface10;
        boolean z29;
        MediaController.VideoConvertorListener videoConvertorListener2;
        ByteBuffer inputBuffer;
        ?? r12;
        int i55;
        int i56;
        long j27;
        boolean z30;
        MediaExtractor mediaExtractor2;
        MP4Builder mP4Builder2;
        MediaController.CropState cropState;
        Exception exc2;
        int i57;
        int i58;
        InputSurface inputSurface5;
        OutputSurface outputSurface11;
        AudioRecoder audioRecoder3;
        String str12;
        boolean z31;
        float f3;
        boolean z32;
        int i59;
        int i60;
        int i61;
        String name2;
        OutputSurface outputSurface12;
        MediaController.CropState cropState2;
        int i62;
        ByteBuffer[] outputBuffers2;
        boolean z33;
        int i63;
        AudioRecoder audioRecoder4;
        boolean z34;
        boolean z35;
        int i64;
        boolean z36;
        int i65;
        boolean z37;
        long j28;
        int dequeueOutputBuffer2;
        int i66;
        InputSurface inputSurface6;
        int i67;
        OutputSurface outputSurface13;
        String str13;
        int i68;
        ByteBuffer outputBuffer2;
        int i69;
        ByteBuffer byteBuffer5;
        ByteBuffer byteBuffer6;
        MediaController.VideoConvertorListener videoConvertorListener3;
        InputSurface inputSurface7;
        int i70;
        String str14 = convertVideoParams.videoPath;
        File file2 = convertVideoParams.cacheFile;
        int i71 = convertVideoParams.rotationValue;
        boolean z38 = convertVideoParams.isSecret;
        int i72 = convertVideoParams.originalWidth;
        int i73 = convertVideoParams.originalHeight;
        int i74 = convertVideoParams.resultWidth;
        int i75 = convertVideoParams.resultHeight;
        int i76 = convertVideoParams.framerate;
        int i77 = convertVideoParams.bitrate;
        int i78 = convertVideoParams.originalBitrate;
        ?? r3 = convertVideoParams.startTime;
        long j29 = convertVideoParams.endTime;
        long j30 = convertVideoParams.avatarStartTime;
        boolean z39 = convertVideoParams.needCompress;
        long j31 = convertVideoParams.duration;
        MediaController.SavedFilterState savedFilterState = convertVideoParams.savedFilterState;
        String str15 = convertVideoParams.paintPath;
        String str16 = convertVideoParams.blurPath;
        ArrayList<VideoEditedInfo.MediaEntity> arrayList = convertVideoParams.mediaEntities;
        boolean z40 = convertVideoParams.isPhoto;
        MediaController.CropState cropState3 = convertVideoParams.cropState;
        boolean z41 = convertVideoParams.isRound;
        Integer num = convertVideoParams.gradientTopColor;
        Integer num2 = convertVideoParams.gradientBottomColor;
        boolean z42 = convertVideoParams.muted;
        float f4 = convertVideoParams.volume;
        boolean z43 = convertVideoParams.isStory;
        StoryEntry.HDRInfo hDRInfo = convertVideoParams.hdrInfo;
        FileLog.d("convertVideoInternal original=" + i72 + "x" + i73 + "  result=" + i74 + "x" + i75 + " " + j30);
        long currentTimeMillis = System.currentTimeMillis();
        boolean z44 = j30 >= 0;
        this.outputMimeType = z43 ? "video/hevc" : MediaController.VIDEO_MIME_TYPE;
        try {
            bufferInfo = new MediaCodec.BufferInfo();
            mp4Movie = new Mp4Movie();
            mp4Movie.setCacheFile(file2);
            j = j30;
            mp4Movie.setRotation(0);
            mp4Movie.setSize(i74, i75);
            f = ((float) j31) / 1000.0f;
            j2 = j31 * 1000;
            this.endPresentationTime = j2;
            checkConversionCanceled();
            str4 = "csd-0";
            str5 = "prepend-sps-pps-to-idr-frames";
        } catch (Throwable th2) {
            th = th2;
            z2 = z;
            file = file2;
            z3 = z39;
            i2 = i76;
            str = "x";
        }
        if (z40) {
            if (z44) {
                i77 = f <= 2000.0f ? 2600000 : f <= 5000.0f ? 2200000 : 1560000;
            } else if (i77 <= 0) {
                i77 = 921600;
            }
            try {
                try {
                    try {
                        try {
                            if (cropState3 != null) {
                                cropState = cropState3;
                                try {
                                    if (cropState.useMatrix != null) {
                                        f3 = f;
                                        i60 = i75;
                                        i61 = i74;
                                        if (BuildVars.LOGS_ENABLED) {
                                            try {
                                                FileLog.d("create photo encoder " + i61 + " " + i60 + " duration = " + j31);
                                            } catch (Exception e2) {
                                                e = e2;
                                                i75 = i60;
                                                i57 = i77;
                                                i74 = i61;
                                                i58 = i76;
                                                mediaCodec2 = null;
                                                inputSurface5 = null;
                                                i10 = -5;
                                                outputSurface11 = null;
                                                audioRecoder3 = null;
                                                str12 = null;
                                                z31 = false;
                                                exc2 = e;
                                                try {
                                                    if (exc2 instanceof IllegalStateException) {
                                                    }
                                                    try {
                                                        StringBuilder sb2 = new StringBuilder();
                                                        sb2.append("bitrate: ");
                                                        i77 = i57;
                                                        try {
                                                            sb2.append(i77);
                                                            sb2.append(" framerate: ");
                                                            i59 = i58;
                                                            try {
                                                                sb2.append(i59);
                                                                sb2.append(" size: ");
                                                                sb2.append(i75);
                                                                str = "x";
                                                                try {
                                                                    sb2.append(str);
                                                                    sb2.append(i74);
                                                                    FileLog.e(sb2.toString());
                                                                    FileLog.e(exc2);
                                                                    z6 = z32;
                                                                    i60 = i75;
                                                                    z7 = true;
                                                                    outputSurface3 = outputSurface11;
                                                                    if (outputSurface3 != null) {
                                                                    }
                                                                    if (inputSurface5 != null) {
                                                                    }
                                                                    if (mediaCodec2 != null) {
                                                                    }
                                                                    if (audioRecoder3 != null) {
                                                                    }
                                                                    checkConversionCanceled();
                                                                    mediaCodec4 = mediaCodec2;
                                                                    inputSurface2 = inputSurface5;
                                                                    i4 = i60;
                                                                    z3 = z39;
                                                                    str2 = str12;
                                                                    z4 = z31;
                                                                    file = file2;
                                                                    mediaCodec5 = null;
                                                                    z2 = z;
                                                                    mediaExtractor2 = this.extractor;
                                                                    if (mediaExtractor2 != null) {
                                                                    }
                                                                    mP4Builder2 = this.mediaMuxer;
                                                                    if (mP4Builder2 != null) {
                                                                    }
                                                                    if (mediaCodec4 != null) {
                                                                    }
                                                                    if (mediaCodec5 != null) {
                                                                    }
                                                                    if (outputSurface3 != null) {
                                                                    }
                                                                    if (inputSurface2 != null) {
                                                                    }
                                                                    str3 = str2;
                                                                    i75 = i4;
                                                                } catch (Throwable th3) {
                                                                    z2 = z;
                                                                    th = th3;
                                                                    outputSurface = outputSurface11;
                                                                    z5 = z32;
                                                                    z3 = z39;
                                                                    str2 = str12;
                                                                    z4 = z31;
                                                                    file = file2;
                                                                    mediaCodec = null;
                                                                    i3 = i10;
                                                                    inputSurface = inputSurface5;
                                                                    i2 = i59;
                                                                    try {
                                                                        FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                        FileLog.e(th);
                                                                        mediaExtractor = this.extractor;
                                                                        if (mediaExtractor != null) {
                                                                        }
                                                                        mP4Builder = this.mediaMuxer;
                                                                        if (mP4Builder != null) {
                                                                        }
                                                                        if (mediaCodec2 != null) {
                                                                        }
                                                                        if (mediaCodec != null) {
                                                                        }
                                                                        if (outputSurface != null) {
                                                                        }
                                                                        if (inputSurface != null) {
                                                                        }
                                                                        str3 = str2;
                                                                        z6 = z5;
                                                                        z7 = true;
                                                                        if (z6) {
                                                                        }
                                                                    } catch (Throwable th4) {
                                                                        MediaExtractor mediaExtractor3 = this.extractor;
                                                                        if (mediaExtractor3 != null) {
                                                                            mediaExtractor3.release();
                                                                        }
                                                                        MP4Builder mP4Builder3 = this.mediaMuxer;
                                                                        if (mP4Builder3 != null) {
                                                                            try {
                                                                                mP4Builder3.finishMovie();
                                                                                this.endPresentationTime = this.mediaMuxer.getLastFrameTimestamp(i3);
                                                                            } catch (Throwable th5) {
                                                                                FileLog.e(th5);
                                                                            }
                                                                        }
                                                                        if (mediaCodec2 != null) {
                                                                            try {
                                                                                mediaCodec2.release();
                                                                            } catch (Exception unused) {
                                                                            }
                                                                        }
                                                                        if (mediaCodec != null) {
                                                                            try {
                                                                                mediaCodec.release();
                                                                            } catch (Exception unused2) {
                                                                            }
                                                                        }
                                                                        if (outputSurface != null) {
                                                                            try {
                                                                                outputSurface.release();
                                                                            } catch (Exception unused3) {
                                                                            }
                                                                        }
                                                                        if (inputSurface != null) {
                                                                            try {
                                                                                inputSurface.release();
                                                                            } catch (Exception unused4) {
                                                                            }
                                                                        }
                                                                        throw th4;
                                                                    }
                                                                }
                                                            } catch (Throwable th6) {
                                                                z2 = z;
                                                                th = th6;
                                                                outputSurface = outputSurface11;
                                                                z5 = z32;
                                                                z3 = z39;
                                                                str2 = str12;
                                                                z4 = z31;
                                                                str = "x";
                                                            }
                                                        } catch (Throwable th7) {
                                                            th = th7;
                                                            z2 = z;
                                                            th = th;
                                                            outputSurface = outputSurface11;
                                                            z5 = z32;
                                                            z3 = z39;
                                                            str2 = str12;
                                                            z4 = z31;
                                                            str = "x";
                                                            file = file2;
                                                            mediaCodec = null;
                                                            i3 = i10;
                                                            inputSurface = inputSurface5;
                                                            i2 = i58;
                                                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                            FileLog.e(th);
                                                            mediaExtractor = this.extractor;
                                                            if (mediaExtractor != null) {
                                                            }
                                                            mP4Builder = this.mediaMuxer;
                                                            if (mP4Builder != null) {
                                                            }
                                                            if (mediaCodec2 != null) {
                                                            }
                                                            if (mediaCodec != null) {
                                                            }
                                                            if (outputSurface != null) {
                                                            }
                                                            if (inputSurface != null) {
                                                            }
                                                            str3 = str2;
                                                            z6 = z5;
                                                            z7 = true;
                                                            if (z6) {
                                                            }
                                                        }
                                                    } catch (Throwable th8) {
                                                        th = th8;
                                                        i77 = i57;
                                                    }
                                                } catch (Throwable th9) {
                                                    i77 = i57;
                                                    z2 = z;
                                                    th = th9;
                                                    i3 = i10;
                                                    outputSurface = outputSurface11;
                                                    z3 = z39;
                                                    str2 = str12;
                                                    z4 = z31;
                                                    str = "x";
                                                    file = file2;
                                                    mediaCodec = null;
                                                    z5 = false;
                                                }
                                                if (z6) {
                                                }
                                            } catch (Throwable th10) {
                                                th = th10;
                                                th = th;
                                                i75 = i60;
                                                i74 = i61;
                                                z3 = z39;
                                                i2 = i76;
                                                str = "x";
                                                file = file2;
                                                mediaCodec2 = null;
                                                inputSurface = null;
                                                i3 = -5;
                                                str2 = null;
                                                mediaCodec = null;
                                                z4 = false;
                                                z5 = false;
                                                outputSurface = null;
                                                z2 = z;
                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                FileLog.e(th);
                                                mediaExtractor = this.extractor;
                                                if (mediaExtractor != null) {
                                                }
                                                mP4Builder = this.mediaMuxer;
                                                if (mP4Builder != null) {
                                                }
                                                if (mediaCodec2 != null) {
                                                }
                                                if (mediaCodec != null) {
                                                }
                                                if (outputSurface != null) {
                                                }
                                                if (inputSurface != null) {
                                                }
                                                str3 = str2;
                                                z6 = z5;
                                                z7 = true;
                                                if (z6) {
                                                }
                                            }
                                        }
                                        mediaCodec2 = createEncoderForMimeType();
                                        MediaFormat createVideoFormat2 = MediaFormat.createVideoFormat(this.outputMimeType, i61, i60);
                                        createVideoFormat2.setInteger("color-format", 2130708361);
                                        createVideoFormat2.setInteger("bitrate", i77);
                                        createVideoFormat2.setInteger("frame-rate", 30);
                                        createVideoFormat2.setInteger("i-frame-interval", 1);
                                        name2 = mediaCodec2.getName();
                                        z31 = "c2.qti.avc.encoder".equalsIgnoreCase(name2);
                                        StringBuilder sb3 = new StringBuilder();
                                        i57 = i77;
                                        sb3.append("selected encoder ");
                                        sb3.append(name2);
                                        FileLog.d(sb3.toString());
                                        mediaCodec2.configure(createVideoFormat2, (Surface) null, (MediaCrypto) null, 1);
                                        inputSurface5 = new InputSurface(mediaCodec2.createInputSurface());
                                        inputSurface5.makeCurrent();
                                        mediaCodec2.start();
                                        if (cropState != null) {
                                            try {
                                                if (cropState.useMatrix != null) {
                                                    cropState2 = cropState;
                                                    i62 = i76;
                                                    outputSurface11 = outputSurface12;
                                                    str12 = name2;
                                                    outputSurface12 = new OutputSurface(savedFilterState, str14, str15, str16, arrayList, cropState2, i61, i60, i72, i73, i71, i62, true, num, num2, null, convertVideoParams);
                                                    if (Build.VERSION.SDK_INT < 21) {
                                                        try {
                                                            try {
                                                                outputBuffers2 = mediaCodec2.getOutputBuffers();
                                                            } catch (Exception e3) {
                                                                e = e3;
                                                                i75 = i60;
                                                                i58 = i62;
                                                                i74 = i61;
                                                                i10 = -5;
                                                                audioRecoder3 = null;
                                                                exc2 = e;
                                                                if (exc2 instanceof IllegalStateException) {
                                                                }
                                                                StringBuilder sb22 = new StringBuilder();
                                                                sb22.append("bitrate: ");
                                                                i77 = i57;
                                                                sb22.append(i77);
                                                                sb22.append(" framerate: ");
                                                                i59 = i58;
                                                                sb22.append(i59);
                                                                sb22.append(" size: ");
                                                                sb22.append(i75);
                                                                str = "x";
                                                                sb22.append(str);
                                                                sb22.append(i74);
                                                                FileLog.e(sb22.toString());
                                                                FileLog.e(exc2);
                                                                z6 = z32;
                                                                i60 = i75;
                                                                z7 = true;
                                                                outputSurface3 = outputSurface11;
                                                                if (outputSurface3 != null) {
                                                                }
                                                                if (inputSurface5 != null) {
                                                                }
                                                                if (mediaCodec2 != null) {
                                                                }
                                                                if (audioRecoder3 != null) {
                                                                }
                                                                checkConversionCanceled();
                                                                mediaCodec4 = mediaCodec2;
                                                                inputSurface2 = inputSurface5;
                                                                i4 = i60;
                                                                z3 = z39;
                                                                str2 = str12;
                                                                z4 = z31;
                                                                file = file2;
                                                                mediaCodec5 = null;
                                                                z2 = z;
                                                                mediaExtractor2 = this.extractor;
                                                                if (mediaExtractor2 != null) {
                                                                }
                                                                mP4Builder2 = this.mediaMuxer;
                                                                if (mP4Builder2 != null) {
                                                                }
                                                                if (mediaCodec4 != null) {
                                                                }
                                                                if (mediaCodec5 != null) {
                                                                }
                                                                if (outputSurface3 != null) {
                                                                }
                                                                if (inputSurface2 != null) {
                                                                }
                                                                str3 = str2;
                                                                i75 = i4;
                                                                if (z6) {
                                                                }
                                                            }
                                                        } catch (Throwable th11) {
                                                            th = th11;
                                                            inputSurface = inputSurface5;
                                                            i75 = i60;
                                                            i2 = i62;
                                                            i74 = i61;
                                                            outputSurface = outputSurface11;
                                                            z3 = z39;
                                                            str2 = str12;
                                                            z4 = z31;
                                                            i77 = i57;
                                                            str = "x";
                                                            file = file2;
                                                            i3 = -5;
                                                            mediaCodec = null;
                                                            z5 = false;
                                                            z2 = z;
                                                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                            FileLog.e(th);
                                                            mediaExtractor = this.extractor;
                                                            if (mediaExtractor != null) {
                                                            }
                                                            mP4Builder = this.mediaMuxer;
                                                            if (mP4Builder != null) {
                                                            }
                                                            if (mediaCodec2 != null) {
                                                            }
                                                            if (mediaCodec != null) {
                                                            }
                                                            if (outputSurface != null) {
                                                            }
                                                            if (inputSurface != null) {
                                                            }
                                                            str3 = str2;
                                                            z6 = z5;
                                                            z7 = true;
                                                            if (z6) {
                                                            }
                                                        }
                                                    } else {
                                                        outputBuffers2 = null;
                                                    }
                                                    checkConversionCanceled();
                                                    ByteBuffer[] byteBufferArr3 = outputBuffers2;
                                                    this.mediaMuxer = new MP4Builder().createMovie(mp4Movie, z38, this.outputMimeType.equals("video/hevc"));
                                                    if (convertVideoParams.soundInfos.isEmpty()) {
                                                        z33 = true;
                                                        i63 = -1;
                                                        i3 = -5;
                                                        audioRecoder4 = null;
                                                    } else {
                                                        ArrayList arrayList2 = new ArrayList();
                                                        arrayList2.add(new BlankAudioInput(j2));
                                                        applyAudioInputs(convertVideoParams.soundInfos, arrayList2);
                                                        audioRecoder4 = new AudioRecoder(arrayList2, j2);
                                                        try {
                                                            i63 = this.mediaMuxer.addTrack(audioRecoder4.format, true);
                                                            z33 = false;
                                                            i3 = -5;
                                                        } catch (Exception e4) {
                                                            e = e4;
                                                            i58 = i62;
                                                            i74 = i61;
                                                            audioRecoder3 = audioRecoder4;
                                                            i10 = -5;
                                                            i75 = i60;
                                                            exc2 = e;
                                                            z32 = ((exc2 instanceof IllegalStateException) || z) ? false : true;
                                                            StringBuilder sb222 = new StringBuilder();
                                                            sb222.append("bitrate: ");
                                                            i77 = i57;
                                                            sb222.append(i77);
                                                            sb222.append(" framerate: ");
                                                            i59 = i58;
                                                            sb222.append(i59);
                                                            sb222.append(" size: ");
                                                            sb222.append(i75);
                                                            str = "x";
                                                            sb222.append(str);
                                                            sb222.append(i74);
                                                            FileLog.e(sb222.toString());
                                                            FileLog.e(exc2);
                                                            z6 = z32;
                                                            i60 = i75;
                                                            z7 = true;
                                                            outputSurface3 = outputSurface11;
                                                            if (outputSurface3 != null) {
                                                                try {
                                                                    outputSurface3.release();
                                                                    outputSurface3 = null;
                                                                } catch (Throwable th12) {
                                                                    th = th12;
                                                                    i3 = i10;
                                                                    outputSurface = outputSurface3;
                                                                    z3 = z39;
                                                                    str2 = str12;
                                                                    z4 = z31;
                                                                    z5 = z6;
                                                                    file = file2;
                                                                    mediaCodec = null;
                                                                    inputSurface = inputSurface5;
                                                                    i75 = i60;
                                                                    i2 = i59;
                                                                    z2 = z;
                                                                    FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                    FileLog.e(th);
                                                                    mediaExtractor = this.extractor;
                                                                    if (mediaExtractor != null) {
                                                                    }
                                                                    mP4Builder = this.mediaMuxer;
                                                                    if (mP4Builder != null) {
                                                                    }
                                                                    if (mediaCodec2 != null) {
                                                                    }
                                                                    if (mediaCodec != null) {
                                                                    }
                                                                    if (outputSurface != null) {
                                                                    }
                                                                    if (inputSurface != null) {
                                                                    }
                                                                    str3 = str2;
                                                                    z6 = z5;
                                                                    z7 = true;
                                                                    if (z6) {
                                                                    }
                                                                }
                                                            }
                                                            if (inputSurface5 != null) {
                                                                inputSurface5.release();
                                                                inputSurface5 = null;
                                                            }
                                                            if (mediaCodec2 != null) {
                                                                mediaCodec2.stop();
                                                                mediaCodec2.release();
                                                                mediaCodec2 = null;
                                                            }
                                                            if (audioRecoder3 != null) {
                                                                audioRecoder3.release();
                                                            }
                                                            checkConversionCanceled();
                                                            mediaCodec4 = mediaCodec2;
                                                            inputSurface2 = inputSurface5;
                                                            i4 = i60;
                                                            z3 = z39;
                                                            str2 = str12;
                                                            z4 = z31;
                                                            file = file2;
                                                            mediaCodec5 = null;
                                                            z2 = z;
                                                            mediaExtractor2 = this.extractor;
                                                            if (mediaExtractor2 != null) {
                                                            }
                                                            mP4Builder2 = this.mediaMuxer;
                                                            if (mP4Builder2 != null) {
                                                            }
                                                            if (mediaCodec4 != null) {
                                                            }
                                                            if (mediaCodec5 != null) {
                                                            }
                                                            if (outputSurface3 != null) {
                                                            }
                                                            if (inputSurface2 != null) {
                                                            }
                                                            str3 = str2;
                                                            i75 = i4;
                                                            if (z6) {
                                                            }
                                                        }
                                                    }
                                                    z34 = false;
                                                    long j32 = 0;
                                                    z35 = false;
                                                    int i79 = 0;
                                                    i64 = 0;
                                                    boolean z45 = true;
                                                    loop0: while (true) {
                                                        if (!z34 && z33) {
                                                            i59 = i62;
                                                            i74 = i61;
                                                            i10 = i3;
                                                            audioRecoder3 = audioRecoder4;
                                                            i77 = i57;
                                                            str = "x";
                                                            z7 = false;
                                                            z6 = false;
                                                            break;
                                                        }
                                                        try {
                                                            try {
                                                                checkConversionCanceled();
                                                                if (audioRecoder4 != null) {
                                                                    try {
                                                                        try {
                                                                            z33 = audioRecoder4.step(this.mediaMuxer, i63);
                                                                        } catch (Exception e5) {
                                                                            e = e5;
                                                                            i58 = i62;
                                                                            i74 = i61;
                                                                            i10 = i3;
                                                                            audioRecoder3 = audioRecoder4;
                                                                            i75 = i60;
                                                                            exc2 = e;
                                                                            if (exc2 instanceof IllegalStateException) {
                                                                            }
                                                                            StringBuilder sb2222 = new StringBuilder();
                                                                            sb2222.append("bitrate: ");
                                                                            i77 = i57;
                                                                            sb2222.append(i77);
                                                                            sb2222.append(" framerate: ");
                                                                            i59 = i58;
                                                                            sb2222.append(i59);
                                                                            sb2222.append(" size: ");
                                                                            sb2222.append(i75);
                                                                            str = "x";
                                                                            sb2222.append(str);
                                                                            sb2222.append(i74);
                                                                            FileLog.e(sb2222.toString());
                                                                            FileLog.e(exc2);
                                                                            z6 = z32;
                                                                            i60 = i75;
                                                                            z7 = true;
                                                                            outputSurface3 = outputSurface11;
                                                                            if (outputSurface3 != null) {
                                                                            }
                                                                            if (inputSurface5 != null) {
                                                                            }
                                                                            if (mediaCodec2 != null) {
                                                                            }
                                                                            if (audioRecoder3 != null) {
                                                                            }
                                                                            checkConversionCanceled();
                                                                            mediaCodec4 = mediaCodec2;
                                                                            inputSurface2 = inputSurface5;
                                                                            i4 = i60;
                                                                            z3 = z39;
                                                                            str2 = str12;
                                                                            z4 = z31;
                                                                            file = file2;
                                                                            mediaCodec5 = null;
                                                                            z2 = z;
                                                                            mediaExtractor2 = this.extractor;
                                                                            if (mediaExtractor2 != null) {
                                                                            }
                                                                            mP4Builder2 = this.mediaMuxer;
                                                                            if (mP4Builder2 != null) {
                                                                            }
                                                                            if (mediaCodec4 != null) {
                                                                            }
                                                                            if (mediaCodec5 != null) {
                                                                            }
                                                                            if (outputSurface3 != null) {
                                                                            }
                                                                            if (inputSurface2 != null) {
                                                                            }
                                                                            str3 = str2;
                                                                            i75 = i4;
                                                                            if (z6) {
                                                                            }
                                                                        }
                                                                    } catch (Throwable th13) {
                                                                        th = th13;
                                                                        th = th;
                                                                        inputSurface = inputSurface5;
                                                                        i75 = i60;
                                                                        i2 = i62;
                                                                        i74 = i61;
                                                                        outputSurface = outputSurface11;
                                                                        z3 = z39;
                                                                        str2 = str12;
                                                                        z4 = z31;
                                                                        i77 = i57;
                                                                        str = "x";
                                                                        file = file2;
                                                                        mediaCodec = null;
                                                                        z5 = false;
                                                                        z2 = z;
                                                                        FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                        FileLog.e(th);
                                                                        mediaExtractor = this.extractor;
                                                                        if (mediaExtractor != null) {
                                                                        }
                                                                        mP4Builder = this.mediaMuxer;
                                                                        if (mP4Builder != null) {
                                                                        }
                                                                        if (mediaCodec2 != null) {
                                                                        }
                                                                        if (mediaCodec != null) {
                                                                        }
                                                                        if (outputSurface != null) {
                                                                        }
                                                                        if (inputSurface != null) {
                                                                        }
                                                                        str3 = str2;
                                                                        z6 = z5;
                                                                        z7 = true;
                                                                        if (z6) {
                                                                        }
                                                                    }
                                                                }
                                                                z36 = !z35;
                                                                boolean z46 = true;
                                                                int i80 = i64;
                                                                boolean z47 = z33;
                                                                i65 = i80;
                                                                while (true) {
                                                                    if (!z36 || z46) {
                                                                        checkConversionCanceled();
                                                                        if (z) {
                                                                            audioRecoder3 = audioRecoder4;
                                                                            z37 = z34;
                                                                            j28 = 22000;
                                                                        } else {
                                                                            audioRecoder3 = audioRecoder4;
                                                                            z37 = z34;
                                                                            j28 = 2500;
                                                                        }
                                                                        try {
                                                                            dequeueOutputBuffer2 = mediaCodec2.dequeueOutputBuffer(bufferInfo, j28);
                                                                            if (dequeueOutputBuffer2 == -1) {
                                                                                inputSurface6 = inputSurface5;
                                                                                i67 = i65;
                                                                                i66 = i63;
                                                                                outputSurface13 = outputSurface11;
                                                                                z34 = z37;
                                                                                str13 = str4;
                                                                                i68 = -1;
                                                                                z46 = false;
                                                                            } else {
                                                                                if (dequeueOutputBuffer2 == -3) {
                                                                                    try {
                                                                                        i66 = i63;
                                                                                        if (Build.VERSION.SDK_INT < 21) {
                                                                                            byteBufferArr3 = mediaCodec2.getOutputBuffers();
                                                                                        }
                                                                                        inputSurface6 = inputSurface5;
                                                                                        i67 = i65;
                                                                                        outputSurface13 = outputSurface11;
                                                                                        z34 = z37;
                                                                                        str13 = str4;
                                                                                    } catch (Exception e6) {
                                                                                        e = e6;
                                                                                        i75 = i60;
                                                                                        i58 = i62;
                                                                                        i74 = i61;
                                                                                        i10 = i3;
                                                                                        exc2 = e;
                                                                                        if (exc2 instanceof IllegalStateException) {
                                                                                        }
                                                                                        StringBuilder sb22222 = new StringBuilder();
                                                                                        sb22222.append("bitrate: ");
                                                                                        i77 = i57;
                                                                                        sb22222.append(i77);
                                                                                        sb22222.append(" framerate: ");
                                                                                        i59 = i58;
                                                                                        sb22222.append(i59);
                                                                                        sb22222.append(" size: ");
                                                                                        sb22222.append(i75);
                                                                                        str = "x";
                                                                                        sb22222.append(str);
                                                                                        sb22222.append(i74);
                                                                                        FileLog.e(sb22222.toString());
                                                                                        FileLog.e(exc2);
                                                                                        z6 = z32;
                                                                                        i60 = i75;
                                                                                        z7 = true;
                                                                                        outputSurface3 = outputSurface11;
                                                                                        if (outputSurface3 != null) {
                                                                                        }
                                                                                        if (inputSurface5 != null) {
                                                                                        }
                                                                                        if (mediaCodec2 != null) {
                                                                                        }
                                                                                        if (audioRecoder3 != null) {
                                                                                        }
                                                                                        checkConversionCanceled();
                                                                                        mediaCodec4 = mediaCodec2;
                                                                                        inputSurface2 = inputSurface5;
                                                                                        i4 = i60;
                                                                                        z3 = z39;
                                                                                        str2 = str12;
                                                                                        z4 = z31;
                                                                                        file = file2;
                                                                                        mediaCodec5 = null;
                                                                                        z2 = z;
                                                                                        mediaExtractor2 = this.extractor;
                                                                                        if (mediaExtractor2 != null) {
                                                                                        }
                                                                                        mP4Builder2 = this.mediaMuxer;
                                                                                        if (mP4Builder2 != null) {
                                                                                        }
                                                                                        if (mediaCodec4 != null) {
                                                                                        }
                                                                                        if (mediaCodec5 != null) {
                                                                                        }
                                                                                        if (outputSurface3 != null) {
                                                                                        }
                                                                                        if (inputSurface2 != null) {
                                                                                        }
                                                                                        str3 = str2;
                                                                                        i75 = i4;
                                                                                        if (z6) {
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    i66 = i63;
                                                                                    if (dequeueOutputBuffer2 == -2) {
                                                                                        MediaFormat outputFormat = mediaCodec2.getOutputFormat();
                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                            FileLog.d("photo encoder new format " + outputFormat);
                                                                                        }
                                                                                        if (i3 != -5 || outputFormat == null) {
                                                                                            str13 = str4;
                                                                                        } else {
                                                                                            i3 = this.mediaMuxer.addTrack(outputFormat, false);
                                                                                            String str17 = str5;
                                                                                            try {
                                                                                                if (outputFormat.containsKey(str17)) {
                                                                                                    str5 = str17;
                                                                                                    if (outputFormat.getInteger(str17) == 1) {
                                                                                                        str13 = str4;
                                                                                                        ByteBuffer byteBuffer7 = outputFormat.getByteBuffer(str13);
                                                                                                        i70 = i3;
                                                                                                        try {
                                                                                                            ByteBuffer byteBuffer8 = outputFormat.getByteBuffer("csd-1");
                                                                                                            i79 = (byteBuffer7 == null ? 0 : byteBuffer7.limit()) + (byteBuffer8 == null ? 0 : byteBuffer8.limit());
                                                                                                            i3 = i70;
                                                                                                        } catch (Exception e7) {
                                                                                                            e = e7;
                                                                                                            i75 = i60;
                                                                                                            i58 = i62;
                                                                                                            i74 = i61;
                                                                                                            i10 = i70;
                                                                                                            exc2 = e;
                                                                                                            if (exc2 instanceof IllegalStateException) {
                                                                                                            }
                                                                                                            StringBuilder sb222222 = new StringBuilder();
                                                                                                            sb222222.append("bitrate: ");
                                                                                                            i77 = i57;
                                                                                                            sb222222.append(i77);
                                                                                                            sb222222.append(" framerate: ");
                                                                                                            i59 = i58;
                                                                                                            sb222222.append(i59);
                                                                                                            sb222222.append(" size: ");
                                                                                                            sb222222.append(i75);
                                                                                                            str = "x";
                                                                                                            sb222222.append(str);
                                                                                                            sb222222.append(i74);
                                                                                                            FileLog.e(sb222222.toString());
                                                                                                            FileLog.e(exc2);
                                                                                                            z6 = z32;
                                                                                                            i60 = i75;
                                                                                                            z7 = true;
                                                                                                            outputSurface3 = outputSurface11;
                                                                                                            if (outputSurface3 != null) {
                                                                                                            }
                                                                                                            if (inputSurface5 != null) {
                                                                                                            }
                                                                                                            if (mediaCodec2 != null) {
                                                                                                            }
                                                                                                            if (audioRecoder3 != null) {
                                                                                                            }
                                                                                                            checkConversionCanceled();
                                                                                                            mediaCodec4 = mediaCodec2;
                                                                                                            inputSurface2 = inputSurface5;
                                                                                                            i4 = i60;
                                                                                                            z3 = z39;
                                                                                                            str2 = str12;
                                                                                                            z4 = z31;
                                                                                                            file = file2;
                                                                                                            mediaCodec5 = null;
                                                                                                            z2 = z;
                                                                                                            mediaExtractor2 = this.extractor;
                                                                                                            if (mediaExtractor2 != null) {
                                                                                                            }
                                                                                                            mP4Builder2 = this.mediaMuxer;
                                                                                                            if (mP4Builder2 != null) {
                                                                                                            }
                                                                                                            if (mediaCodec4 != null) {
                                                                                                            }
                                                                                                            if (mediaCodec5 != null) {
                                                                                                            }
                                                                                                            if (outputSurface3 != null) {
                                                                                                            }
                                                                                                            if (inputSurface2 != null) {
                                                                                                            }
                                                                                                            str3 = str2;
                                                                                                            i75 = i4;
                                                                                                            if (z6) {
                                                                                                            }
                                                                                                        } catch (Throwable th14) {
                                                                                                            th = th14;
                                                                                                            inputSurface = inputSurface5;
                                                                                                            i75 = i60;
                                                                                                            i2 = i62;
                                                                                                            i74 = i61;
                                                                                                            outputSurface = outputSurface11;
                                                                                                            i3 = i70;
                                                                                                            z3 = z39;
                                                                                                            str2 = str12;
                                                                                                            z4 = z31;
                                                                                                            i77 = i57;
                                                                                                            str = "x";
                                                                                                            file = file2;
                                                                                                            mediaCodec = null;
                                                                                                            z5 = false;
                                                                                                            z2 = z;
                                                                                                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                            FileLog.e(th);
                                                                                                            mediaExtractor = this.extractor;
                                                                                                            if (mediaExtractor != null) {
                                                                                                            }
                                                                                                            mP4Builder = this.mediaMuxer;
                                                                                                            if (mP4Builder != null) {
                                                                                                            }
                                                                                                            if (mediaCodec2 != null) {
                                                                                                            }
                                                                                                            if (mediaCodec != null) {
                                                                                                            }
                                                                                                            if (outputSurface != null) {
                                                                                                            }
                                                                                                            if (inputSurface != null) {
                                                                                                            }
                                                                                                            str3 = str2;
                                                                                                            z6 = z5;
                                                                                                            z7 = true;
                                                                                                            if (z6) {
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                } else {
                                                                                                    str5 = str17;
                                                                                                }
                                                                                                i70 = i3;
                                                                                                str13 = str4;
                                                                                                i3 = i70;
                                                                                            } catch (Exception e8) {
                                                                                                e = e8;
                                                                                                i70 = i3;
                                                                                            } catch (Throwable th15) {
                                                                                                th = th15;
                                                                                                th = th;
                                                                                                inputSurface = inputSurface5;
                                                                                                i75 = i60;
                                                                                                i2 = i62;
                                                                                                i74 = i61;
                                                                                                outputSurface = outputSurface11;
                                                                                                z3 = z39;
                                                                                                str2 = str12;
                                                                                                z4 = z31;
                                                                                                i77 = i57;
                                                                                                str = "x";
                                                                                                file = file2;
                                                                                                mediaCodec = null;
                                                                                                z5 = false;
                                                                                                z2 = z;
                                                                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                FileLog.e(th);
                                                                                                mediaExtractor = this.extractor;
                                                                                                if (mediaExtractor != null) {
                                                                                                }
                                                                                                mP4Builder = this.mediaMuxer;
                                                                                                if (mP4Builder != null) {
                                                                                                }
                                                                                                if (mediaCodec2 != null) {
                                                                                                }
                                                                                                if (mediaCodec != null) {
                                                                                                }
                                                                                                if (outputSurface != null) {
                                                                                                }
                                                                                                if (inputSurface != null) {
                                                                                                }
                                                                                                str3 = str2;
                                                                                                z6 = z5;
                                                                                                z7 = true;
                                                                                                if (z6) {
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        inputSurface6 = inputSurface5;
                                                                                        i67 = i65;
                                                                                        outputSurface13 = outputSurface11;
                                                                                        z34 = z37;
                                                                                    } else {
                                                                                        str13 = str4;
                                                                                        if (dequeueOutputBuffer2 < 0) {
                                                                                            throw new RuntimeException("unexpected result from encoder.dequeueOutputBuffer: " + dequeueOutputBuffer2);
                                                                                        }
                                                                                        if (Build.VERSION.SDK_INT < 21) {
                                                                                            outputBuffer2 = byteBufferArr3[dequeueOutputBuffer2];
                                                                                        } else {
                                                                                            outputBuffer2 = mediaCodec2.getOutputBuffer(dequeueOutputBuffer2);
                                                                                        }
                                                                                        if (outputBuffer2 == null) {
                                                                                            throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer2 + " was null");
                                                                                        }
                                                                                        int i81 = bufferInfo.size;
                                                                                        i58 = i62;
                                                                                        try {
                                                                                            if (i81 > 1) {
                                                                                                try {
                                                                                                    int i82 = bufferInfo.flags;
                                                                                                    if ((i82 & 2) != 0) {
                                                                                                        inputSurface6 = inputSurface5;
                                                                                                        i67 = i65;
                                                                                                        outputSurface13 = outputSurface11;
                                                                                                        i69 = dequeueOutputBuffer2;
                                                                                                        if (i3 == -5) {
                                                                                                            if (this.outputMimeType.equals("video/hevc")) {
                                                                                                                break loop0;
                                                                                                            }
                                                                                                            int i83 = bufferInfo.size;
                                                                                                            byte[] bArr = new byte[i83];
                                                                                                            outputBuffer2.limit(bufferInfo.offset + i83);
                                                                                                            outputBuffer2.position(bufferInfo.offset);
                                                                                                            outputBuffer2.get(bArr);
                                                                                                            byte b = 1;
                                                                                                            int i84 = bufferInfo.size - 1;
                                                                                                            while (i84 >= 0 && i84 > 3) {
                                                                                                                if (bArr[i84] == b && bArr[i84 - 1] == 0 && bArr[i84 - 2] == 0) {
                                                                                                                    int i85 = i84 - 3;
                                                                                                                    if (bArr[i85] == 0) {
                                                                                                                        byteBuffer5 = ByteBuffer.allocate(i85);
                                                                                                                        byteBuffer6 = ByteBuffer.allocate(bufferInfo.size - i85);
                                                                                                                        byteBuffer5.put(bArr, 0, i85).position(0);
                                                                                                                        byteBuffer6.put(bArr, i85, bufferInfo.size - i85).position(0);
                                                                                                                        break;
                                                                                                                    }
                                                                                                                }
                                                                                                                i84--;
                                                                                                                b = 1;
                                                                                                            }
                                                                                                            byteBuffer5 = null;
                                                                                                            byteBuffer6 = null;
                                                                                                            MediaFormat createVideoFormat3 = MediaFormat.createVideoFormat(this.outputMimeType, i61, i60);
                                                                                                            if (byteBuffer5 != null && byteBuffer6 != null) {
                                                                                                                createVideoFormat3.setByteBuffer(str13, byteBuffer5);
                                                                                                                createVideoFormat3.setByteBuffer("csd-1", byteBuffer6);
                                                                                                            }
                                                                                                            i3 = this.mediaMuxer.addTrack(createVideoFormat3, false);
                                                                                                        }
                                                                                                    } else {
                                                                                                        if (i79 == 0 || (i82 & 1) == 0) {
                                                                                                            inputSurface6 = inputSurface5;
                                                                                                        } else {
                                                                                                            inputSurface6 = inputSurface5;
                                                                                                            try {
                                                                                                                bufferInfo.offset += i79;
                                                                                                                bufferInfo.size = i81 - i79;
                                                                                                            } catch (Exception e9) {
                                                                                                                e = e9;
                                                                                                                i75 = i60;
                                                                                                                i74 = i61;
                                                                                                                i10 = i3;
                                                                                                                inputSurface5 = inputSurface6;
                                                                                                                exc2 = e;
                                                                                                                if (exc2 instanceof IllegalStateException) {
                                                                                                                }
                                                                                                                StringBuilder sb2222222 = new StringBuilder();
                                                                                                                sb2222222.append("bitrate: ");
                                                                                                                i77 = i57;
                                                                                                                sb2222222.append(i77);
                                                                                                                sb2222222.append(" framerate: ");
                                                                                                                i59 = i58;
                                                                                                                sb2222222.append(i59);
                                                                                                                sb2222222.append(" size: ");
                                                                                                                sb2222222.append(i75);
                                                                                                                str = "x";
                                                                                                                sb2222222.append(str);
                                                                                                                sb2222222.append(i74);
                                                                                                                FileLog.e(sb2222222.toString());
                                                                                                                FileLog.e(exc2);
                                                                                                                z6 = z32;
                                                                                                                i60 = i75;
                                                                                                                z7 = true;
                                                                                                                outputSurface3 = outputSurface11;
                                                                                                                if (outputSurface3 != null) {
                                                                                                                }
                                                                                                                if (inputSurface5 != null) {
                                                                                                                }
                                                                                                                if (mediaCodec2 != null) {
                                                                                                                }
                                                                                                                if (audioRecoder3 != null) {
                                                                                                                }
                                                                                                                checkConversionCanceled();
                                                                                                                mediaCodec4 = mediaCodec2;
                                                                                                                inputSurface2 = inputSurface5;
                                                                                                                i4 = i60;
                                                                                                                z3 = z39;
                                                                                                                str2 = str12;
                                                                                                                z4 = z31;
                                                                                                                file = file2;
                                                                                                                mediaCodec5 = null;
                                                                                                                z2 = z;
                                                                                                                mediaExtractor2 = this.extractor;
                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                }
                                                                                                                mP4Builder2 = this.mediaMuxer;
                                                                                                                if (mP4Builder2 != null) {
                                                                                                                }
                                                                                                                if (mediaCodec4 != null) {
                                                                                                                }
                                                                                                                if (mediaCodec5 != null) {
                                                                                                                }
                                                                                                                if (outputSurface3 != null) {
                                                                                                                }
                                                                                                                if (inputSurface2 != null) {
                                                                                                                }
                                                                                                                str3 = str2;
                                                                                                                i75 = i4;
                                                                                                                if (z6) {
                                                                                                                }
                                                                                                            } catch (Throwable th16) {
                                                                                                                th = th16;
                                                                                                                i75 = i60;
                                                                                                                i74 = i61;
                                                                                                                outputSurface = outputSurface11;
                                                                                                                i2 = i58;
                                                                                                                inputSurface = inputSurface6;
                                                                                                                z3 = z39;
                                                                                                                str2 = str12;
                                                                                                                z4 = z31;
                                                                                                                i77 = i57;
                                                                                                                str = "x";
                                                                                                                file = file2;
                                                                                                                mediaCodec = null;
                                                                                                                z5 = false;
                                                                                                                z2 = z;
                                                                                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                                FileLog.e(th);
                                                                                                                mediaExtractor = this.extractor;
                                                                                                                if (mediaExtractor != null) {
                                                                                                                }
                                                                                                                mP4Builder = this.mediaMuxer;
                                                                                                                if (mP4Builder != null) {
                                                                                                                }
                                                                                                                if (mediaCodec2 != null) {
                                                                                                                }
                                                                                                                if (mediaCodec != null) {
                                                                                                                }
                                                                                                                if (outputSurface != null) {
                                                                                                                }
                                                                                                                if (inputSurface != null) {
                                                                                                                }
                                                                                                                str3 = str2;
                                                                                                                z6 = z5;
                                                                                                                z7 = true;
                                                                                                                if (z6) {
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                        if (z45 && (i82 & 1) != 0) {
                                                                                                            cutOfNalData(this.outputMimeType, outputBuffer2, bufferInfo);
                                                                                                            z45 = false;
                                                                                                        }
                                                                                                        try {
                                                                                                            int i86 = i65;
                                                                                                            long writeSampleData = this.mediaMuxer.writeSampleData(i3, outputBuffer2, bufferInfo, true);
                                                                                                            if (writeSampleData == 0 || (videoConvertorListener3 = this.callback) == null) {
                                                                                                                outputSurface13 = outputSurface11;
                                                                                                                i69 = dequeueOutputBuffer2;
                                                                                                                i67 = i86;
                                                                                                            } else {
                                                                                                                i69 = dequeueOutputBuffer2;
                                                                                                                i67 = i86;
                                                                                                                long j33 = bufferInfo.presentationTimeUs;
                                                                                                                if (j33 <= j32) {
                                                                                                                    j33 = j32;
                                                                                                                }
                                                                                                                outputSurface13 = outputSurface11;
                                                                                                                try {
                                                                                                                    videoConvertorListener3.didWriteData(writeSampleData, ((((float) j33) / 1000.0f) / 1000.0f) / f3);
                                                                                                                    j32 = j33;
                                                                                                                } catch (Exception e10) {
                                                                                                                    e = e10;
                                                                                                                    i75 = i60;
                                                                                                                    i74 = i61;
                                                                                                                    i10 = i3;
                                                                                                                    inputSurface5 = inputSurface6;
                                                                                                                    outputSurface11 = outputSurface13;
                                                                                                                    exc2 = e;
                                                                                                                    if (exc2 instanceof IllegalStateException) {
                                                                                                                    }
                                                                                                                    StringBuilder sb22222222 = new StringBuilder();
                                                                                                                    sb22222222.append("bitrate: ");
                                                                                                                    i77 = i57;
                                                                                                                    sb22222222.append(i77);
                                                                                                                    sb22222222.append(" framerate: ");
                                                                                                                    i59 = i58;
                                                                                                                    sb22222222.append(i59);
                                                                                                                    sb22222222.append(" size: ");
                                                                                                                    sb22222222.append(i75);
                                                                                                                    str = "x";
                                                                                                                    sb22222222.append(str);
                                                                                                                    sb22222222.append(i74);
                                                                                                                    FileLog.e(sb22222222.toString());
                                                                                                                    FileLog.e(exc2);
                                                                                                                    z6 = z32;
                                                                                                                    i60 = i75;
                                                                                                                    z7 = true;
                                                                                                                    outputSurface3 = outputSurface11;
                                                                                                                    if (outputSurface3 != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface5 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec2 != null) {
                                                                                                                    }
                                                                                                                    if (audioRecoder3 != null) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    mediaCodec4 = mediaCodec2;
                                                                                                                    inputSurface2 = inputSurface5;
                                                                                                                    i4 = i60;
                                                                                                                    z3 = z39;
                                                                                                                    str2 = str12;
                                                                                                                    z4 = z31;
                                                                                                                    file = file2;
                                                                                                                    mediaCodec5 = null;
                                                                                                                    z2 = z;
                                                                                                                    mediaExtractor2 = this.extractor;
                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                    }
                                                                                                                    mP4Builder2 = this.mediaMuxer;
                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec5 != null) {
                                                                                                                    }
                                                                                                                    if (outputSurface3 != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface2 != null) {
                                                                                                                    }
                                                                                                                    str3 = str2;
                                                                                                                    i75 = i4;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                } catch (Throwable th17) {
                                                                                                                    th = th17;
                                                                                                                    th = th;
                                                                                                                    i75 = i60;
                                                                                                                    i74 = i61;
                                                                                                                    i2 = i58;
                                                                                                                    inputSurface = inputSurface6;
                                                                                                                    outputSurface = outputSurface13;
                                                                                                                    z3 = z39;
                                                                                                                    str2 = str12;
                                                                                                                    z4 = z31;
                                                                                                                    i77 = i57;
                                                                                                                    str = "x";
                                                                                                                    file = file2;
                                                                                                                    mediaCodec = null;
                                                                                                                    z5 = false;
                                                                                                                    z2 = z;
                                                                                                                    FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                                    FileLog.e(th);
                                                                                                                    mediaExtractor = this.extractor;
                                                                                                                    if (mediaExtractor != null) {
                                                                                                                    }
                                                                                                                    mP4Builder = this.mediaMuxer;
                                                                                                                    if (mP4Builder != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec2 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec != null) {
                                                                                                                    }
                                                                                                                    if (outputSurface != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface != null) {
                                                                                                                    }
                                                                                                                    str3 = str2;
                                                                                                                    z6 = z5;
                                                                                                                    z7 = true;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        } catch (Exception e11) {
                                                                                                            e = e11;
                                                                                                            i75 = i60;
                                                                                                            i74 = i61;
                                                                                                            i10 = i3;
                                                                                                            inputSurface5 = inputSurface6;
                                                                                                            exc2 = e;
                                                                                                            if (exc2 instanceof IllegalStateException) {
                                                                                                            }
                                                                                                            StringBuilder sb222222222 = new StringBuilder();
                                                                                                            sb222222222.append("bitrate: ");
                                                                                                            i77 = i57;
                                                                                                            sb222222222.append(i77);
                                                                                                            sb222222222.append(" framerate: ");
                                                                                                            i59 = i58;
                                                                                                            sb222222222.append(i59);
                                                                                                            sb222222222.append(" size: ");
                                                                                                            sb222222222.append(i75);
                                                                                                            str = "x";
                                                                                                            sb222222222.append(str);
                                                                                                            sb222222222.append(i74);
                                                                                                            FileLog.e(sb222222222.toString());
                                                                                                            FileLog.e(exc2);
                                                                                                            z6 = z32;
                                                                                                            i60 = i75;
                                                                                                            z7 = true;
                                                                                                            outputSurface3 = outputSurface11;
                                                                                                            if (outputSurface3 != null) {
                                                                                                            }
                                                                                                            if (inputSurface5 != null) {
                                                                                                            }
                                                                                                            if (mediaCodec2 != null) {
                                                                                                            }
                                                                                                            if (audioRecoder3 != null) {
                                                                                                            }
                                                                                                            checkConversionCanceled();
                                                                                                            mediaCodec4 = mediaCodec2;
                                                                                                            inputSurface2 = inputSurface5;
                                                                                                            i4 = i60;
                                                                                                            z3 = z39;
                                                                                                            str2 = str12;
                                                                                                            z4 = z31;
                                                                                                            file = file2;
                                                                                                            mediaCodec5 = null;
                                                                                                            z2 = z;
                                                                                                            mediaExtractor2 = this.extractor;
                                                                                                            if (mediaExtractor2 != null) {
                                                                                                            }
                                                                                                            mP4Builder2 = this.mediaMuxer;
                                                                                                            if (mP4Builder2 != null) {
                                                                                                            }
                                                                                                            if (mediaCodec4 != null) {
                                                                                                            }
                                                                                                            if (mediaCodec5 != null) {
                                                                                                            }
                                                                                                            if (outputSurface3 != null) {
                                                                                                            }
                                                                                                            if (inputSurface2 != null) {
                                                                                                            }
                                                                                                            str3 = str2;
                                                                                                            i75 = i4;
                                                                                                            if (z6) {
                                                                                                            }
                                                                                                        } catch (Throwable th18) {
                                                                                                            th = th18;
                                                                                                            outputSurface13 = outputSurface11;
                                                                                                            th = th;
                                                                                                            i75 = i60;
                                                                                                            i74 = i61;
                                                                                                            i2 = i58;
                                                                                                            inputSurface = inputSurface6;
                                                                                                            outputSurface = outputSurface13;
                                                                                                            z3 = z39;
                                                                                                            str2 = str12;
                                                                                                            z4 = z31;
                                                                                                            i77 = i57;
                                                                                                            str = "x";
                                                                                                            file = file2;
                                                                                                            mediaCodec = null;
                                                                                                            z5 = false;
                                                                                                            z2 = z;
                                                                                                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                            FileLog.e(th);
                                                                                                            mediaExtractor = this.extractor;
                                                                                                            if (mediaExtractor != null) {
                                                                                                            }
                                                                                                            mP4Builder = this.mediaMuxer;
                                                                                                            if (mP4Builder != null) {
                                                                                                            }
                                                                                                            if (mediaCodec2 != null) {
                                                                                                            }
                                                                                                            if (mediaCodec != null) {
                                                                                                            }
                                                                                                            if (outputSurface != null) {
                                                                                                            }
                                                                                                            if (inputSurface != null) {
                                                                                                            }
                                                                                                            str3 = str2;
                                                                                                            z6 = z5;
                                                                                                            z7 = true;
                                                                                                            if (z6) {
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                    if (i69 != i68) {
                                                                                                        str4 = str13;
                                                                                                        audioRecoder4 = audioRecoder3;
                                                                                                        i62 = i58;
                                                                                                        i63 = i66;
                                                                                                        i65 = i67;
                                                                                                        inputSurface5 = inputSurface6;
                                                                                                        outputSurface11 = outputSurface13;
                                                                                                    } else {
                                                                                                        if (z35) {
                                                                                                            inputSurface7 = inputSurface6;
                                                                                                            outputSurface11 = outputSurface13;
                                                                                                            i65 = i67;
                                                                                                        } else {
                                                                                                            int i87 = i67;
                                                                                                            long j34 = (i87 / 30.0f) * 1000.0f * 1000.0f * 1000.0f;
                                                                                                            outputSurface11 = outputSurface13;
                                                                                                            try {
                                                                                                                outputSurface11.drawImage(j34);
                                                                                                                inputSurface7 = inputSurface6;
                                                                                                                try {
                                                                                                                    inputSurface7.setPresentationTime(j34);
                                                                                                                    inputSurface7.swapBuffers();
                                                                                                                    i65 = i87 + 1;
                                                                                                                    if (i65 >= 30.0f * f3) {
                                                                                                                        mediaCodec2.signalEndOfInputStream();
                                                                                                                        z35 = true;
                                                                                                                        z36 = false;
                                                                                                                    }
                                                                                                                } catch (Exception e12) {
                                                                                                                    e = e12;
                                                                                                                    i74 = i61;
                                                                                                                    i10 = i3;
                                                                                                                    inputSurface5 = inputSurface7;
                                                                                                                    i75 = i60;
                                                                                                                    exc2 = e;
                                                                                                                    if (exc2 instanceof IllegalStateException) {
                                                                                                                    }
                                                                                                                    StringBuilder sb2222222222 = new StringBuilder();
                                                                                                                    sb2222222222.append("bitrate: ");
                                                                                                                    i77 = i57;
                                                                                                                    sb2222222222.append(i77);
                                                                                                                    sb2222222222.append(" framerate: ");
                                                                                                                    i59 = i58;
                                                                                                                    sb2222222222.append(i59);
                                                                                                                    sb2222222222.append(" size: ");
                                                                                                                    sb2222222222.append(i75);
                                                                                                                    str = "x";
                                                                                                                    sb2222222222.append(str);
                                                                                                                    sb2222222222.append(i74);
                                                                                                                    FileLog.e(sb2222222222.toString());
                                                                                                                    FileLog.e(exc2);
                                                                                                                    z6 = z32;
                                                                                                                    i60 = i75;
                                                                                                                    z7 = true;
                                                                                                                    outputSurface3 = outputSurface11;
                                                                                                                    if (outputSurface3 != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface5 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec2 != null) {
                                                                                                                    }
                                                                                                                    if (audioRecoder3 != null) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    mediaCodec4 = mediaCodec2;
                                                                                                                    inputSurface2 = inputSurface5;
                                                                                                                    i4 = i60;
                                                                                                                    z3 = z39;
                                                                                                                    str2 = str12;
                                                                                                                    z4 = z31;
                                                                                                                    file = file2;
                                                                                                                    mediaCodec5 = null;
                                                                                                                    z2 = z;
                                                                                                                    mediaExtractor2 = this.extractor;
                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                    }
                                                                                                                    mP4Builder2 = this.mediaMuxer;
                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec5 != null) {
                                                                                                                    }
                                                                                                                    if (outputSurface3 != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface2 != null) {
                                                                                                                    }
                                                                                                                    str3 = str2;
                                                                                                                    i75 = i4;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                } catch (Throwable th19) {
                                                                                                                    th = th19;
                                                                                                                    th = th;
                                                                                                                    i74 = i61;
                                                                                                                    outputSurface = outputSurface11;
                                                                                                                    inputSurface = inputSurface7;
                                                                                                                    i2 = i58;
                                                                                                                    z3 = z39;
                                                                                                                    str2 = str12;
                                                                                                                    z4 = z31;
                                                                                                                    i77 = i57;
                                                                                                                    str = "x";
                                                                                                                    file = file2;
                                                                                                                    mediaCodec = null;
                                                                                                                    z5 = false;
                                                                                                                    i75 = i60;
                                                                                                                    z2 = z;
                                                                                                                    FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                                    FileLog.e(th);
                                                                                                                    mediaExtractor = this.extractor;
                                                                                                                    if (mediaExtractor != null) {
                                                                                                                    }
                                                                                                                    mP4Builder = this.mediaMuxer;
                                                                                                                    if (mP4Builder != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec2 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec != null) {
                                                                                                                    }
                                                                                                                    if (outputSurface != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface != null) {
                                                                                                                    }
                                                                                                                    str3 = str2;
                                                                                                                    z6 = z5;
                                                                                                                    z7 = true;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                }
                                                                                                            } catch (Exception e13) {
                                                                                                                e = e13;
                                                                                                                inputSurface7 = inputSurface6;
                                                                                                            } catch (Throwable th20) {
                                                                                                                th = th20;
                                                                                                                inputSurface7 = inputSurface6;
                                                                                                            }
                                                                                                        }
                                                                                                        str4 = str13;
                                                                                                        inputSurface5 = inputSurface7;
                                                                                                        audioRecoder4 = audioRecoder3;
                                                                                                        i62 = i58;
                                                                                                        i63 = i66;
                                                                                                    }
                                                                                                } catch (Exception e14) {
                                                                                                    e = e14;
                                                                                                    i75 = i60;
                                                                                                    i74 = i61;
                                                                                                    i10 = i3;
                                                                                                    exc2 = e;
                                                                                                    if (exc2 instanceof IllegalStateException) {
                                                                                                    }
                                                                                                    StringBuilder sb22222222222 = new StringBuilder();
                                                                                                    sb22222222222.append("bitrate: ");
                                                                                                    i77 = i57;
                                                                                                    sb22222222222.append(i77);
                                                                                                    sb22222222222.append(" framerate: ");
                                                                                                    i59 = i58;
                                                                                                    sb22222222222.append(i59);
                                                                                                    sb22222222222.append(" size: ");
                                                                                                    sb22222222222.append(i75);
                                                                                                    str = "x";
                                                                                                    sb22222222222.append(str);
                                                                                                    sb22222222222.append(i74);
                                                                                                    FileLog.e(sb22222222222.toString());
                                                                                                    FileLog.e(exc2);
                                                                                                    z6 = z32;
                                                                                                    i60 = i75;
                                                                                                    z7 = true;
                                                                                                    outputSurface3 = outputSurface11;
                                                                                                    if (outputSurface3 != null) {
                                                                                                    }
                                                                                                    if (inputSurface5 != null) {
                                                                                                    }
                                                                                                    if (mediaCodec2 != null) {
                                                                                                    }
                                                                                                    if (audioRecoder3 != null) {
                                                                                                    }
                                                                                                    checkConversionCanceled();
                                                                                                    mediaCodec4 = mediaCodec2;
                                                                                                    inputSurface2 = inputSurface5;
                                                                                                    i4 = i60;
                                                                                                    z3 = z39;
                                                                                                    str2 = str12;
                                                                                                    z4 = z31;
                                                                                                    file = file2;
                                                                                                    mediaCodec5 = null;
                                                                                                    z2 = z;
                                                                                                    mediaExtractor2 = this.extractor;
                                                                                                    if (mediaExtractor2 != null) {
                                                                                                    }
                                                                                                    mP4Builder2 = this.mediaMuxer;
                                                                                                    if (mP4Builder2 != null) {
                                                                                                    }
                                                                                                    if (mediaCodec4 != null) {
                                                                                                    }
                                                                                                    if (mediaCodec5 != null) {
                                                                                                    }
                                                                                                    if (outputSurface3 != null) {
                                                                                                    }
                                                                                                    if (inputSurface2 != null) {
                                                                                                    }
                                                                                                    str3 = str2;
                                                                                                    i75 = i4;
                                                                                                    if (z6) {
                                                                                                    }
                                                                                                } catch (Throwable th21) {
                                                                                                    th = th21;
                                                                                                    inputSurface6 = inputSurface5;
                                                                                                }
                                                                                            } else {
                                                                                                inputSurface6 = inputSurface5;
                                                                                                i67 = i65;
                                                                                                outputSurface13 = outputSurface11;
                                                                                                i69 = dequeueOutputBuffer2;
                                                                                            }
                                                                                            boolean z48 = (bufferInfo.flags & 4) != 0;
                                                                                            mediaCodec2.releaseOutputBuffer(i69, false);
                                                                                            z34 = z48;
                                                                                            i68 = -1;
                                                                                            if (i69 != i68) {
                                                                                            }
                                                                                        } catch (Exception e15) {
                                                                                            e = e15;
                                                                                            inputSurface7 = inputSurface6;
                                                                                            outputSurface11 = outputSurface13;
                                                                                            i74 = i61;
                                                                                            i10 = i3;
                                                                                            inputSurface5 = inputSurface7;
                                                                                            i75 = i60;
                                                                                            exc2 = e;
                                                                                            if (exc2 instanceof IllegalStateException) {
                                                                                            }
                                                                                            StringBuilder sb222222222222 = new StringBuilder();
                                                                                            sb222222222222.append("bitrate: ");
                                                                                            i77 = i57;
                                                                                            sb222222222222.append(i77);
                                                                                            sb222222222222.append(" framerate: ");
                                                                                            i59 = i58;
                                                                                            sb222222222222.append(i59);
                                                                                            sb222222222222.append(" size: ");
                                                                                            sb222222222222.append(i75);
                                                                                            str = "x";
                                                                                            sb222222222222.append(str);
                                                                                            sb222222222222.append(i74);
                                                                                            FileLog.e(sb222222222222.toString());
                                                                                            FileLog.e(exc2);
                                                                                            z6 = z32;
                                                                                            i60 = i75;
                                                                                            z7 = true;
                                                                                            outputSurface3 = outputSurface11;
                                                                                            if (outputSurface3 != null) {
                                                                                            }
                                                                                            if (inputSurface5 != null) {
                                                                                            }
                                                                                            if (mediaCodec2 != null) {
                                                                                            }
                                                                                            if (audioRecoder3 != null) {
                                                                                            }
                                                                                            checkConversionCanceled();
                                                                                            mediaCodec4 = mediaCodec2;
                                                                                            inputSurface2 = inputSurface5;
                                                                                            i4 = i60;
                                                                                            z3 = z39;
                                                                                            str2 = str12;
                                                                                            z4 = z31;
                                                                                            file = file2;
                                                                                            mediaCodec5 = null;
                                                                                            z2 = z;
                                                                                            mediaExtractor2 = this.extractor;
                                                                                            if (mediaExtractor2 != null) {
                                                                                            }
                                                                                            mP4Builder2 = this.mediaMuxer;
                                                                                            if (mP4Builder2 != null) {
                                                                                            }
                                                                                            if (mediaCodec4 != null) {
                                                                                            }
                                                                                            if (mediaCodec5 != null) {
                                                                                            }
                                                                                            if (outputSurface3 != null) {
                                                                                            }
                                                                                            if (inputSurface2 != null) {
                                                                                            }
                                                                                            str3 = str2;
                                                                                            i75 = i4;
                                                                                            if (z6) {
                                                                                            }
                                                                                        } catch (Throwable th22) {
                                                                                            th = th22;
                                                                                            inputSurface7 = inputSurface6;
                                                                                            outputSurface11 = outputSurface13;
                                                                                            th = th;
                                                                                            i74 = i61;
                                                                                            outputSurface = outputSurface11;
                                                                                            inputSurface = inputSurface7;
                                                                                            i2 = i58;
                                                                                            z3 = z39;
                                                                                            str2 = str12;
                                                                                            z4 = z31;
                                                                                            i77 = i57;
                                                                                            str = "x";
                                                                                            file = file2;
                                                                                            mediaCodec = null;
                                                                                            z5 = false;
                                                                                            i75 = i60;
                                                                                            z2 = z;
                                                                                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                            FileLog.e(th);
                                                                                            mediaExtractor = this.extractor;
                                                                                            if (mediaExtractor != null) {
                                                                                            }
                                                                                            mP4Builder = this.mediaMuxer;
                                                                                            if (mP4Builder != null) {
                                                                                            }
                                                                                            if (mediaCodec2 != null) {
                                                                                            }
                                                                                            if (mediaCodec != null) {
                                                                                            }
                                                                                            if (outputSurface != null) {
                                                                                            }
                                                                                            if (inputSurface != null) {
                                                                                            }
                                                                                            str3 = str2;
                                                                                            z6 = z5;
                                                                                            z7 = true;
                                                                                            if (z6) {
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                i68 = -1;
                                                                            }
                                                                            i58 = i62;
                                                                            i69 = dequeueOutputBuffer2;
                                                                            if (i69 != i68) {
                                                                            }
                                                                        } catch (Exception e16) {
                                                                            e = e16;
                                                                            i58 = i62;
                                                                            i74 = i61;
                                                                            i10 = i3;
                                                                            i75 = i60;
                                                                            exc2 = e;
                                                                            if (exc2 instanceof IllegalStateException) {
                                                                            }
                                                                            StringBuilder sb2222222222222 = new StringBuilder();
                                                                            sb2222222222222.append("bitrate: ");
                                                                            i77 = i57;
                                                                            sb2222222222222.append(i77);
                                                                            sb2222222222222.append(" framerate: ");
                                                                            i59 = i58;
                                                                            sb2222222222222.append(i59);
                                                                            sb2222222222222.append(" size: ");
                                                                            sb2222222222222.append(i75);
                                                                            str = "x";
                                                                            sb2222222222222.append(str);
                                                                            sb2222222222222.append(i74);
                                                                            FileLog.e(sb2222222222222.toString());
                                                                            FileLog.e(exc2);
                                                                            z6 = z32;
                                                                            i60 = i75;
                                                                            z7 = true;
                                                                            outputSurface3 = outputSurface11;
                                                                            if (outputSurface3 != null) {
                                                                            }
                                                                            if (inputSurface5 != null) {
                                                                            }
                                                                            if (mediaCodec2 != null) {
                                                                            }
                                                                            if (audioRecoder3 != null) {
                                                                            }
                                                                            checkConversionCanceled();
                                                                            mediaCodec4 = mediaCodec2;
                                                                            inputSurface2 = inputSurface5;
                                                                            i4 = i60;
                                                                            z3 = z39;
                                                                            str2 = str12;
                                                                            z4 = z31;
                                                                            file = file2;
                                                                            mediaCodec5 = null;
                                                                            z2 = z;
                                                                            mediaExtractor2 = this.extractor;
                                                                            if (mediaExtractor2 != null) {
                                                                            }
                                                                            mP4Builder2 = this.mediaMuxer;
                                                                            if (mP4Builder2 != null) {
                                                                            }
                                                                            if (mediaCodec4 != null) {
                                                                            }
                                                                            if (mediaCodec5 != null) {
                                                                            }
                                                                            if (outputSurface3 != null) {
                                                                            }
                                                                            if (inputSurface2 != null) {
                                                                            }
                                                                            str3 = str2;
                                                                            i75 = i4;
                                                                            if (z6) {
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } catch (Throwable th23) {
                                                                InputSurface inputSurface8 = inputSurface5;
                                                                th = th23;
                                                                i2 = i62;
                                                                i74 = i61;
                                                                outputSurface = outputSurface11;
                                                                inputSurface = inputSurface8;
                                                            }
                                                        } catch (Exception e17) {
                                                            e = e17;
                                                            i58 = i62;
                                                            audioRecoder3 = audioRecoder4;
                                                        }
                                                        i64 = i65;
                                                        z33 = z47;
                                                    }
                                                }
                                            } catch (Exception e18) {
                                                i75 = i60;
                                                i58 = i76;
                                                i10 = -5;
                                                outputSurface11 = null;
                                                audioRecoder3 = null;
                                                exc2 = e18;
                                                str12 = name2;
                                                i74 = i61;
                                                if (exc2 instanceof IllegalStateException) {
                                                }
                                                StringBuilder sb22222222222222 = new StringBuilder();
                                                sb22222222222222.append("bitrate: ");
                                                i77 = i57;
                                                sb22222222222222.append(i77);
                                                sb22222222222222.append(" framerate: ");
                                                i59 = i58;
                                                sb22222222222222.append(i59);
                                                sb22222222222222.append(" size: ");
                                                sb22222222222222.append(i75);
                                                str = "x";
                                                sb22222222222222.append(str);
                                                sb22222222222222.append(i74);
                                                FileLog.e(sb22222222222222.toString());
                                                FileLog.e(exc2);
                                                z6 = z32;
                                                i60 = i75;
                                                z7 = true;
                                                outputSurface3 = outputSurface11;
                                                if (outputSurface3 != null) {
                                                }
                                                if (inputSurface5 != null) {
                                                }
                                                if (mediaCodec2 != null) {
                                                }
                                                if (audioRecoder3 != null) {
                                                }
                                                checkConversionCanceled();
                                                mediaCodec4 = mediaCodec2;
                                                inputSurface2 = inputSurface5;
                                                i4 = i60;
                                                z3 = z39;
                                                str2 = str12;
                                                z4 = z31;
                                                file = file2;
                                                mediaCodec5 = null;
                                                z2 = z;
                                                mediaExtractor2 = this.extractor;
                                                if (mediaExtractor2 != null) {
                                                }
                                                mP4Builder2 = this.mediaMuxer;
                                                if (mP4Builder2 != null) {
                                                }
                                                if (mediaCodec4 != null) {
                                                }
                                                if (mediaCodec5 != null) {
                                                }
                                                if (outputSurface3 != null) {
                                                }
                                                if (inputSurface2 != null) {
                                                }
                                                str3 = str2;
                                                i75 = i4;
                                                if (z6) {
                                                }
                                            } catch (Throwable th24) {
                                                th = th24;
                                                inputSurface = inputSurface5;
                                                i75 = i60;
                                                str2 = name2;
                                                z3 = z39;
                                                i2 = i76;
                                                z4 = z31;
                                                i77 = i57;
                                                str = "x";
                                                file = file2;
                                                i3 = -5;
                                                mediaCodec = null;
                                                z5 = false;
                                                outputSurface = null;
                                                z2 = z;
                                                i74 = i61;
                                            }
                                        }
                                        i62 = i76;
                                        cropState2 = null;
                                        outputSurface11 = outputSurface12;
                                        str12 = name2;
                                        outputSurface12 = new OutputSurface(savedFilterState, str14, str15, str16, arrayList, cropState2, i61, i60, i72, i73, i71, i62, true, num, num2, null, convertVideoParams);
                                        if (Build.VERSION.SDK_INT < 21) {
                                        }
                                        checkConversionCanceled();
                                        ByteBuffer[] byteBufferArr32 = outputBuffers2;
                                        this.mediaMuxer = new MP4Builder().createMovie(mp4Movie, z38, this.outputMimeType.equals("video/hevc"));
                                        if (convertVideoParams.soundInfos.isEmpty()) {
                                        }
                                        z34 = false;
                                        long j322 = 0;
                                        z35 = false;
                                        int i792 = 0;
                                        i64 = 0;
                                        boolean z452 = true;
                                        loop0: while (true) {
                                            if (!z34) {
                                            }
                                            checkConversionCanceled();
                                            if (audioRecoder4 != null) {
                                            }
                                            z36 = !z35;
                                            boolean z462 = true;
                                            int i802 = i64;
                                            boolean z472 = z33;
                                            i65 = i802;
                                            while (true) {
                                                if (!z36) {
                                                }
                                                checkConversionCanceled();
                                                if (z) {
                                                }
                                                dequeueOutputBuffer2 = mediaCodec2.dequeueOutputBuffer(bufferInfo, j28);
                                                if (dequeueOutputBuffer2 == -1) {
                                                }
                                                i58 = i62;
                                                i69 = dequeueOutputBuffer2;
                                                if (i69 != i68) {
                                                }
                                            }
                                            i64 = i65;
                                            z33 = z472;
                                        }
                                    }
                                } catch (Exception e19) {
                                    exc2 = e19;
                                    i57 = i77;
                                    i58 = i76;
                                    mediaCodec2 = null;
                                    inputSurface5 = null;
                                    i10 = -5;
                                    outputSurface11 = null;
                                    audioRecoder3 = null;
                                    str12 = null;
                                    z31 = false;
                                    if (exc2 instanceof IllegalStateException) {
                                    }
                                    StringBuilder sb222222222222222 = new StringBuilder();
                                    sb222222222222222.append("bitrate: ");
                                    i77 = i57;
                                    sb222222222222222.append(i77);
                                    sb222222222222222.append(" framerate: ");
                                    i59 = i58;
                                    sb222222222222222.append(i59);
                                    sb222222222222222.append(" size: ");
                                    sb222222222222222.append(i75);
                                    str = "x";
                                    sb222222222222222.append(str);
                                    sb222222222222222.append(i74);
                                    FileLog.e(sb222222222222222.toString());
                                    FileLog.e(exc2);
                                    z6 = z32;
                                    i60 = i75;
                                    z7 = true;
                                    outputSurface3 = outputSurface11;
                                    if (outputSurface3 != null) {
                                    }
                                    if (inputSurface5 != null) {
                                    }
                                    if (mediaCodec2 != null) {
                                    }
                                    if (audioRecoder3 != null) {
                                    }
                                    checkConversionCanceled();
                                    mediaCodec4 = mediaCodec2;
                                    inputSurface2 = inputSurface5;
                                    i4 = i60;
                                    z3 = z39;
                                    str2 = str12;
                                    z4 = z31;
                                    file = file2;
                                    mediaCodec5 = null;
                                    z2 = z;
                                    mediaExtractor2 = this.extractor;
                                    if (mediaExtractor2 != null) {
                                    }
                                    mP4Builder2 = this.mediaMuxer;
                                    if (mP4Builder2 != null) {
                                    }
                                    if (mediaCodec4 != null) {
                                    }
                                    if (mediaCodec5 != null) {
                                    }
                                    if (outputSurface3 != null) {
                                    }
                                    if (inputSurface2 != null) {
                                    }
                                    str3 = str2;
                                    i75 = i4;
                                    if (z6) {
                                    }
                                } catch (Throwable th25) {
                                    th = th25;
                                    z2 = z;
                                    th = th;
                                    z3 = z39;
                                    i2 = i76;
                                    str = "x";
                                    file = file2;
                                    mediaCodec2 = null;
                                    inputSurface = null;
                                    i3 = -5;
                                    str2 = null;
                                    mediaCodec = null;
                                    z4 = false;
                                    z5 = false;
                                    outputSurface = null;
                                    FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                    FileLog.e(th);
                                    mediaExtractor = this.extractor;
                                    if (mediaExtractor != null) {
                                    }
                                    mP4Builder = this.mediaMuxer;
                                    if (mP4Builder != null) {
                                    }
                                    if (mediaCodec2 != null) {
                                    }
                                    if (mediaCodec != null) {
                                    }
                                    if (outputSurface != null) {
                                    }
                                    if (inputSurface != null) {
                                    }
                                    str3 = str2;
                                    z6 = z5;
                                    z7 = true;
                                    if (z6) {
                                    }
                                }
                            } else {
                                cropState = cropState3;
                            }
                            if (Build.VERSION.SDK_INT < 21) {
                            }
                            checkConversionCanceled();
                            ByteBuffer[] byteBufferArr322 = outputBuffers2;
                            this.mediaMuxer = new MP4Builder().createMovie(mp4Movie, z38, this.outputMimeType.equals("video/hevc"));
                            if (convertVideoParams.soundInfos.isEmpty()) {
                            }
                            z34 = false;
                            long j3222 = 0;
                            z35 = false;
                            int i7922 = 0;
                            i64 = 0;
                            boolean z4522 = true;
                            loop0: while (true) {
                                if (!z34) {
                                }
                                checkConversionCanceled();
                                if (audioRecoder4 != null) {
                                }
                                z36 = !z35;
                                boolean z4622 = true;
                                int i8022 = i64;
                                boolean z4722 = z33;
                                i65 = i8022;
                                while (true) {
                                    if (!z36) {
                                    }
                                    checkConversionCanceled();
                                    if (z) {
                                    }
                                    dequeueOutputBuffer2 = mediaCodec2.dequeueOutputBuffer(bufferInfo, j28);
                                    if (dequeueOutputBuffer2 == -1) {
                                    }
                                    i58 = i62;
                                    i69 = dequeueOutputBuffer2;
                                    if (i69 != i68) {
                                    }
                                }
                                i64 = i65;
                                z33 = z4722;
                            }
                        } catch (Exception e20) {
                            e = e20;
                            i58 = i62;
                            i74 = i61;
                            i10 = -5;
                            audioRecoder3 = null;
                            i75 = i60;
                            exc2 = e;
                            if (exc2 instanceof IllegalStateException) {
                            }
                            StringBuilder sb2222222222222222 = new StringBuilder();
                            sb2222222222222222.append("bitrate: ");
                            i77 = i57;
                            sb2222222222222222.append(i77);
                            sb2222222222222222.append(" framerate: ");
                            i59 = i58;
                            sb2222222222222222.append(i59);
                            sb2222222222222222.append(" size: ");
                            sb2222222222222222.append(i75);
                            str = "x";
                            sb2222222222222222.append(str);
                            sb2222222222222222.append(i74);
                            FileLog.e(sb2222222222222222.toString());
                            FileLog.e(exc2);
                            z6 = z32;
                            i60 = i75;
                            z7 = true;
                            outputSurface3 = outputSurface11;
                            if (outputSurface3 != null) {
                            }
                            if (inputSurface5 != null) {
                            }
                            if (mediaCodec2 != null) {
                            }
                            if (audioRecoder3 != null) {
                            }
                            checkConversionCanceled();
                            mediaCodec4 = mediaCodec2;
                            inputSurface2 = inputSurface5;
                            i4 = i60;
                            z3 = z39;
                            str2 = str12;
                            z4 = z31;
                            file = file2;
                            mediaCodec5 = null;
                            z2 = z;
                            mediaExtractor2 = this.extractor;
                            if (mediaExtractor2 != null) {
                            }
                            mP4Builder2 = this.mediaMuxer;
                            if (mP4Builder2 != null) {
                            }
                            if (mediaCodec4 != null) {
                            }
                            if (mediaCodec5 != null) {
                            }
                            if (outputSurface3 != null) {
                            }
                            if (inputSurface2 != null) {
                            }
                            str3 = str2;
                            i75 = i4;
                            if (z6) {
                            }
                        } catch (Throwable th26) {
                            th = th26;
                            i2 = i62;
                            i74 = i61;
                            outputSurface = outputSurface11;
                            inputSurface = inputSurface5;
                            z3 = z39;
                            str2 = str12;
                            z4 = z31;
                            i77 = i57;
                            str = "x";
                            file = file2;
                            i3 = -5;
                        }
                        StringBuilder sb32 = new StringBuilder();
                        i57 = i77;
                        sb32.append("selected encoder ");
                        sb32.append(name2);
                        FileLog.d(sb32.toString());
                        mediaCodec2.configure(createVideoFormat2, (Surface) null, (MediaCrypto) null, 1);
                        inputSurface5 = new InputSurface(mediaCodec2.createInputSurface());
                        inputSurface5.makeCurrent();
                        mediaCodec2.start();
                        if (cropState != null) {
                        }
                        i62 = i76;
                        cropState2 = null;
                        outputSurface11 = outputSurface12;
                        str12 = name2;
                        outputSurface12 = new OutputSurface(savedFilterState, str14, str15, str16, arrayList, cropState2, i61, i60, i72, i73, i71, i62, true, num, num2, null, convertVideoParams);
                    } catch (Exception e21) {
                        e = e21;
                        i57 = i77;
                    } catch (Throwable th27) {
                        th = th27;
                        i75 = i60;
                        i74 = i61;
                        i2 = i76;
                        z3 = z39;
                        str2 = name2;
                        z4 = z31;
                    }
                    z31 = "c2.qti.avc.encoder".equalsIgnoreCase(name2);
                } catch (Exception e22) {
                    e = e22;
                    i57 = i77;
                    i58 = i76;
                    str12 = name2;
                    i75 = i60;
                    i74 = i61;
                    inputSurface5 = null;
                    i10 = -5;
                    outputSurface11 = null;
                    audioRecoder3 = null;
                    z31 = false;
                    exc2 = e;
                    if (exc2 instanceof IllegalStateException) {
                    }
                    StringBuilder sb22222222222222222 = new StringBuilder();
                    sb22222222222222222.append("bitrate: ");
                    i77 = i57;
                    sb22222222222222222.append(i77);
                    sb22222222222222222.append(" framerate: ");
                    i59 = i58;
                    sb22222222222222222.append(i59);
                    sb22222222222222222.append(" size: ");
                    sb22222222222222222.append(i75);
                    str = "x";
                    sb22222222222222222.append(str);
                    sb22222222222222222.append(i74);
                    FileLog.e(sb22222222222222222.toString());
                    FileLog.e(exc2);
                    z6 = z32;
                    i60 = i75;
                    z7 = true;
                    outputSurface3 = outputSurface11;
                    if (outputSurface3 != null) {
                    }
                    if (inputSurface5 != null) {
                    }
                    if (mediaCodec2 != null) {
                    }
                    if (audioRecoder3 != null) {
                    }
                    checkConversionCanceled();
                    mediaCodec4 = mediaCodec2;
                    inputSurface2 = inputSurface5;
                    i4 = i60;
                    z3 = z39;
                    str2 = str12;
                    z4 = z31;
                    file = file2;
                    mediaCodec5 = null;
                    z2 = z;
                    mediaExtractor2 = this.extractor;
                    if (mediaExtractor2 != null) {
                    }
                    mP4Builder2 = this.mediaMuxer;
                    if (mP4Builder2 != null) {
                    }
                    if (mediaCodec4 != null) {
                    }
                    if (mediaCodec5 != null) {
                    }
                    if (outputSurface3 != null) {
                    }
                    if (inputSurface2 != null) {
                    }
                    str3 = str2;
                    i75 = i4;
                    if (z6) {
                    }
                } catch (Throwable th28) {
                    th = th28;
                    i75 = i60;
                    i74 = i61;
                    i2 = i76;
                    z3 = z39;
                    str2 = name2;
                    str = "x";
                    file = file2;
                    inputSurface = null;
                    i3 = -5;
                    mediaCodec = null;
                    z4 = false;
                    z5 = false;
                    outputSurface = null;
                    z2 = z;
                    FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                    FileLog.e(th);
                    mediaExtractor = this.extractor;
                    if (mediaExtractor != null) {
                    }
                    mP4Builder = this.mediaMuxer;
                    if (mP4Builder != null) {
                    }
                    if (mediaCodec2 != null) {
                    }
                    if (mediaCodec != null) {
                    }
                    if (outputSurface != null) {
                    }
                    if (inputSurface != null) {
                    }
                    str3 = str2;
                    z6 = z5;
                    z7 = true;
                    if (z6) {
                    }
                }
                if (i74 % 16 != 0) {
                    if (BuildVars.LOGS_ENABLED) {
                        StringBuilder sb4 = new StringBuilder();
                        f3 = f;
                        sb4.append("changing width from ");
                        sb4.append(i74);
                        sb4.append(" to ");
                        sb4.append(Math.round(i74 / 16.0f) * 16);
                        FileLog.d(sb4.toString());
                    } else {
                        f3 = f;
                    }
                    i74 = Math.round(i74 / 16.0f) * 16;
                } else {
                    f3 = f;
                }
                if (i75 % 16 != 0) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("changing height from " + i75 + " to " + (Math.round(i75 / 16.0f) * 16));
                    }
                    i75 = Math.round(i75 / 16.0f) * 16;
                }
                i60 = i75;
                i61 = i74;
                if (BuildVars.LOGS_ENABLED) {
                }
                mediaCodec2 = createEncoderForMimeType();
                MediaFormat createVideoFormat22 = MediaFormat.createVideoFormat(this.outputMimeType, i61, i60);
                createVideoFormat22.setInteger("color-format", 2130708361);
                createVideoFormat22.setInteger("bitrate", i77);
                createVideoFormat22.setInteger("frame-rate", 30);
                createVideoFormat22.setInteger("i-frame-interval", 1);
                name2 = mediaCodec2.getName();
            } catch (Exception e23) {
                i57 = i77;
                i58 = i76;
                exc2 = e23;
                mediaCodec2 = null;
                inputSurface5 = null;
                i10 = -5;
                outputSurface11 = null;
                audioRecoder3 = null;
                str12 = null;
                z31 = false;
                if (exc2 instanceof IllegalStateException) {
                }
                StringBuilder sb222222222222222222 = new StringBuilder();
                sb222222222222222222.append("bitrate: ");
                i77 = i57;
                sb222222222222222222.append(i77);
                sb222222222222222222.append(" framerate: ");
                i59 = i58;
                sb222222222222222222.append(i59);
                sb222222222222222222.append(" size: ");
                sb222222222222222222.append(i75);
                str = "x";
                sb222222222222222222.append(str);
                sb222222222222222222.append(i74);
                FileLog.e(sb222222222222222222.toString());
                FileLog.e(exc2);
                z6 = z32;
                i60 = i75;
                z7 = true;
                outputSurface3 = outputSurface11;
                if (outputSurface3 != null) {
                }
                if (inputSurface5 != null) {
                }
                if (mediaCodec2 != null) {
                }
                if (audioRecoder3 != null) {
                }
                checkConversionCanceled();
                mediaCodec4 = mediaCodec2;
                inputSurface2 = inputSurface5;
                i4 = i60;
                z3 = z39;
                str2 = str12;
                z4 = z31;
                file = file2;
                mediaCodec5 = null;
                z2 = z;
                mediaExtractor2 = this.extractor;
                if (mediaExtractor2 != null) {
                }
                mP4Builder2 = this.mediaMuxer;
                if (mP4Builder2 != null) {
                }
                if (mediaCodec4 != null) {
                }
                if (mediaCodec5 != null) {
                }
                if (outputSurface3 != null) {
                }
                if (inputSurface2 != null) {
                }
                str3 = str2;
                i75 = i4;
                if (z6) {
                }
            } catch (Throwable th29) {
                th = th29;
                z2 = z;
                th = th;
                z3 = z39;
                i2 = i76;
                str = "x";
                file = file2;
                mediaCodec2 = null;
                inputSurface = null;
                i3 = -5;
                str2 = null;
                mediaCodec = null;
                z4 = false;
                z5 = false;
                outputSurface = null;
                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                FileLog.e(th);
                mediaExtractor = this.extractor;
                if (mediaExtractor != null) {
                }
                mP4Builder = this.mediaMuxer;
                if (mP4Builder != null) {
                }
                if (mediaCodec2 != null) {
                }
                if (mediaCodec != null) {
                }
                if (outputSurface != null) {
                }
                if (inputSurface != null) {
                }
                str3 = str2;
                z6 = z5;
                z7 = true;
                if (z6) {
                }
            }
        } else {
            file = null;
            try {
                MediaExtractor mediaExtractor4 = new MediaExtractor();
                this.extractor = mediaExtractor4;
                str6 = str4;
                mediaExtractor4.setDataSource(str14);
                try {
                    findTrack = MediaController.findTrack(this.extractor, false);
                    if (i77 == -1 || z42 || f4 <= 0.0f) {
                        i4 = i75;
                        str7 = str5;
                        i5 = -1;
                    } else {
                        try {
                            i4 = i75;
                        } catch (Throwable th30) {
                            z2 = z;
                            th = th30;
                            str = "x";
                            mediaCodec2 = null;
                            inputSurface = null;
                            str2 = null;
                            mediaCodec = null;
                            outputSurface = null;
                            z3 = z39;
                            i2 = i76;
                            file = file2;
                            i3 = -5;
                            z4 = false;
                            z5 = false;
                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                            FileLog.e(th);
                            mediaExtractor = this.extractor;
                            if (mediaExtractor != null) {
                            }
                            mP4Builder = this.mediaMuxer;
                            if (mP4Builder != null) {
                            }
                            if (mediaCodec2 != null) {
                            }
                            if (mediaCodec != null) {
                            }
                            if (outputSurface != null) {
                            }
                            if (inputSurface != null) {
                            }
                            str3 = str2;
                            z6 = z5;
                            z7 = true;
                            if (z6) {
                            }
                        }
                        try {
                            i5 = MediaController.findTrack(this.extractor, true);
                            str7 = str5;
                        } catch (Throwable th31) {
                            z2 = z;
                            th = th31;
                            str = "x";
                            mediaCodec2 = null;
                            inputSurface = null;
                            str2 = null;
                            mediaCodec = null;
                            outputSurface = null;
                            i75 = i4;
                            z3 = z39;
                            i2 = i76;
                            file = file2;
                            i3 = -5;
                            z4 = false;
                            z5 = false;
                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                            FileLog.e(th);
                            mediaExtractor = this.extractor;
                            if (mediaExtractor != null) {
                            }
                            mP4Builder = this.mediaMuxer;
                            if (mP4Builder != null) {
                            }
                            if (mediaCodec2 != null) {
                            }
                            if (mediaCodec != null) {
                            }
                            if (outputSurface != null) {
                            }
                            if (inputSurface != null) {
                            }
                            str3 = str2;
                            z6 = z5;
                            z7 = true;
                            if (z6) {
                            }
                        }
                    }
                } catch (Throwable th32) {
                    th = th32;
                    z2 = z;
                    str = "x";
                    z3 = z39;
                    i2 = i76;
                    file = file2;
                    th = th;
                    mediaCodec2 = null;
                    inputSurface = null;
                    i3 = -5;
                    str2 = null;
                    mediaCodec = null;
                    z4 = false;
                    z5 = false;
                    outputSurface = null;
                    FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                    FileLog.e(th);
                    mediaExtractor = this.extractor;
                    if (mediaExtractor != null) {
                    }
                    mP4Builder = this.mediaMuxer;
                    if (mP4Builder != null) {
                    }
                    if (mediaCodec2 != null) {
                    }
                    if (mediaCodec != null) {
                    }
                    if (outputSurface != null) {
                    }
                    if (inputSurface != null) {
                    }
                    str3 = str2;
                    z6 = z5;
                    z7 = true;
                    if (z6) {
                    }
                }
            } catch (Throwable th33) {
                th = th33;
                z2 = z;
                i2 = i76;
                str = "x";
                z3 = z39;
            }
            if (findTrack >= 0) {
                i6 = i5;
                if (!this.extractor.getTrackFormat(findTrack).getString("mime").equals(MediaController.VIDEO_MIME_TYPE)) {
                    z8 = true;
                    if (!z39 || z8) {
                        long j35 = j29;
                        z3 = z39;
                        file = file2;
                        if (findTrack < 0) {
                            try {
                                long j36 = (1000 / i76) * 1000;
                                if (i76 < 30) {
                                    j3 = j36;
                                    try {
                                        i17 = 1000 / (i76 + 5);
                                    } catch (Exception e24) {
                                        exc = e24;
                                        i13 = findTrack;
                                        i2 = i76;
                                        i77 = i77;
                                        i14 = i4;
                                        i16 = -5;
                                        mediaCodec6 = null;
                                        mediaCodec4 = null;
                                        audioRecoder = null;
                                        z4 = false;
                                        outputSurface = null;
                                        inputSurface2 = null;
                                        i15 = i74;
                                        str2 = null;
                                        try {
                                            if (exc instanceof IllegalStateException) {
                                            }
                                            z5 = false;
                                            try {
                                                StringBuilder sb5 = new StringBuilder();
                                                sb5.append("bitrate: ");
                                                sb5.append(i77);
                                                sb5.append(" framerate: ");
                                                sb5.append(i2);
                                                sb5.append(" size: ");
                                                i8 = i14;
                                                try {
                                                    sb5.append(i8);
                                                    str = "x";
                                                    try {
                                                        sb5.append(str);
                                                        i7 = i15;
                                                        try {
                                                            sb5.append(i7);
                                                            FileLog.e(sb5.toString());
                                                            FileLog.e(exc);
                                                            i9 = i16;
                                                            mediaCodec7 = mediaCodec6;
                                                            outputSurface4 = outputSurface;
                                                            z7 = true;
                                                            this.extractor.unselectTrack(i13);
                                                            if (mediaCodec7 != null) {
                                                            }
                                                            mediaCodec3 = mediaCodec7;
                                                            outputSurface2 = outputSurface4;
                                                            if (outputSurface2 != null) {
                                                            }
                                                            if (inputSurface2 != null) {
                                                            }
                                                            if (mediaCodec4 != null) {
                                                            }
                                                            if (audioRecoder != null) {
                                                            }
                                                            checkConversionCanceled();
                                                            mediaCodec5 = mediaCodec3;
                                                            i4 = i8;
                                                            i74 = i7;
                                                            z6 = z5;
                                                            int i88 = i9;
                                                            outputSurface3 = outputSurface2;
                                                            i10 = i88;
                                                            mediaExtractor2 = this.extractor;
                                                            if (mediaExtractor2 != null) {
                                                            }
                                                            mP4Builder2 = this.mediaMuxer;
                                                            if (mP4Builder2 != null) {
                                                            }
                                                            if (mediaCodec4 != null) {
                                                            }
                                                            if (mediaCodec5 != null) {
                                                            }
                                                            if (outputSurface3 != null) {
                                                            }
                                                            if (inputSurface2 != null) {
                                                            }
                                                            str3 = str2;
                                                            i75 = i4;
                                                        } catch (Throwable th34) {
                                                            th = th34;
                                                            th = th;
                                                            i75 = i8;
                                                            i74 = i7;
                                                            mediaCodec = mediaCodec6;
                                                            mediaCodec2 = mediaCodec4;
                                                            i3 = i16;
                                                            inputSurface = inputSurface2;
                                                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                            FileLog.e(th);
                                                            mediaExtractor = this.extractor;
                                                            if (mediaExtractor != null) {
                                                            }
                                                            mP4Builder = this.mediaMuxer;
                                                            if (mP4Builder != null) {
                                                            }
                                                            if (mediaCodec2 != null) {
                                                            }
                                                            if (mediaCodec != null) {
                                                            }
                                                            if (outputSurface != null) {
                                                            }
                                                            if (inputSurface != null) {
                                                            }
                                                            str3 = str2;
                                                            z6 = z5;
                                                            z7 = true;
                                                            if (z6) {
                                                            }
                                                        }
                                                    } catch (Throwable th35) {
                                                        th = th35;
                                                        i7 = i15;
                                                    }
                                                } catch (Throwable th36) {
                                                    th = th36;
                                                    i7 = i15;
                                                    str = "x";
                                                }
                                            } catch (Throwable th37) {
                                                th = th37;
                                                i7 = i15;
                                                str = "x";
                                                i8 = i14;
                                            }
                                        } catch (Throwable th38) {
                                            z2 = z;
                                            str = "x";
                                            th = th38;
                                            i75 = i14;
                                            i74 = i15;
                                            mediaCodec = mediaCodec6;
                                            mediaCodec2 = mediaCodec4;
                                            z5 = false;
                                        }
                                        if (z6) {
                                        }
                                    }
                                } else {
                                    j3 = j36;
                                    i17 = 1000 / (i76 + 1);
                                }
                                j4 = i17 * 1000;
                                this.extractor.selectTrack(findTrack);
                                trackFormat = this.extractor.getTrackFormat(findTrack);
                                if (z44) {
                                    int i89 = f <= 2000.0f ? 2600000 : f <= 5000.0f ? 2200000 : 1560000;
                                    i18 = i78;
                                    if (i18 >= 15000000) {
                                        str8 = "OMX.google.h264.encoder";
                                        i19 = i89;
                                    } else {
                                        i19 = i89;
                                        str8 = null;
                                    }
                                    j = 0;
                                } else {
                                    i18 = i78;
                                    i19 = i77 <= 0 ? 921600 : i77;
                                    str8 = null;
                                }
                                if (i18 > 0) {
                                    try {
                                        try {
                                            i19 = Math.min(i18, i19);
                                        } catch (Throwable th39) {
                                            z2 = z;
                                            th = th39;
                                            i77 = i19;
                                            i2 = i76;
                                            i74 = i74;
                                            i75 = i4;
                                            str = "x";
                                            mediaCodec2 = null;
                                            inputSurface = null;
                                            i3 = -5;
                                            str2 = null;
                                            mediaCodec = null;
                                            z4 = false;
                                            z5 = false;
                                            outputSurface = null;
                                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                            FileLog.e(th);
                                            mediaExtractor = this.extractor;
                                            if (mediaExtractor != null) {
                                            }
                                            mP4Builder = this.mediaMuxer;
                                            if (mP4Builder != null) {
                                            }
                                            if (mediaCodec2 != null) {
                                            }
                                            if (mediaCodec != null) {
                                            }
                                            if (outputSurface != null) {
                                            }
                                            if (inputSurface != null) {
                                            }
                                            str3 = str2;
                                            z6 = z5;
                                            z7 = true;
                                            if (z6) {
                                            }
                                        }
                                    } catch (Exception e25) {
                                        exc = e25;
                                        i13 = findTrack;
                                        i77 = i19;
                                        i2 = i76;
                                        i14 = i4;
                                        i16 = -5;
                                        mediaCodec6 = null;
                                        mediaCodec4 = null;
                                        audioRecoder = null;
                                        z4 = false;
                                        outputSurface = null;
                                        inputSurface2 = null;
                                        i15 = i74;
                                        str2 = null;
                                        if (exc instanceof IllegalStateException) {
                                        }
                                        z5 = false;
                                        StringBuilder sb52 = new StringBuilder();
                                        sb52.append("bitrate: ");
                                        sb52.append(i77);
                                        sb52.append(" framerate: ");
                                        sb52.append(i2);
                                        sb52.append(" size: ");
                                        i8 = i14;
                                        sb52.append(i8);
                                        str = "x";
                                        sb52.append(str);
                                        i7 = i15;
                                        sb52.append(i7);
                                        FileLog.e(sb52.toString());
                                        FileLog.e(exc);
                                        i9 = i16;
                                        mediaCodec7 = mediaCodec6;
                                        outputSurface4 = outputSurface;
                                        z7 = true;
                                        this.extractor.unselectTrack(i13);
                                        if (mediaCodec7 != null) {
                                        }
                                        mediaCodec3 = mediaCodec7;
                                        outputSurface2 = outputSurface4;
                                        if (outputSurface2 != null) {
                                        }
                                        if (inputSurface2 != null) {
                                        }
                                        if (mediaCodec4 != null) {
                                        }
                                        if (audioRecoder != null) {
                                        }
                                        checkConversionCanceled();
                                        mediaCodec5 = mediaCodec3;
                                        i4 = i8;
                                        i74 = i7;
                                        z6 = z5;
                                        int i882 = i9;
                                        outputSurface3 = outputSurface2;
                                        i10 = i882;
                                        mediaExtractor2 = this.extractor;
                                        if (mediaExtractor2 != null) {
                                        }
                                        mP4Builder2 = this.mediaMuxer;
                                        if (mP4Builder2 != null) {
                                        }
                                        if (mediaCodec4 != null) {
                                        }
                                        if (mediaCodec5 != null) {
                                        }
                                        if (outputSurface3 != null) {
                                        }
                                        if (inputSurface2 != null) {
                                        }
                                        str3 = str2;
                                        i75 = i4;
                                        if (z6) {
                                        }
                                    }
                                }
                                j5 = j >= 0 ? -1L : j;
                            } catch (Exception e26) {
                                i13 = findTrack;
                                i2 = i76;
                                i14 = i4;
                                i15 = i74;
                                exc = e26;
                                i77 = i77;
                            } catch (Throwable th40) {
                                i2 = i76;
                                i11 = i4;
                                i12 = i74;
                                z2 = z;
                                th = th40;
                                i77 = i77;
                            }
                            if (j5 >= 0) {
                                obj = "video/hevc";
                                this.extractor.seekTo(j5, 0);
                                j7 = r3;
                                j6 = j5;
                            } else {
                                obj = "video/hevc";
                                j6 = j5;
                                if (r3 > 0) {
                                    this.extractor.seekTo(r3, 0);
                                    j7 = r3;
                                } else {
                                    try {
                                        j7 = r3;
                                        this.extractor.seekTo(0L, 0);
                                        if (cropState3 == null && cropState3.useMatrix == null) {
                                            i20 = i71;
                                            if (i20 != 90 && i20 != 270) {
                                                i55 = cropState3.transformWidth;
                                                i56 = cropState3.transformHeight;
                                                int i90 = i56;
                                                i21 = i55;
                                                i22 = i90;
                                            }
                                            i55 = cropState3.transformHeight;
                                            i56 = cropState3.transformWidth;
                                            int i902 = i56;
                                            i21 = i55;
                                            i22 = i902;
                                        } else {
                                            i20 = i71;
                                            i21 = i74;
                                            i22 = i4;
                                        }
                                    } catch (Exception e27) {
                                        i13 = findTrack;
                                        i2 = i76;
                                        i14 = i4;
                                        i15 = i74;
                                        exc = e27;
                                        i77 = i19;
                                        i16 = -5;
                                        mediaCodec6 = null;
                                        str2 = null;
                                        mediaCodec4 = null;
                                        audioRecoder = null;
                                        z4 = false;
                                        outputSurface = null;
                                        inputSurface2 = null;
                                        if (exc instanceof IllegalStateException) {
                                        }
                                        z5 = false;
                                        StringBuilder sb522 = new StringBuilder();
                                        sb522.append("bitrate: ");
                                        sb522.append(i77);
                                        sb522.append(" framerate: ");
                                        sb522.append(i2);
                                        sb522.append(" size: ");
                                        i8 = i14;
                                        sb522.append(i8);
                                        str = "x";
                                        sb522.append(str);
                                        i7 = i15;
                                        sb522.append(i7);
                                        FileLog.e(sb522.toString());
                                        FileLog.e(exc);
                                        i9 = i16;
                                        mediaCodec7 = mediaCodec6;
                                        outputSurface4 = outputSurface;
                                        z7 = true;
                                        this.extractor.unselectTrack(i13);
                                        if (mediaCodec7 != null) {
                                        }
                                        mediaCodec3 = mediaCodec7;
                                        outputSurface2 = outputSurface4;
                                        if (outputSurface2 != null) {
                                        }
                                        if (inputSurface2 != null) {
                                        }
                                        if (mediaCodec4 != null) {
                                        }
                                        if (audioRecoder != null) {
                                        }
                                        checkConversionCanceled();
                                        mediaCodec5 = mediaCodec3;
                                        i4 = i8;
                                        i74 = i7;
                                        z6 = z5;
                                        int i8822 = i9;
                                        outputSurface3 = outputSurface2;
                                        i10 = i8822;
                                        mediaExtractor2 = this.extractor;
                                        if (mediaExtractor2 != null) {
                                        }
                                        mP4Builder2 = this.mediaMuxer;
                                        if (mP4Builder2 != null) {
                                        }
                                        if (mediaCodec4 != null) {
                                        }
                                        if (mediaCodec5 != null) {
                                        }
                                        if (outputSurface3 != null) {
                                        }
                                        if (inputSurface2 != null) {
                                        }
                                        str3 = str2;
                                        i75 = i4;
                                        if (z6) {
                                        }
                                    } catch (Throwable th41) {
                                        i2 = i76;
                                        i11 = i4;
                                        i12 = i74;
                                        z2 = z;
                                        th = th41;
                                        i77 = i19;
                                        i74 = i12;
                                        str = "x";
                                        i75 = i11;
                                        mediaCodec2 = null;
                                        inputSurface = null;
                                        i3 = -5;
                                        str2 = null;
                                        mediaCodec = null;
                                        z4 = false;
                                        z5 = false;
                                        outputSurface = null;
                                        FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                        FileLog.e(th);
                                        mediaExtractor = this.extractor;
                                        if (mediaExtractor != null) {
                                        }
                                        mP4Builder = this.mediaMuxer;
                                        if (mP4Builder != null) {
                                        }
                                        if (mediaCodec2 != null) {
                                        }
                                        if (mediaCodec != null) {
                                        }
                                        if (outputSurface != null) {
                                        }
                                        if (inputSurface != null) {
                                        }
                                        str3 = str2;
                                        z6 = z5;
                                        z7 = true;
                                        if (z6) {
                                        }
                                    }
                                    if (str8 != null) {
                                        try {
                                            createByCodecName = MediaCodec.createByCodecName(str8);
                                        } catch (Exception unused5) {
                                        }
                                        if (createByCodecName == null) {
                                            try {
                                                createByCodecName = createEncoderForMimeType();
                                            } catch (Exception e28) {
                                                exc = e28;
                                                i13 = findTrack;
                                                i77 = i19;
                                                i2 = i76;
                                                mediaCodec4 = createByCodecName;
                                                i14 = i4;
                                                i16 = -5;
                                                mediaCodec6 = null;
                                                audioRecoder = null;
                                                z4 = false;
                                                outputSurface = null;
                                                inputSurface2 = null;
                                                i15 = i74;
                                                str2 = null;
                                                if (exc instanceof IllegalStateException) {
                                                }
                                                z5 = false;
                                                StringBuilder sb5222 = new StringBuilder();
                                                sb5222.append("bitrate: ");
                                                sb5222.append(i77);
                                                sb5222.append(" framerate: ");
                                                sb5222.append(i2);
                                                sb5222.append(" size: ");
                                                i8 = i14;
                                                sb5222.append(i8);
                                                str = "x";
                                                sb5222.append(str);
                                                i7 = i15;
                                                sb5222.append(i7);
                                                FileLog.e(sb5222.toString());
                                                FileLog.e(exc);
                                                i9 = i16;
                                                mediaCodec7 = mediaCodec6;
                                                outputSurface4 = outputSurface;
                                                z7 = true;
                                                this.extractor.unselectTrack(i13);
                                                if (mediaCodec7 != null) {
                                                }
                                                mediaCodec3 = mediaCodec7;
                                                outputSurface2 = outputSurface4;
                                                if (outputSurface2 != null) {
                                                }
                                                if (inputSurface2 != null) {
                                                }
                                                if (mediaCodec4 != null) {
                                                }
                                                if (audioRecoder != null) {
                                                }
                                                checkConversionCanceled();
                                                mediaCodec5 = mediaCodec3;
                                                i4 = i8;
                                                i74 = i7;
                                                z6 = z5;
                                                int i88222 = i9;
                                                outputSurface3 = outputSurface2;
                                                i10 = i88222;
                                                mediaExtractor2 = this.extractor;
                                                if (mediaExtractor2 != null) {
                                                }
                                                mP4Builder2 = this.mediaMuxer;
                                                if (mP4Builder2 != null) {
                                                }
                                                if (mediaCodec4 != null) {
                                                }
                                                if (mediaCodec5 != null) {
                                                }
                                                if (outputSurface3 != null) {
                                                }
                                                if (inputSurface2 != null) {
                                                }
                                                str3 = str2;
                                                i75 = i4;
                                                if (z6) {
                                                }
                                            } catch (Throwable th42) {
                                                z2 = z;
                                                th = th42;
                                                i77 = i19;
                                                i2 = i76;
                                                mediaCodec2 = createByCodecName;
                                                i74 = i74;
                                                i75 = i4;
                                                str = "x";
                                                inputSurface = null;
                                                i3 = -5;
                                                str2 = null;
                                                mediaCodec = null;
                                                z4 = false;
                                                z5 = false;
                                                outputSurface = null;
                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                FileLog.e(th);
                                                mediaExtractor = this.extractor;
                                                if (mediaExtractor != null) {
                                                }
                                                mP4Builder = this.mediaMuxer;
                                                if (mP4Builder != null) {
                                                }
                                                if (mediaCodec2 != null) {
                                                }
                                                if (mediaCodec != null) {
                                                }
                                                if (outputSurface != null) {
                                                }
                                                if (inputSurface != null) {
                                                }
                                                str3 = str2;
                                                z6 = z5;
                                                z7 = true;
                                                if (z6) {
                                                }
                                            }
                                        }
                                        MediaCodec mediaCodec14 = createByCodecName;
                                        if (BuildVars.LOGS_ENABLED) {
                                            try {
                                                try {
                                                    sb = new StringBuilder();
                                                    i13 = findTrack;
                                                } catch (Exception e29) {
                                                    exc = e29;
                                                    i13 = findTrack;
                                                    i77 = i19;
                                                    i2 = i76;
                                                    i14 = i4;
                                                    mediaCodec4 = mediaCodec14;
                                                    i16 = -5;
                                                    mediaCodec6 = null;
                                                    audioRecoder = null;
                                                    z4 = false;
                                                    outputSurface = null;
                                                    inputSurface2 = null;
                                                    i15 = i74;
                                                    str2 = null;
                                                    if (exc instanceof IllegalStateException) {
                                                    }
                                                    z5 = false;
                                                    StringBuilder sb52222 = new StringBuilder();
                                                    sb52222.append("bitrate: ");
                                                    sb52222.append(i77);
                                                    sb52222.append(" framerate: ");
                                                    sb52222.append(i2);
                                                    sb52222.append(" size: ");
                                                    i8 = i14;
                                                    sb52222.append(i8);
                                                    str = "x";
                                                    sb52222.append(str);
                                                    i7 = i15;
                                                    sb52222.append(i7);
                                                    FileLog.e(sb52222.toString());
                                                    FileLog.e(exc);
                                                    i9 = i16;
                                                    mediaCodec7 = mediaCodec6;
                                                    outputSurface4 = outputSurface;
                                                    z7 = true;
                                                    this.extractor.unselectTrack(i13);
                                                    if (mediaCodec7 != null) {
                                                    }
                                                    mediaCodec3 = mediaCodec7;
                                                    outputSurface2 = outputSurface4;
                                                    if (outputSurface2 != null) {
                                                    }
                                                    if (inputSurface2 != null) {
                                                    }
                                                    if (mediaCodec4 != null) {
                                                    }
                                                    if (audioRecoder != null) {
                                                    }
                                                    checkConversionCanceled();
                                                    mediaCodec5 = mediaCodec3;
                                                    i4 = i8;
                                                    i74 = i7;
                                                    z6 = z5;
                                                    int i882222 = i9;
                                                    outputSurface3 = outputSurface2;
                                                    i10 = i882222;
                                                    mediaExtractor2 = this.extractor;
                                                    if (mediaExtractor2 != null) {
                                                    }
                                                    mP4Builder2 = this.mediaMuxer;
                                                    if (mP4Builder2 != null) {
                                                    }
                                                    if (mediaCodec4 != null) {
                                                    }
                                                    if (mediaCodec5 != null) {
                                                    }
                                                    if (outputSurface3 != null) {
                                                    }
                                                    if (inputSurface2 != null) {
                                                    }
                                                    str3 = str2;
                                                    i75 = i4;
                                                    if (z6) {
                                                    }
                                                }
                                                try {
                                                    sb.append("create encoder with w = ");
                                                    sb.append(i21);
                                                    sb.append(" h = ");
                                                    sb.append(i22);
                                                    sb.append(" bitrate = ");
                                                    sb.append(i19);
                                                    FileLog.d(sb.toString());
                                                } catch (Exception e30) {
                                                    exc = e30;
                                                    i77 = i19;
                                                    i2 = i76;
                                                    i14 = i4;
                                                    mediaCodec4 = mediaCodec14;
                                                    i16 = -5;
                                                    mediaCodec6 = null;
                                                    audioRecoder = null;
                                                    z4 = false;
                                                    outputSurface = null;
                                                    inputSurface2 = null;
                                                    i15 = i74;
                                                    str2 = null;
                                                    if (exc instanceof IllegalStateException) {
                                                    }
                                                    z5 = false;
                                                    StringBuilder sb522222 = new StringBuilder();
                                                    sb522222.append("bitrate: ");
                                                    sb522222.append(i77);
                                                    sb522222.append(" framerate: ");
                                                    sb522222.append(i2);
                                                    sb522222.append(" size: ");
                                                    i8 = i14;
                                                    sb522222.append(i8);
                                                    str = "x";
                                                    sb522222.append(str);
                                                    i7 = i15;
                                                    sb522222.append(i7);
                                                    FileLog.e(sb522222.toString());
                                                    FileLog.e(exc);
                                                    i9 = i16;
                                                    mediaCodec7 = mediaCodec6;
                                                    outputSurface4 = outputSurface;
                                                    z7 = true;
                                                    this.extractor.unselectTrack(i13);
                                                    if (mediaCodec7 != null) {
                                                    }
                                                    mediaCodec3 = mediaCodec7;
                                                    outputSurface2 = outputSurface4;
                                                    if (outputSurface2 != null) {
                                                    }
                                                    if (inputSurface2 != null) {
                                                    }
                                                    if (mediaCodec4 != null) {
                                                    }
                                                    if (audioRecoder != null) {
                                                    }
                                                    checkConversionCanceled();
                                                    mediaCodec5 = mediaCodec3;
                                                    i4 = i8;
                                                    i74 = i7;
                                                    z6 = z5;
                                                    int i8822222 = i9;
                                                    outputSurface3 = outputSurface2;
                                                    i10 = i8822222;
                                                    mediaExtractor2 = this.extractor;
                                                    if (mediaExtractor2 != null) {
                                                    }
                                                    mP4Builder2 = this.mediaMuxer;
                                                    if (mP4Builder2 != null) {
                                                    }
                                                    if (mediaCodec4 != null) {
                                                    }
                                                    if (mediaCodec5 != null) {
                                                    }
                                                    if (outputSurface3 != null) {
                                                    }
                                                    if (inputSurface2 != null) {
                                                    }
                                                    str3 = str2;
                                                    i75 = i4;
                                                    if (z6) {
                                                    }
                                                }
                                            } catch (Throwable th43) {
                                                z2 = z;
                                                th = th43;
                                                i77 = i19;
                                                i2 = i76;
                                                i74 = i74;
                                                i75 = i4;
                                                mediaCodec2 = mediaCodec14;
                                                str = "x";
                                                inputSurface = null;
                                                i3 = -5;
                                                str2 = null;
                                                mediaCodec = null;
                                                z4 = false;
                                                z5 = false;
                                                outputSurface = null;
                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                FileLog.e(th);
                                                mediaExtractor = this.extractor;
                                                if (mediaExtractor != null) {
                                                }
                                                mP4Builder = this.mediaMuxer;
                                                if (mP4Builder != null) {
                                                }
                                                if (mediaCodec2 != null) {
                                                }
                                                if (mediaCodec != null) {
                                                }
                                                if (outputSurface != null) {
                                                }
                                                if (inputSurface != null) {
                                                }
                                                str3 = str2;
                                                z6 = z5;
                                                z7 = true;
                                                if (z6) {
                                                }
                                            }
                                        } else {
                                            i13 = findTrack;
                                        }
                                        createVideoFormat = MediaFormat.createVideoFormat(this.outputMimeType, i21, i22);
                                        createVideoFormat.setInteger("color-format", 2130708361);
                                        createVideoFormat.setInteger("bitrate", i19);
                                        if (z44 && Build.VERSION.SDK_INT >= 21) {
                                            createVideoFormat.setInteger("bitrate-mode", 2);
                                        }
                                        createVideoFormat.setInteger("max-bitrate", i19);
                                        createVideoFormat.setInteger("frame-rate", i76);
                                        createVideoFormat.setInteger("i-frame-interval", 1);
                                        i23 = Build.VERSION.SDK_INT;
                                        if (i23 >= 24) {
                                            if (trackFormat.containsKey("color-transfer")) {
                                                i25 = trackFormat.getInteger("color-transfer");
                                                str9 = "mime";
                                            } else {
                                                str9 = "mime";
                                                i25 = 0;
                                            }
                                            if (trackFormat.containsKey("color-standard")) {
                                                i24 = trackFormat.getInteger("color-standard");
                                                mp4Movie2 = mp4Movie;
                                            } else {
                                                mp4Movie2 = mp4Movie;
                                                i24 = 0;
                                            }
                                            if (trackFormat.containsKey("color-range")) {
                                                trackFormat.getInteger("color-range");
                                            }
                                            if ((i25 == 6 || i25 == 7) && i24 == 6) {
                                                z9 = true;
                                                if (i23 >= 23) {
                                                    i26 = i22;
                                                    if (Math.min(i22, i21) <= 480 && !z44) {
                                                        i77 = i19 > 921600 ? 921600 : i19;
                                                        try {
                                                            createVideoFormat.setInteger("bitrate", i77);
                                                            i27 = i77;
                                                            name = mediaCodec14.getName();
                                                            z4 = "c2.qti.avc.encoder".equalsIgnoreCase(name);
                                                            FileLog.d("selected encoder " + name);
                                                            mediaCodec14.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                                                            i28 = i21;
                                                            inputSurface = new InputSurface(mediaCodec14.createInputSurface());
                                                            inputSurface.makeCurrent();
                                                            mediaCodec14.start();
                                                            i14 = i4;
                                                            i29 = i76;
                                                            i15 = i74;
                                                            outputSurface5 = new OutputSurface(savedFilterState, null, str15, str16, arrayList, cropState3, i15, i14, i72, i73, i20, i76, false, num, num2, hDRInfo, convertVideoParams);
                                                        } catch (Exception e31) {
                                                            exc = e31;
                                                            i2 = i76;
                                                            i14 = i4;
                                                            mediaCodec4 = mediaCodec14;
                                                            i16 = -5;
                                                            mediaCodec6 = null;
                                                            audioRecoder = null;
                                                            z4 = false;
                                                            outputSurface = null;
                                                            inputSurface2 = null;
                                                            i15 = i74;
                                                            str2 = null;
                                                            if (exc instanceof IllegalStateException) {
                                                            }
                                                            z5 = false;
                                                            StringBuilder sb5222222 = new StringBuilder();
                                                            sb5222222.append("bitrate: ");
                                                            sb5222222.append(i77);
                                                            sb5222222.append(" framerate: ");
                                                            sb5222222.append(i2);
                                                            sb5222222.append(" size: ");
                                                            i8 = i14;
                                                            sb5222222.append(i8);
                                                            str = "x";
                                                            sb5222222.append(str);
                                                            i7 = i15;
                                                            sb5222222.append(i7);
                                                            FileLog.e(sb5222222.toString());
                                                            FileLog.e(exc);
                                                            i9 = i16;
                                                            mediaCodec7 = mediaCodec6;
                                                            outputSurface4 = outputSurface;
                                                            z7 = true;
                                                            this.extractor.unselectTrack(i13);
                                                            if (mediaCodec7 != null) {
                                                            }
                                                            mediaCodec3 = mediaCodec7;
                                                            outputSurface2 = outputSurface4;
                                                            if (outputSurface2 != null) {
                                                            }
                                                            if (inputSurface2 != null) {
                                                            }
                                                            if (mediaCodec4 != null) {
                                                            }
                                                            if (audioRecoder != null) {
                                                            }
                                                            checkConversionCanceled();
                                                            mediaCodec5 = mediaCodec3;
                                                            i4 = i8;
                                                            i74 = i7;
                                                            z6 = z5;
                                                            int i88222222 = i9;
                                                            outputSurface3 = outputSurface2;
                                                            i10 = i88222222;
                                                            mediaExtractor2 = this.extractor;
                                                            if (mediaExtractor2 != null) {
                                                            }
                                                            mP4Builder2 = this.mediaMuxer;
                                                            if (mP4Builder2 != null) {
                                                            }
                                                            if (mediaCodec4 != null) {
                                                            }
                                                            if (mediaCodec5 != null) {
                                                            }
                                                            if (outputSurface3 != null) {
                                                            }
                                                            if (inputSurface2 != null) {
                                                            }
                                                            str3 = str2;
                                                            i75 = i4;
                                                            if (z6) {
                                                            }
                                                        } catch (Throwable th44) {
                                                            z2 = z;
                                                            th = th44;
                                                            i2 = i76;
                                                            i74 = i74;
                                                            i75 = i4;
                                                            mediaCodec2 = mediaCodec14;
                                                            str = "x";
                                                            inputSurface = null;
                                                            i3 = -5;
                                                            str2 = null;
                                                            mediaCodec = null;
                                                            z4 = false;
                                                            z5 = false;
                                                            outputSurface = null;
                                                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                            FileLog.e(th);
                                                            mediaExtractor = this.extractor;
                                                            if (mediaExtractor != null) {
                                                            }
                                                            mP4Builder = this.mediaMuxer;
                                                            if (mP4Builder != null) {
                                                            }
                                                            if (mediaCodec2 != null) {
                                                            }
                                                            if (mediaCodec != null) {
                                                            }
                                                            if (outputSurface != null) {
                                                            }
                                                            if (inputSurface != null) {
                                                            }
                                                            str3 = str2;
                                                            z6 = z5;
                                                            z7 = true;
                                                            if (z6) {
                                                            }
                                                        }
                                                        if (hDRInfo == null) {
                                                            try {
                                                            } catch (Exception e32) {
                                                                exc = e32;
                                                                mediaCodec4 = mediaCodec14;
                                                                outputSurface = outputSurface5;
                                                                inputSurface2 = inputSurface;
                                                                str2 = name;
                                                                i77 = i27;
                                                                i2 = i29;
                                                                i16 = -5;
                                                                mediaCodec6 = null;
                                                                audioRecoder = null;
                                                                if (exc instanceof IllegalStateException) {
                                                                }
                                                                z5 = false;
                                                                StringBuilder sb52222222 = new StringBuilder();
                                                                sb52222222.append("bitrate: ");
                                                                sb52222222.append(i77);
                                                                sb52222222.append(" framerate: ");
                                                                sb52222222.append(i2);
                                                                sb52222222.append(" size: ");
                                                                i8 = i14;
                                                                sb52222222.append(i8);
                                                                str = "x";
                                                                sb52222222.append(str);
                                                                i7 = i15;
                                                                sb52222222.append(i7);
                                                                FileLog.e(sb52222222.toString());
                                                                FileLog.e(exc);
                                                                i9 = i16;
                                                                mediaCodec7 = mediaCodec6;
                                                                outputSurface4 = outputSurface;
                                                                z7 = true;
                                                                this.extractor.unselectTrack(i13);
                                                                if (mediaCodec7 != null) {
                                                                }
                                                                mediaCodec3 = mediaCodec7;
                                                                outputSurface2 = outputSurface4;
                                                                if (outputSurface2 != null) {
                                                                }
                                                                if (inputSurface2 != null) {
                                                                }
                                                                if (mediaCodec4 != null) {
                                                                }
                                                                if (audioRecoder != null) {
                                                                }
                                                                checkConversionCanceled();
                                                                mediaCodec5 = mediaCodec3;
                                                                i4 = i8;
                                                                i74 = i7;
                                                                z6 = z5;
                                                                int i882222222 = i9;
                                                                outputSurface3 = outputSurface2;
                                                                i10 = i882222222;
                                                                mediaExtractor2 = this.extractor;
                                                                if (mediaExtractor2 != null) {
                                                                }
                                                                mP4Builder2 = this.mediaMuxer;
                                                                if (mP4Builder2 != null) {
                                                                }
                                                                if (mediaCodec4 != null) {
                                                                }
                                                                if (mediaCodec5 != null) {
                                                                }
                                                                if (outputSurface3 != null) {
                                                                }
                                                                if (inputSurface2 != null) {
                                                                }
                                                                str3 = str2;
                                                                i75 = i4;
                                                                if (z6) {
                                                                }
                                                            } catch (Throwable th45) {
                                                                z2 = z;
                                                                th = th45;
                                                                mediaCodec2 = mediaCodec14;
                                                                outputSurface = outputSurface5;
                                                                i74 = i15;
                                                                str2 = name;
                                                                i77 = i27;
                                                                str = "x";
                                                                i75 = i14;
                                                                i2 = i29;
                                                                i3 = -5;
                                                                mediaCodec = null;
                                                                z5 = false;
                                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                FileLog.e(th);
                                                                mediaExtractor = this.extractor;
                                                                if (mediaExtractor != null) {
                                                                }
                                                                mP4Builder = this.mediaMuxer;
                                                                if (mP4Builder != null) {
                                                                }
                                                                if (mediaCodec2 != null) {
                                                                }
                                                                if (mediaCodec != null) {
                                                                }
                                                                if (outputSurface != null) {
                                                                }
                                                                if (inputSurface != null) {
                                                                }
                                                                str3 = str2;
                                                                z6 = z5;
                                                                z7 = true;
                                                                if (z6) {
                                                                }
                                                            }
                                                            if (outputSurface5.supportsEXTYUV() && z9) {
                                                                StoryEntry.HDRInfo hDRInfo2 = new StoryEntry.HDRInfo();
                                                                hDRInfo2.colorTransfer = i25;
                                                                hDRInfo2.colorStandard = i24;
                                                                if (i23 >= 24) {
                                                                    createVideoFormat.setInteger("color-transfer", 3);
                                                                }
                                                                hDRInfo = hDRInfo2;
                                                                if (i23 >= 24 && hDRInfo != null) {
                                                                    try {
                                                                    } catch (Exception e33) {
                                                                        e = e33;
                                                                        mediaCodec4 = mediaCodec14;
                                                                        outputSurface6 = outputSurface5;
                                                                        inputSurface2 = inputSurface;
                                                                        str2 = name;
                                                                    } catch (Throwable th46) {
                                                                        str2 = name;
                                                                        z2 = z;
                                                                        th = th46;
                                                                        outputSurface = outputSurface5;
                                                                        mediaCodec2 = mediaCodec14;
                                                                        i74 = i15;
                                                                        i77 = i27;
                                                                        str = "x";
                                                                        i75 = i14;
                                                                        i2 = i29;
                                                                        i3 = -5;
                                                                        mediaCodec = null;
                                                                        z5 = false;
                                                                        FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                        FileLog.e(th);
                                                                        mediaExtractor = this.extractor;
                                                                        if (mediaExtractor != null) {
                                                                        }
                                                                        mP4Builder = this.mediaMuxer;
                                                                        if (mP4Builder != null) {
                                                                        }
                                                                        if (mediaCodec2 != null) {
                                                                        }
                                                                        if (mediaCodec != null) {
                                                                        }
                                                                        if (outputSurface != null) {
                                                                        }
                                                                        if (inputSurface != null) {
                                                                        }
                                                                        str3 = str2;
                                                                        z6 = z5;
                                                                        z7 = true;
                                                                        if (z6) {
                                                                        }
                                                                    }
                                                                    if (hDRInfo.getHDRType() != 0) {
                                                                        if (outputSurface5.supportsEXTYUV()) {
                                                                            f2 = f4;
                                                                            i30 = i6;
                                                                            j10 = j3;
                                                                            outputSurface = outputSurface5;
                                                                            str2 = name;
                                                                            mediaFormat = trackFormat;
                                                                            mediaCodec4 = mediaCodec14;
                                                                            j8 = j31;
                                                                            obj2 = obj;
                                                                            inputSurface2 = inputSurface;
                                                                            i31 = i28;
                                                                            j9 = j6;
                                                                            bufferInfo2 = bufferInfo;
                                                                            j11 = j7;
                                                                            i32 = i26;
                                                                            z10 = z38;
                                                                            try {
                                                                                outputSurface6 = outputSurface;
                                                                            } catch (Exception e34) {
                                                                                exc = e34;
                                                                                i77 = i27;
                                                                                i2 = i29;
                                                                                i16 = -5;
                                                                                mediaCodec6 = null;
                                                                                audioRecoder = null;
                                                                                if (exc instanceof IllegalStateException) {
                                                                                }
                                                                                z5 = false;
                                                                                StringBuilder sb522222222 = new StringBuilder();
                                                                                sb522222222.append("bitrate: ");
                                                                                sb522222222.append(i77);
                                                                                sb522222222.append(" framerate: ");
                                                                                sb522222222.append(i2);
                                                                                sb522222222.append(" size: ");
                                                                                i8 = i14;
                                                                                sb522222222.append(i8);
                                                                                str = "x";
                                                                                sb522222222.append(str);
                                                                                i7 = i15;
                                                                                sb522222222.append(i7);
                                                                                FileLog.e(sb522222222.toString());
                                                                                FileLog.e(exc);
                                                                                i9 = i16;
                                                                                mediaCodec7 = mediaCodec6;
                                                                                outputSurface4 = outputSurface;
                                                                                z7 = true;
                                                                                this.extractor.unselectTrack(i13);
                                                                                if (mediaCodec7 != null) {
                                                                                }
                                                                                mediaCodec3 = mediaCodec7;
                                                                                outputSurface2 = outputSurface4;
                                                                                if (outputSurface2 != null) {
                                                                                }
                                                                                if (inputSurface2 != null) {
                                                                                }
                                                                                if (mediaCodec4 != null) {
                                                                                }
                                                                                if (audioRecoder != null) {
                                                                                }
                                                                                checkConversionCanceled();
                                                                                mediaCodec5 = mediaCodec3;
                                                                                i4 = i8;
                                                                                i74 = i7;
                                                                                z6 = z5;
                                                                                int i8822222222 = i9;
                                                                                outputSurface3 = outputSurface2;
                                                                                i10 = i8822222222;
                                                                                mediaExtractor2 = this.extractor;
                                                                                if (mediaExtractor2 != null) {
                                                                                }
                                                                                mP4Builder2 = this.mediaMuxer;
                                                                                if (mP4Builder2 != null) {
                                                                                }
                                                                                if (mediaCodec4 != null) {
                                                                                }
                                                                                if (mediaCodec5 != null) {
                                                                                }
                                                                                if (outputSurface3 != null) {
                                                                                }
                                                                                if (inputSurface2 != null) {
                                                                                }
                                                                                str3 = str2;
                                                                                i75 = i4;
                                                                                if (z6) {
                                                                                }
                                                                            } catch (Throwable th47) {
                                                                                z2 = z;
                                                                                th = th47;
                                                                            }
                                                                            try {
                                                                                outputSurface6.changeFragmentShader(hdrFragmentShader(i72, i73, i15, i14, true, hDRInfo), hdrFragmentShader(i72, i73, i15, i14, false, hDRInfo), true);
                                                                                bufferInfo3 = bufferInfo2;
                                                                                i34 = 0;
                                                                                outputSurface4 = outputSurface6;
                                                                                mediaCodec7 = getDecoderByFormat(mediaFormat);
                                                                                mediaCodec7.configure(mediaFormat, outputSurface4.getSurface(), (MediaCrypto) null, i34);
                                                                                mediaCodec7.start();
                                                                                if (i23 >= 21) {
                                                                                    try {
                                                                                        try {
                                                                                            inputBuffers = mediaCodec7.getInputBuffers();
                                                                                            outputBuffers = mediaCodec4.getOutputBuffers();
                                                                                        } catch (Exception e35) {
                                                                                            exc = e35;
                                                                                            audioRecoder = null;
                                                                                            mediaCodec6 = mediaCodec7;
                                                                                            outputSurface = outputSurface4;
                                                                                            i77 = i27;
                                                                                            i2 = i29;
                                                                                            i16 = -5;
                                                                                            if (exc instanceof IllegalStateException) {
                                                                                            }
                                                                                            z5 = false;
                                                                                            StringBuilder sb5222222222 = new StringBuilder();
                                                                                            sb5222222222.append("bitrate: ");
                                                                                            sb5222222222.append(i77);
                                                                                            sb5222222222.append(" framerate: ");
                                                                                            sb5222222222.append(i2);
                                                                                            sb5222222222.append(" size: ");
                                                                                            i8 = i14;
                                                                                            sb5222222222.append(i8);
                                                                                            str = "x";
                                                                                            sb5222222222.append(str);
                                                                                            i7 = i15;
                                                                                            sb5222222222.append(i7);
                                                                                            FileLog.e(sb5222222222.toString());
                                                                                            FileLog.e(exc);
                                                                                            i9 = i16;
                                                                                            mediaCodec7 = mediaCodec6;
                                                                                            outputSurface4 = outputSurface;
                                                                                            z7 = true;
                                                                                            this.extractor.unselectTrack(i13);
                                                                                            if (mediaCodec7 != null) {
                                                                                            }
                                                                                            mediaCodec3 = mediaCodec7;
                                                                                            outputSurface2 = outputSurface4;
                                                                                            if (outputSurface2 != null) {
                                                                                            }
                                                                                            if (inputSurface2 != null) {
                                                                                            }
                                                                                            if (mediaCodec4 != null) {
                                                                                            }
                                                                                            if (audioRecoder != null) {
                                                                                            }
                                                                                            checkConversionCanceled();
                                                                                            mediaCodec5 = mediaCodec3;
                                                                                            i4 = i8;
                                                                                            i74 = i7;
                                                                                            z6 = z5;
                                                                                            int i88222222222 = i9;
                                                                                            outputSurface3 = outputSurface2;
                                                                                            i10 = i88222222222;
                                                                                            mediaExtractor2 = this.extractor;
                                                                                            if (mediaExtractor2 != null) {
                                                                                            }
                                                                                            mP4Builder2 = this.mediaMuxer;
                                                                                            if (mP4Builder2 != null) {
                                                                                            }
                                                                                            if (mediaCodec4 != null) {
                                                                                            }
                                                                                            if (mediaCodec5 != null) {
                                                                                            }
                                                                                            if (outputSurface3 != null) {
                                                                                            }
                                                                                            if (inputSurface2 != null) {
                                                                                            }
                                                                                            str3 = str2;
                                                                                            i75 = i4;
                                                                                            if (z6) {
                                                                                            }
                                                                                        }
                                                                                    } catch (Throwable th48) {
                                                                                        th = th48;
                                                                                        z2 = z;
                                                                                        th = th;
                                                                                        mediaCodec = mediaCodec7;
                                                                                        outputSurface = outputSurface4;
                                                                                        mediaCodec2 = mediaCodec4;
                                                                                        i74 = i15;
                                                                                        i77 = i27;
                                                                                        str = "x";
                                                                                        i75 = i14;
                                                                                        i2 = i29;
                                                                                        inputSurface = inputSurface2;
                                                                                        i3 = -5;
                                                                                        z5 = false;
                                                                                        FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                        FileLog.e(th);
                                                                                        mediaExtractor = this.extractor;
                                                                                        if (mediaExtractor != null) {
                                                                                        }
                                                                                        mP4Builder = this.mediaMuxer;
                                                                                        if (mP4Builder != null) {
                                                                                        }
                                                                                        if (mediaCodec2 != null) {
                                                                                        }
                                                                                        if (mediaCodec != null) {
                                                                                        }
                                                                                        if (outputSurface != null) {
                                                                                        }
                                                                                        if (inputSurface != null) {
                                                                                        }
                                                                                        str3 = str2;
                                                                                        z6 = z5;
                                                                                        z7 = true;
                                                                                        if (z6) {
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    inputBuffers = null;
                                                                                    outputBuffers = null;
                                                                                }
                                                                                ?? mP4Builder4 = new MP4Builder();
                                                                                ?? equals = this.outputMimeType.equals(obj2);
                                                                                this.mediaMuxer = mP4Builder4.createMovie(mp4Movie2, z10, equals);
                                                                                if (i30 < 0) {
                                                                                    try {
                                                                                        MediaFormat trackFormat2 = this.extractor.getTrackFormat(i30);
                                                                                        if (Math.abs(f2 - 1.0f) < 0.001f) {
                                                                                            convertVideoParams2 = convertVideoParams;
                                                                                            if (convertVideoParams2.soundInfos.isEmpty()) {
                                                                                                str10 = str9;
                                                                                                if (!trackFormat2.getString(str10).equals(MediaController.AUDIO_MIME_TYPE)) {
                                                                                                }
                                                                                                r10 = 1;
                                                                                                if (trackFormat2.getString(str10).equals("audio/unknown")) {
                                                                                                    i30 = -1;
                                                                                                }
                                                                                                if (i30 >= 0) {
                                                                                                    mediaCodec8 = r10;
                                                                                                    j12 = j11;
                                                                                                    i35 = -5;
                                                                                                    audioRecoder2 = null;
                                                                                                } else if (r10 != 0) {
                                                                                                    int addTrack = this.mediaMuxer.addTrack(trackFormat2, true);
                                                                                                    this.extractor.selectTrack(i30);
                                                                                                    try {
                                                                                                        i36 = trackFormat2.getInteger("max-input-size");
                                                                                                    } catch (Exception e36) {
                                                                                                        FileLog.e(e36);
                                                                                                        i36 = 0;
                                                                                                    }
                                                                                                    if (i36 <= 0) {
                                                                                                        i36 = 65536;
                                                                                                    }
                                                                                                    ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i36);
                                                                                                    j12 = j11;
                                                                                                    if (j12 > 0) {
                                                                                                        this.extractor.seekTo(j12, 0);
                                                                                                        byteBuffer = allocateDirect;
                                                                                                        i38 = addTrack;
                                                                                                        mediaCodec8 = r10;
                                                                                                    } else {
                                                                                                        byteBuffer = allocateDirect;
                                                                                                        i38 = addTrack;
                                                                                                        mediaCodec8 = r10;
                                                                                                        this.extractor.seekTo(0L, 0);
                                                                                                    }
                                                                                                    i35 = i38;
                                                                                                    audioRecoder2 = null;
                                                                                                    i37 = i36;
                                                                                                    mediaCodec6 = mediaCodec8;
                                                                                                } else {
                                                                                                    mediaCodec8 = r10;
                                                                                                    j12 = j11;
                                                                                                    ArrayList arrayList3 = new ArrayList();
                                                                                                    GeneralAudioInput generalAudioInput = new GeneralAudioInput(str14, i30);
                                                                                                    if (j35 > 0) {
                                                                                                        generalAudioInput.setEndTimeUs(j35);
                                                                                                    }
                                                                                                    if (j12 > 0) {
                                                                                                        generalAudioInput.setStartTimeUs(j12);
                                                                                                    }
                                                                                                    generalAudioInput.setVolume(f2);
                                                                                                    arrayList3.add(generalAudioInput);
                                                                                                    applyAudioInputs(convertVideoParams2.soundInfos, arrayList3);
                                                                                                    j35 = j35;
                                                                                                    audioRecoder2 = new AudioRecoder(arrayList3, j8);
                                                                                                    i35 = this.mediaMuxer.addTrack(audioRecoder2.format, true);
                                                                                                }
                                                                                                i36 = 0;
                                                                                                byteBuffer = null;
                                                                                                i37 = i36;
                                                                                                mediaCodec6 = mediaCodec8;
                                                                                            } else {
                                                                                                str10 = str9;
                                                                                            }
                                                                                        } else {
                                                                                            convertVideoParams2 = convertVideoParams;
                                                                                            str10 = str9;
                                                                                        }
                                                                                        r10 = 0;
                                                                                        if (trackFormat2.getString(str10).equals("audio/unknown")) {
                                                                                        }
                                                                                        if (i30 >= 0) {
                                                                                        }
                                                                                        i36 = 0;
                                                                                        byteBuffer = null;
                                                                                        i37 = i36;
                                                                                        mediaCodec6 = mediaCodec8;
                                                                                    } catch (Exception e37) {
                                                                                        e = e37;
                                                                                        exc = e;
                                                                                        mediaCodec6 = mediaCodec7;
                                                                                        outputSurface = outputSurface4;
                                                                                        i77 = i27;
                                                                                        i2 = i29;
                                                                                        i16 = -5;
                                                                                        audioRecoder = null;
                                                                                        if (exc instanceof IllegalStateException) {
                                                                                        }
                                                                                        z5 = false;
                                                                                        StringBuilder sb52222222222 = new StringBuilder();
                                                                                        sb52222222222.append("bitrate: ");
                                                                                        sb52222222222.append(i77);
                                                                                        sb52222222222.append(" framerate: ");
                                                                                        sb52222222222.append(i2);
                                                                                        sb52222222222.append(" size: ");
                                                                                        i8 = i14;
                                                                                        sb52222222222.append(i8);
                                                                                        str = "x";
                                                                                        sb52222222222.append(str);
                                                                                        i7 = i15;
                                                                                        sb52222222222.append(i7);
                                                                                        FileLog.e(sb52222222222.toString());
                                                                                        FileLog.e(exc);
                                                                                        i9 = i16;
                                                                                        mediaCodec7 = mediaCodec6;
                                                                                        outputSurface4 = outputSurface;
                                                                                        z7 = true;
                                                                                        this.extractor.unselectTrack(i13);
                                                                                        if (mediaCodec7 != null) {
                                                                                        }
                                                                                        mediaCodec3 = mediaCodec7;
                                                                                        outputSurface2 = outputSurface4;
                                                                                        if (outputSurface2 != null) {
                                                                                        }
                                                                                        if (inputSurface2 != null) {
                                                                                        }
                                                                                        if (mediaCodec4 != null) {
                                                                                        }
                                                                                        if (audioRecoder != null) {
                                                                                        }
                                                                                        checkConversionCanceled();
                                                                                        mediaCodec5 = mediaCodec3;
                                                                                        i4 = i8;
                                                                                        i74 = i7;
                                                                                        z6 = z5;
                                                                                        int i882222222222 = i9;
                                                                                        outputSurface3 = outputSurface2;
                                                                                        i10 = i882222222222;
                                                                                        mediaExtractor2 = this.extractor;
                                                                                        if (mediaExtractor2 != null) {
                                                                                        }
                                                                                        mP4Builder2 = this.mediaMuxer;
                                                                                        if (mP4Builder2 != null) {
                                                                                        }
                                                                                        if (mediaCodec4 != null) {
                                                                                        }
                                                                                        if (mediaCodec5 != null) {
                                                                                        }
                                                                                        if (outputSurface3 != null) {
                                                                                        }
                                                                                        if (inputSurface2 != null) {
                                                                                        }
                                                                                        str3 = str2;
                                                                                        i75 = i4;
                                                                                        if (z6) {
                                                                                        }
                                                                                    } catch (Throwable th49) {
                                                                                        th = th49;
                                                                                        z2 = z;
                                                                                        th = th;
                                                                                        mediaCodec = mediaCodec7;
                                                                                        outputSurface = outputSurface4;
                                                                                        mediaCodec2 = mediaCodec4;
                                                                                        i74 = i15;
                                                                                        i77 = i27;
                                                                                        str = "x";
                                                                                        i75 = i14;
                                                                                        i2 = i29;
                                                                                        inputSurface = inputSurface2;
                                                                                        i3 = -5;
                                                                                        z5 = false;
                                                                                        FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                        FileLog.e(th);
                                                                                        mediaExtractor = this.extractor;
                                                                                        if (mediaExtractor != null) {
                                                                                        }
                                                                                        mP4Builder = this.mediaMuxer;
                                                                                        if (mP4Builder != null) {
                                                                                        }
                                                                                        if (mediaCodec2 != null) {
                                                                                        }
                                                                                        if (mediaCodec != null) {
                                                                                        }
                                                                                        if (outputSurface != null) {
                                                                                        }
                                                                                        if (inputSurface != null) {
                                                                                        }
                                                                                        str3 = str2;
                                                                                        z6 = z5;
                                                                                        z7 = true;
                                                                                        if (z6) {
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    long j37 = j8;
                                                                                    j12 = j11;
                                                                                    if (convertVideoParams.soundInfos.isEmpty()) {
                                                                                        i35 = -5;
                                                                                        audioRecoder2 = null;
                                                                                        i37 = 0;
                                                                                        r12 = 1;
                                                                                    } else {
                                                                                        ArrayList arrayList4 = new ArrayList();
                                                                                        arrayList4.add(new BlankAudioInput(j37));
                                                                                        applyAudioInputs(convertVideoParams.soundInfos, arrayList4);
                                                                                        audioRecoder2 = new AudioRecoder(arrayList4, j37);
                                                                                        i35 = this.mediaMuxer.addTrack(audioRecoder2.format, true);
                                                                                        i37 = 0;
                                                                                        r12 = 0;
                                                                                    }
                                                                                    byteBuffer = null;
                                                                                    mediaCodec6 = r12;
                                                                                }
                                                                                boolean z49 = audioRecoder2 != null;
                                                                                checkConversionCanceled();
                                                                                byteBufferArr = outputBuffers;
                                                                                i39 = i37;
                                                                                z11 = z49;
                                                                                byteBuffer2 = byteBuffer;
                                                                                j13 = -2147483648L;
                                                                                j14 = -1;
                                                                                j15 = -1;
                                                                                j16 = 0;
                                                                                i9 = -5;
                                                                                j17 = 0;
                                                                                z12 = false;
                                                                                z13 = false;
                                                                                z14 = false;
                                                                                i40 = 0;
                                                                                z15 = true;
                                                                                i41 = i34;
                                                                            } catch (Exception e38) {
                                                                                e = e38;
                                                                                exc = e;
                                                                                outputSurface = outputSurface6;
                                                                                i77 = i27;
                                                                                i2 = i29;
                                                                                i16 = -5;
                                                                                mediaCodec6 = null;
                                                                                audioRecoder = null;
                                                                                if (exc instanceof IllegalStateException) {
                                                                                }
                                                                                z5 = false;
                                                                                StringBuilder sb522222222222 = new StringBuilder();
                                                                                sb522222222222.append("bitrate: ");
                                                                                sb522222222222.append(i77);
                                                                                sb522222222222.append(" framerate: ");
                                                                                sb522222222222.append(i2);
                                                                                sb522222222222.append(" size: ");
                                                                                i8 = i14;
                                                                                sb522222222222.append(i8);
                                                                                str = "x";
                                                                                sb522222222222.append(str);
                                                                                i7 = i15;
                                                                                sb522222222222.append(i7);
                                                                                FileLog.e(sb522222222222.toString());
                                                                                FileLog.e(exc);
                                                                                i9 = i16;
                                                                                mediaCodec7 = mediaCodec6;
                                                                                outputSurface4 = outputSurface;
                                                                                z7 = true;
                                                                                this.extractor.unselectTrack(i13);
                                                                                if (mediaCodec7 != null) {
                                                                                }
                                                                                mediaCodec3 = mediaCodec7;
                                                                                outputSurface2 = outputSurface4;
                                                                                if (outputSurface2 != null) {
                                                                                }
                                                                                if (inputSurface2 != null) {
                                                                                }
                                                                                if (mediaCodec4 != null) {
                                                                                }
                                                                                if (audioRecoder != null) {
                                                                                }
                                                                                checkConversionCanceled();
                                                                                mediaCodec5 = mediaCodec3;
                                                                                i4 = i8;
                                                                                i74 = i7;
                                                                                z6 = z5;
                                                                                int i8822222222222 = i9;
                                                                                outputSurface3 = outputSurface2;
                                                                                i10 = i8822222222222;
                                                                                mediaExtractor2 = this.extractor;
                                                                                if (mediaExtractor2 != null) {
                                                                                }
                                                                                mP4Builder2 = this.mediaMuxer;
                                                                                if (mP4Builder2 != null) {
                                                                                }
                                                                                if (mediaCodec4 != null) {
                                                                                }
                                                                                if (mediaCodec5 != null) {
                                                                                }
                                                                                if (outputSurface3 != null) {
                                                                                }
                                                                                if (inputSurface2 != null) {
                                                                                }
                                                                                str3 = str2;
                                                                                i75 = i4;
                                                                                if (z6) {
                                                                                }
                                                                            } catch (Throwable th50) {
                                                                                z2 = z;
                                                                                th = th50;
                                                                                outputSurface = outputSurface6;
                                                                                mediaCodec2 = mediaCodec4;
                                                                                i74 = i15;
                                                                                i77 = i27;
                                                                                str = "x";
                                                                                i75 = i14;
                                                                                i2 = i29;
                                                                                inputSurface = inputSurface2;
                                                                                i3 = -5;
                                                                                mediaCodec = null;
                                                                                z5 = false;
                                                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                FileLog.e(th);
                                                                                mediaExtractor = this.extractor;
                                                                                if (mediaExtractor != null) {
                                                                                }
                                                                                mP4Builder = this.mediaMuxer;
                                                                                if (mP4Builder != null) {
                                                                                }
                                                                                if (mediaCodec2 != null) {
                                                                                }
                                                                                if (mediaCodec != null) {
                                                                                }
                                                                                if (outputSurface != null) {
                                                                                }
                                                                                if (inputSurface != null) {
                                                                                }
                                                                                str3 = str2;
                                                                                z6 = z5;
                                                                                z7 = true;
                                                                                if (z6) {
                                                                                }
                                                                            }
                                                                            while (true) {
                                                                                if (z12 || (mediaCodec6 == null && !z11)) {
                                                                                    try {
                                                                                        checkConversionCanceled();
                                                                                        if (audioRecoder2 == null) {
                                                                                            i42 = i9;
                                                                                            try {
                                                                                                z16 = audioRecoder2.step(this.mediaMuxer, i35);
                                                                                            } catch (Exception e39) {
                                                                                                exc = e39;
                                                                                                mediaCodec6 = mediaCodec7;
                                                                                                audioRecoder = audioRecoder2;
                                                                                                outputSurface = outputSurface4;
                                                                                                i16 = i42;
                                                                                                i77 = i27;
                                                                                                i2 = i29;
                                                                                                if (exc instanceof IllegalStateException) {
                                                                                                }
                                                                                                z5 = false;
                                                                                                StringBuilder sb5222222222222 = new StringBuilder();
                                                                                                sb5222222222222.append("bitrate: ");
                                                                                                sb5222222222222.append(i77);
                                                                                                sb5222222222222.append(" framerate: ");
                                                                                                sb5222222222222.append(i2);
                                                                                                sb5222222222222.append(" size: ");
                                                                                                i8 = i14;
                                                                                                sb5222222222222.append(i8);
                                                                                                str = "x";
                                                                                                sb5222222222222.append(str);
                                                                                                i7 = i15;
                                                                                                sb5222222222222.append(i7);
                                                                                                FileLog.e(sb5222222222222.toString());
                                                                                                FileLog.e(exc);
                                                                                                i9 = i16;
                                                                                                mediaCodec7 = mediaCodec6;
                                                                                                outputSurface4 = outputSurface;
                                                                                                z7 = true;
                                                                                                this.extractor.unselectTrack(i13);
                                                                                                if (mediaCodec7 != null) {
                                                                                                }
                                                                                                mediaCodec3 = mediaCodec7;
                                                                                                outputSurface2 = outputSurface4;
                                                                                                if (outputSurface2 != null) {
                                                                                                }
                                                                                                if (inputSurface2 != null) {
                                                                                                }
                                                                                                if (mediaCodec4 != null) {
                                                                                                }
                                                                                                if (audioRecoder != null) {
                                                                                                }
                                                                                                checkConversionCanceled();
                                                                                                mediaCodec5 = mediaCodec3;
                                                                                                i4 = i8;
                                                                                                i74 = i7;
                                                                                                z6 = z5;
                                                                                                int i88222222222222 = i9;
                                                                                                outputSurface3 = outputSurface2;
                                                                                                i10 = i88222222222222;
                                                                                                mediaExtractor2 = this.extractor;
                                                                                                if (mediaExtractor2 != null) {
                                                                                                }
                                                                                                mP4Builder2 = this.mediaMuxer;
                                                                                                if (mP4Builder2 != null) {
                                                                                                }
                                                                                                if (mediaCodec4 != null) {
                                                                                                }
                                                                                                if (mediaCodec5 != null) {
                                                                                                }
                                                                                                if (outputSurface3 != null) {
                                                                                                }
                                                                                                if (inputSurface2 != null) {
                                                                                                }
                                                                                                str3 = str2;
                                                                                                i75 = i4;
                                                                                                if (z6) {
                                                                                                }
                                                                                            } catch (Throwable th51) {
                                                                                                z2 = z;
                                                                                                th = th51;
                                                                                                mediaCodec = mediaCodec7;
                                                                                                outputSurface = outputSurface4;
                                                                                                mediaCodec2 = mediaCodec4;
                                                                                                i74 = i15;
                                                                                                i3 = i42;
                                                                                                i77 = i27;
                                                                                                str = "x";
                                                                                                i75 = i14;
                                                                                                i2 = i29;
                                                                                                inputSurface = inputSurface2;
                                                                                                z5 = false;
                                                                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                FileLog.e(th);
                                                                                                mediaExtractor = this.extractor;
                                                                                                if (mediaExtractor != null) {
                                                                                                }
                                                                                                mP4Builder = this.mediaMuxer;
                                                                                                if (mP4Builder != null) {
                                                                                                }
                                                                                                if (mediaCodec2 != null) {
                                                                                                }
                                                                                                if (mediaCodec != null) {
                                                                                                }
                                                                                                if (outputSurface != null) {
                                                                                                }
                                                                                                if (inputSurface != null) {
                                                                                                }
                                                                                                str3 = str2;
                                                                                                z6 = z5;
                                                                                                z7 = true;
                                                                                                if (z6) {
                                                                                                }
                                                                                            }
                                                                                        } else {
                                                                                            i42 = i9;
                                                                                            z16 = z11;
                                                                                        }
                                                                                        if (z13) {
                                                                                            audioRecoder = audioRecoder2;
                                                                                            try {
                                                                                                int sampleTrackIndex = this.extractor.getSampleTrackIndex();
                                                                                                z17 = z16;
                                                                                                int i91 = i13;
                                                                                                if (sampleTrackIndex == i91) {
                                                                                                    i13 = i91;
                                                                                                    outputSurface = outputSurface4;
                                                                                                    try {
                                                                                                        int dequeueInputBuffer = mediaCodec7.dequeueInputBuffer(2500L);
                                                                                                        if (dequeueInputBuffer >= 0) {
                                                                                                            if (Build.VERSION.SDK_INT < 21) {
                                                                                                                inputBuffer = inputBuffers[dequeueInputBuffer];
                                                                                                            } else {
                                                                                                                inputBuffer = mediaCodec7.getInputBuffer(dequeueInputBuffer);
                                                                                                            }
                                                                                                            byteBufferArr2 = inputBuffers;
                                                                                                            int readSampleData = this.extractor.readSampleData(inputBuffer, 0);
                                                                                                            if (readSampleData < 0) {
                                                                                                                mediaCodec7.queueInputBuffer(dequeueInputBuffer, 0, 0, 0L, 4);
                                                                                                                z13 = true;
                                                                                                            } else {
                                                                                                                mediaCodec7.queueInputBuffer(dequeueInputBuffer, 0, readSampleData, this.extractor.getSampleTime(), 0);
                                                                                                                this.extractor.advance();
                                                                                                            }
                                                                                                        } else {
                                                                                                            byteBufferArr2 = inputBuffers;
                                                                                                        }
                                                                                                        i44 = i30;
                                                                                                        mediaCodec10 = mediaCodec7;
                                                                                                        bufferInfo4 = bufferInfo3;
                                                                                                        i43 = i35;
                                                                                                    } catch (Exception e40) {
                                                                                                        exc = e40;
                                                                                                        mediaCodec6 = mediaCodec7;
                                                                                                        i16 = i42;
                                                                                                        i77 = i27;
                                                                                                        i2 = i29;
                                                                                                        if (exc instanceof IllegalStateException) {
                                                                                                        }
                                                                                                        z5 = false;
                                                                                                        StringBuilder sb52222222222222 = new StringBuilder();
                                                                                                        sb52222222222222.append("bitrate: ");
                                                                                                        sb52222222222222.append(i77);
                                                                                                        sb52222222222222.append(" framerate: ");
                                                                                                        sb52222222222222.append(i2);
                                                                                                        sb52222222222222.append(" size: ");
                                                                                                        i8 = i14;
                                                                                                        sb52222222222222.append(i8);
                                                                                                        str = "x";
                                                                                                        sb52222222222222.append(str);
                                                                                                        i7 = i15;
                                                                                                        sb52222222222222.append(i7);
                                                                                                        FileLog.e(sb52222222222222.toString());
                                                                                                        FileLog.e(exc);
                                                                                                        i9 = i16;
                                                                                                        mediaCodec7 = mediaCodec6;
                                                                                                        outputSurface4 = outputSurface;
                                                                                                        z7 = true;
                                                                                                        this.extractor.unselectTrack(i13);
                                                                                                        if (mediaCodec7 != null) {
                                                                                                        }
                                                                                                        mediaCodec3 = mediaCodec7;
                                                                                                        outputSurface2 = outputSurface4;
                                                                                                        if (outputSurface2 != null) {
                                                                                                        }
                                                                                                        if (inputSurface2 != null) {
                                                                                                        }
                                                                                                        if (mediaCodec4 != null) {
                                                                                                        }
                                                                                                        if (audioRecoder != null) {
                                                                                                        }
                                                                                                        checkConversionCanceled();
                                                                                                        mediaCodec5 = mediaCodec3;
                                                                                                        i4 = i8;
                                                                                                        i74 = i7;
                                                                                                        z6 = z5;
                                                                                                        int i882222222222222 = i9;
                                                                                                        outputSurface3 = outputSurface2;
                                                                                                        i10 = i882222222222222;
                                                                                                        mediaExtractor2 = this.extractor;
                                                                                                        if (mediaExtractor2 != null) {
                                                                                                        }
                                                                                                        mP4Builder2 = this.mediaMuxer;
                                                                                                        if (mP4Builder2 != null) {
                                                                                                        }
                                                                                                        if (mediaCodec4 != null) {
                                                                                                        }
                                                                                                        if (mediaCodec5 != null) {
                                                                                                        }
                                                                                                        if (outputSurface3 != null) {
                                                                                                        }
                                                                                                        if (inputSurface2 != null) {
                                                                                                        }
                                                                                                        str3 = str2;
                                                                                                        i75 = i4;
                                                                                                        if (z6) {
                                                                                                        }
                                                                                                    } catch (Throwable th52) {
                                                                                                        z2 = z;
                                                                                                        th = th52;
                                                                                                        mediaCodec = mediaCodec7;
                                                                                                        mediaCodec2 = mediaCodec4;
                                                                                                        i74 = i15;
                                                                                                        i3 = i42;
                                                                                                        i77 = i27;
                                                                                                        str = "x";
                                                                                                        i75 = i14;
                                                                                                        i2 = i29;
                                                                                                        inputSurface = inputSurface2;
                                                                                                        z5 = false;
                                                                                                        FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                        FileLog.e(th);
                                                                                                        mediaExtractor = this.extractor;
                                                                                                        if (mediaExtractor != null) {
                                                                                                        }
                                                                                                        mP4Builder = this.mediaMuxer;
                                                                                                        if (mP4Builder != null) {
                                                                                                        }
                                                                                                        if (mediaCodec2 != null) {
                                                                                                        }
                                                                                                        if (mediaCodec != null) {
                                                                                                        }
                                                                                                        if (outputSurface != null) {
                                                                                                        }
                                                                                                        if (inputSurface != null) {
                                                                                                        }
                                                                                                        str3 = str2;
                                                                                                        z6 = z5;
                                                                                                        z7 = true;
                                                                                                        if (z6) {
                                                                                                        }
                                                                                                    }
                                                                                                } else {
                                                                                                    byteBufferArr2 = inputBuffers;
                                                                                                    i13 = i91;
                                                                                                    outputSurface = outputSurface4;
                                                                                                    if (mediaCodec6 == null || i30 == -1 || sampleTrackIndex != i30) {
                                                                                                        i44 = i30;
                                                                                                        mediaCodec10 = mediaCodec7;
                                                                                                        bufferInfo4 = bufferInfo3;
                                                                                                        i43 = i35;
                                                                                                        if (sampleTrackIndex == -1) {
                                                                                                            z29 = true;
                                                                                                            if (z29) {
                                                                                                                mediaCodec9 = mediaCodec10;
                                                                                                            } else {
                                                                                                                mediaCodec9 = mediaCodec10;
                                                                                                                try {
                                                                                                                    int dequeueInputBuffer2 = mediaCodec9.dequeueInputBuffer(2500L);
                                                                                                                    if (dequeueInputBuffer2 >= 0) {
                                                                                                                        mediaCodec9.queueInputBuffer(dequeueInputBuffer2, 0, 0, 0L, 4);
                                                                                                                        z13 = true;
                                                                                                                    }
                                                                                                                } catch (Exception e41) {
                                                                                                                    e = e41;
                                                                                                                    mediaCodec6 = mediaCodec9;
                                                                                                                    i16 = i42;
                                                                                                                    i77 = i27;
                                                                                                                    i2 = i29;
                                                                                                                    exc = e;
                                                                                                                    if (exc instanceof IllegalStateException) {
                                                                                                                    }
                                                                                                                    z5 = false;
                                                                                                                    StringBuilder sb522222222222222 = new StringBuilder();
                                                                                                                    sb522222222222222.append("bitrate: ");
                                                                                                                    sb522222222222222.append(i77);
                                                                                                                    sb522222222222222.append(" framerate: ");
                                                                                                                    sb522222222222222.append(i2);
                                                                                                                    sb522222222222222.append(" size: ");
                                                                                                                    i8 = i14;
                                                                                                                    sb522222222222222.append(i8);
                                                                                                                    str = "x";
                                                                                                                    sb522222222222222.append(str);
                                                                                                                    i7 = i15;
                                                                                                                    sb522222222222222.append(i7);
                                                                                                                    FileLog.e(sb522222222222222.toString());
                                                                                                                    FileLog.e(exc);
                                                                                                                    i9 = i16;
                                                                                                                    mediaCodec7 = mediaCodec6;
                                                                                                                    outputSurface4 = outputSurface;
                                                                                                                    z7 = true;
                                                                                                                    this.extractor.unselectTrack(i13);
                                                                                                                    if (mediaCodec7 != null) {
                                                                                                                    }
                                                                                                                    mediaCodec3 = mediaCodec7;
                                                                                                                    outputSurface2 = outputSurface4;
                                                                                                                    if (outputSurface2 != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface2 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                    }
                                                                                                                    if (audioRecoder != null) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    mediaCodec5 = mediaCodec3;
                                                                                                                    i4 = i8;
                                                                                                                    i74 = i7;
                                                                                                                    z6 = z5;
                                                                                                                    int i8822222222222222 = i9;
                                                                                                                    outputSurface3 = outputSurface2;
                                                                                                                    i10 = i8822222222222222;
                                                                                                                    mediaExtractor2 = this.extractor;
                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                    }
                                                                                                                    mP4Builder2 = this.mediaMuxer;
                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec5 != null) {
                                                                                                                    }
                                                                                                                    if (outputSurface3 != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface2 != null) {
                                                                                                                    }
                                                                                                                    str3 = str2;
                                                                                                                    i75 = i4;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                } catch (Throwable th53) {
                                                                                                                    th = th53;
                                                                                                                    z2 = z;
                                                                                                                    th = th;
                                                                                                                    mediaCodec = mediaCodec9;
                                                                                                                    mediaCodec2 = mediaCodec4;
                                                                                                                    i74 = i15;
                                                                                                                    i3 = i42;
                                                                                                                    i77 = i27;
                                                                                                                    str = "x";
                                                                                                                    i75 = i14;
                                                                                                                    i2 = i29;
                                                                                                                    inputSurface = inputSurface2;
                                                                                                                    z5 = false;
                                                                                                                    FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                                    FileLog.e(th);
                                                                                                                    mediaExtractor = this.extractor;
                                                                                                                    if (mediaExtractor != null) {
                                                                                                                    }
                                                                                                                    mP4Builder = this.mediaMuxer;
                                                                                                                    if (mP4Builder != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec2 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec != null) {
                                                                                                                    }
                                                                                                                    if (outputSurface != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface != null) {
                                                                                                                    }
                                                                                                                    str3 = str2;
                                                                                                                    z6 = z5;
                                                                                                                    z7 = true;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    } else {
                                                                                                        try {
                                                                                                            int i92 = Build.VERSION.SDK_INT;
                                                                                                            if (i92 >= 28) {
                                                                                                                long sampleSize = this.extractor.getSampleSize();
                                                                                                                mediaCodec10 = mediaCodec7;
                                                                                                                if (sampleSize > i39) {
                                                                                                                    i39 = (int) (sampleSize + 1024);
                                                                                                                    try {
                                                                                                                        byteBuffer2 = ByteBuffer.allocateDirect(i39);
                                                                                                                    } catch (Exception e42) {
                                                                                                                        e = e42;
                                                                                                                        exc = e;
                                                                                                                        i16 = i42;
                                                                                                                        i77 = i27;
                                                                                                                        mediaCodec6 = mediaCodec10;
                                                                                                                        i2 = i29;
                                                                                                                        if (exc instanceof IllegalStateException) {
                                                                                                                        }
                                                                                                                        z5 = false;
                                                                                                                        StringBuilder sb5222222222222222 = new StringBuilder();
                                                                                                                        sb5222222222222222.append("bitrate: ");
                                                                                                                        sb5222222222222222.append(i77);
                                                                                                                        sb5222222222222222.append(" framerate: ");
                                                                                                                        sb5222222222222222.append(i2);
                                                                                                                        sb5222222222222222.append(" size: ");
                                                                                                                        i8 = i14;
                                                                                                                        sb5222222222222222.append(i8);
                                                                                                                        str = "x";
                                                                                                                        sb5222222222222222.append(str);
                                                                                                                        i7 = i15;
                                                                                                                        sb5222222222222222.append(i7);
                                                                                                                        FileLog.e(sb5222222222222222.toString());
                                                                                                                        FileLog.e(exc);
                                                                                                                        i9 = i16;
                                                                                                                        mediaCodec7 = mediaCodec6;
                                                                                                                        outputSurface4 = outputSurface;
                                                                                                                        z7 = true;
                                                                                                                        this.extractor.unselectTrack(i13);
                                                                                                                        if (mediaCodec7 != null) {
                                                                                                                        }
                                                                                                                        mediaCodec3 = mediaCodec7;
                                                                                                                        outputSurface2 = outputSurface4;
                                                                                                                        if (outputSurface2 != null) {
                                                                                                                        }
                                                                                                                        if (inputSurface2 != null) {
                                                                                                                        }
                                                                                                                        if (mediaCodec4 != null) {
                                                                                                                        }
                                                                                                                        if (audioRecoder != null) {
                                                                                                                        }
                                                                                                                        checkConversionCanceled();
                                                                                                                        mediaCodec5 = mediaCodec3;
                                                                                                                        i4 = i8;
                                                                                                                        i74 = i7;
                                                                                                                        z6 = z5;
                                                                                                                        int i88222222222222222 = i9;
                                                                                                                        outputSurface3 = outputSurface2;
                                                                                                                        i10 = i88222222222222222;
                                                                                                                        mediaExtractor2 = this.extractor;
                                                                                                                        if (mediaExtractor2 != null) {
                                                                                                                        }
                                                                                                                        mP4Builder2 = this.mediaMuxer;
                                                                                                                        if (mP4Builder2 != null) {
                                                                                                                        }
                                                                                                                        if (mediaCodec4 != null) {
                                                                                                                        }
                                                                                                                        if (mediaCodec5 != null) {
                                                                                                                        }
                                                                                                                        if (outputSurface3 != null) {
                                                                                                                        }
                                                                                                                        if (inputSurface2 != null) {
                                                                                                                        }
                                                                                                                        str3 = str2;
                                                                                                                        i75 = i4;
                                                                                                                        if (z6) {
                                                                                                                        }
                                                                                                                    } catch (Throwable th54) {
                                                                                                                        th = th54;
                                                                                                                        z2 = z;
                                                                                                                        th = th;
                                                                                                                        mediaCodec2 = mediaCodec4;
                                                                                                                        i74 = i15;
                                                                                                                        i3 = i42;
                                                                                                                        i77 = i27;
                                                                                                                        str = "x";
                                                                                                                        mediaCodec = mediaCodec10;
                                                                                                                        i75 = i14;
                                                                                                                        i2 = i29;
                                                                                                                        inputSurface = inputSurface2;
                                                                                                                        z5 = false;
                                                                                                                        FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                                        FileLog.e(th);
                                                                                                                        mediaExtractor = this.extractor;
                                                                                                                        if (mediaExtractor != null) {
                                                                                                                        }
                                                                                                                        mP4Builder = this.mediaMuxer;
                                                                                                                        if (mP4Builder != null) {
                                                                                                                        }
                                                                                                                        if (mediaCodec2 != null) {
                                                                                                                        }
                                                                                                                        if (mediaCodec != null) {
                                                                                                                        }
                                                                                                                        if (outputSurface != null) {
                                                                                                                        }
                                                                                                                        if (inputSurface != null) {
                                                                                                                        }
                                                                                                                        str3 = str2;
                                                                                                                        z6 = z5;
                                                                                                                        z7 = true;
                                                                                                                        if (z6) {
                                                                                                                        }
                                                                                                                    }
                                                                                                                }
                                                                                                            } else {
                                                                                                                mediaCodec10 = mediaCodec7;
                                                                                                            }
                                                                                                            ByteBuffer byteBuffer9 = byteBuffer2;
                                                                                                            bufferInfo4 = bufferInfo3;
                                                                                                            bufferInfo4.size = this.extractor.readSampleData(byteBuffer9, 0);
                                                                                                            if (i92 < 21) {
                                                                                                                byteBuffer9.position(0);
                                                                                                                byteBuffer9.limit(bufferInfo4.size);
                                                                                                            }
                                                                                                            if (bufferInfo4.size >= 0) {
                                                                                                                bufferInfo4.presentationTimeUs = this.extractor.getSampleTime();
                                                                                                                this.extractor.advance();
                                                                                                            } else {
                                                                                                                bufferInfo4.size = 0;
                                                                                                                z13 = true;
                                                                                                            }
                                                                                                            if (bufferInfo4.size > 0 && (j35 < 0 || bufferInfo4.presentationTimeUs < j35)) {
                                                                                                                bufferInfo4.offset = 0;
                                                                                                                bufferInfo4.flags = this.extractor.getSampleFlags();
                                                                                                                long writeSampleData2 = this.mediaMuxer.writeSampleData(i35, byteBuffer9, bufferInfo4, false);
                                                                                                                if (writeSampleData2 != 0 && (videoConvertorListener2 = this.callback) != null) {
                                                                                                                    i43 = i35;
                                                                                                                    i44 = i30;
                                                                                                                    long j38 = bufferInfo4.presentationTimeUs;
                                                                                                                    if (j38 - j12 > j16) {
                                                                                                                        j16 = j38 - j12;
                                                                                                                    }
                                                                                                                    videoConvertorListener2.didWriteData(writeSampleData2, (((float) j16) / 1000.0f) / f);
                                                                                                                    byteBuffer2 = byteBuffer9;
                                                                                                                }
                                                                                                            }
                                                                                                            i43 = i35;
                                                                                                            i44 = i30;
                                                                                                            byteBuffer2 = byteBuffer9;
                                                                                                        } catch (Exception e43) {
                                                                                                            e = e43;
                                                                                                            mediaCodec10 = mediaCodec7;
                                                                                                        } catch (Throwable th55) {
                                                                                                            th = th55;
                                                                                                            mediaCodec10 = mediaCodec7;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                z29 = false;
                                                                                                if (z29) {
                                                                                                }
                                                                                            } catch (Exception e44) {
                                                                                                e = e44;
                                                                                                mediaCodec9 = mediaCodec7;
                                                                                                outputSurface = outputSurface4;
                                                                                            } catch (Throwable th56) {
                                                                                                th = th56;
                                                                                                mediaCodec9 = mediaCodec7;
                                                                                                outputSurface = outputSurface4;
                                                                                            }
                                                                                        } else {
                                                                                            byteBufferArr2 = inputBuffers;
                                                                                            audioRecoder = audioRecoder2;
                                                                                            z17 = z16;
                                                                                            outputSurface = outputSurface4;
                                                                                            bufferInfo4 = bufferInfo3;
                                                                                            i43 = i35;
                                                                                            i44 = i30;
                                                                                            mediaCodec9 = mediaCodec7;
                                                                                        }
                                                                                        z18 = !z14;
                                                                                        j18 = j13;
                                                                                        i45 = i42;
                                                                                        z19 = true;
                                                                                        outputSurface7 = i41;
                                                                                    } catch (Exception e45) {
                                                                                        mediaCodec6 = mediaCodec7;
                                                                                        audioRecoder = audioRecoder2;
                                                                                        i2 = i29;
                                                                                        exc = e45;
                                                                                        outputSurface = outputSurface4;
                                                                                        i16 = i9;
                                                                                    } catch (Throwable th57) {
                                                                                        MediaCodec mediaCodec15 = mediaCodec7;
                                                                                        i2 = i29;
                                                                                        th = th57;
                                                                                        inputSurface = inputSurface2;
                                                                                        outputSurface = outputSurface4;
                                                                                        mediaCodec2 = mediaCodec4;
                                                                                        mediaCodec = mediaCodec15;
                                                                                        i74 = i15;
                                                                                        i3 = i9;
                                                                                        i77 = i27;
                                                                                        str = "x";
                                                                                        i75 = i14;
                                                                                        z5 = false;
                                                                                        z2 = z;
                                                                                        FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                        FileLog.e(th);
                                                                                        mediaExtractor = this.extractor;
                                                                                        if (mediaExtractor != null) {
                                                                                        }
                                                                                        mP4Builder = this.mediaMuxer;
                                                                                        if (mP4Builder != null) {
                                                                                        }
                                                                                        if (mediaCodec2 != null) {
                                                                                        }
                                                                                        if (mediaCodec != null) {
                                                                                        }
                                                                                        if (outputSurface != null) {
                                                                                        }
                                                                                        if (inputSurface != null) {
                                                                                        }
                                                                                        str3 = str2;
                                                                                        z6 = z5;
                                                                                        z7 = true;
                                                                                        if (z6) {
                                                                                        }
                                                                                    }
                                                                                    while (true) {
                                                                                        if (!z18 || z19) {
                                                                                            try {
                                                                                                checkConversionCanceled();
                                                                                                z20 = z18;
                                                                                                if (z) {
                                                                                                    z21 = z19;
                                                                                                    j19 = 2500;
                                                                                                    MediaCodec mediaCodec16 = mediaCodec4;
                                                                                                    i46 = i39;
                                                                                                    mediaCodec11 = mediaCodec16;
                                                                                                } else {
                                                                                                    z21 = z19;
                                                                                                    j19 = 22000;
                                                                                                    MediaCodec mediaCodec17 = mediaCodec4;
                                                                                                    i46 = i39;
                                                                                                    mediaCodec11 = mediaCodec17;
                                                                                                }
                                                                                                try {
                                                                                                    i2 = mediaCodec11.dequeueOutputBuffer(bufferInfo4, j19);
                                                                                                } catch (Exception e46) {
                                                                                                    e = e46;
                                                                                                    mediaCodec6 = mediaCodec9;
                                                                                                } catch (Throwable th58) {
                                                                                                    th = th58;
                                                                                                    mediaCodec6 = mediaCodec9;
                                                                                                }
                                                                                            } catch (Exception e47) {
                                                                                                mediaCodec6 = mediaCodec9;
                                                                                                i2 = i29;
                                                                                                exc = e47;
                                                                                            } catch (Throwable th59) {
                                                                                                th = th59;
                                                                                                mediaCodec6 = mediaCodec9;
                                                                                                mediaCodec11 = mediaCodec4;
                                                                                            }
                                                                                            try {
                                                                                                if (i2 != -1) {
                                                                                                    mediaCodec10 = mediaCodec9;
                                                                                                    i47 = i2;
                                                                                                    j20 = j18;
                                                                                                    mediaCodec13 = mediaCodec6;
                                                                                                    i48 = i44;
                                                                                                    str11 = str6;
                                                                                                    i50 = i31;
                                                                                                    i49 = i32;
                                                                                                    i51 = -1;
                                                                                                    z21 = false;
                                                                                                } else {
                                                                                                    if (i2 == -3) {
                                                                                                        try {
                                                                                                            mediaCodec13 = mediaCodec6;
                                                                                                            if (Build.VERSION.SDK_INT < 21) {
                                                                                                                byteBufferArr = mediaCodec11.getOutputBuffers();
                                                                                                            }
                                                                                                            mediaCodec10 = mediaCodec9;
                                                                                                            i47 = i2;
                                                                                                            j20 = j18;
                                                                                                            i48 = i44;
                                                                                                            str11 = str6;
                                                                                                        } catch (Exception e48) {
                                                                                                            e = e48;
                                                                                                            mediaCodec6 = mediaCodec9;
                                                                                                            mediaCodec4 = mediaCodec11;
                                                                                                            i16 = i45;
                                                                                                            i77 = i27;
                                                                                                            i2 = i29;
                                                                                                            exc = e;
                                                                                                            if (exc instanceof IllegalStateException) {
                                                                                                            }
                                                                                                            z5 = false;
                                                                                                            StringBuilder sb52222222222222222 = new StringBuilder();
                                                                                                            sb52222222222222222.append("bitrate: ");
                                                                                                            sb52222222222222222.append(i77);
                                                                                                            sb52222222222222222.append(" framerate: ");
                                                                                                            sb52222222222222222.append(i2);
                                                                                                            sb52222222222222222.append(" size: ");
                                                                                                            i8 = i14;
                                                                                                            sb52222222222222222.append(i8);
                                                                                                            str = "x";
                                                                                                            sb52222222222222222.append(str);
                                                                                                            i7 = i15;
                                                                                                            sb52222222222222222.append(i7);
                                                                                                            FileLog.e(sb52222222222222222.toString());
                                                                                                            FileLog.e(exc);
                                                                                                            i9 = i16;
                                                                                                            mediaCodec7 = mediaCodec6;
                                                                                                            outputSurface4 = outputSurface;
                                                                                                            z7 = true;
                                                                                                            this.extractor.unselectTrack(i13);
                                                                                                            if (mediaCodec7 != null) {
                                                                                                            }
                                                                                                            mediaCodec3 = mediaCodec7;
                                                                                                            outputSurface2 = outputSurface4;
                                                                                                            if (outputSurface2 != null) {
                                                                                                            }
                                                                                                            if (inputSurface2 != null) {
                                                                                                            }
                                                                                                            if (mediaCodec4 != null) {
                                                                                                            }
                                                                                                            if (audioRecoder != null) {
                                                                                                            }
                                                                                                            checkConversionCanceled();
                                                                                                            mediaCodec5 = mediaCodec3;
                                                                                                            i4 = i8;
                                                                                                            i74 = i7;
                                                                                                            z6 = z5;
                                                                                                            int i882222222222222222 = i9;
                                                                                                            outputSurface3 = outputSurface2;
                                                                                                            i10 = i882222222222222222;
                                                                                                            mediaExtractor2 = this.extractor;
                                                                                                            if (mediaExtractor2 != null) {
                                                                                                            }
                                                                                                            mP4Builder2 = this.mediaMuxer;
                                                                                                            if (mP4Builder2 != null) {
                                                                                                            }
                                                                                                            if (mediaCodec4 != null) {
                                                                                                            }
                                                                                                            if (mediaCodec5 != null) {
                                                                                                            }
                                                                                                            if (outputSurface3 != null) {
                                                                                                            }
                                                                                                            if (inputSurface2 != null) {
                                                                                                            }
                                                                                                            str3 = str2;
                                                                                                            i75 = i4;
                                                                                                            if (z6) {
                                                                                                            }
                                                                                                        } catch (Throwable th60) {
                                                                                                            z2 = z;
                                                                                                            th = th60;
                                                                                                            mediaCodec = mediaCodec9;
                                                                                                            mediaCodec2 = mediaCodec11;
                                                                                                            i3 = i45;
                                                                                                            i74 = i15;
                                                                                                            i77 = i27;
                                                                                                            str = "x";
                                                                                                            i75 = i14;
                                                                                                            i2 = i29;
                                                                                                            inputSurface = inputSurface2;
                                                                                                            z5 = false;
                                                                                                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                            FileLog.e(th);
                                                                                                            mediaExtractor = this.extractor;
                                                                                                            if (mediaExtractor != null) {
                                                                                                            }
                                                                                                            mP4Builder = this.mediaMuxer;
                                                                                                            if (mP4Builder != null) {
                                                                                                            }
                                                                                                            if (mediaCodec2 != null) {
                                                                                                            }
                                                                                                            if (mediaCodec != null) {
                                                                                                            }
                                                                                                            if (outputSurface != null) {
                                                                                                            }
                                                                                                            if (inputSurface != null) {
                                                                                                            }
                                                                                                            str3 = str2;
                                                                                                            z6 = z5;
                                                                                                            z7 = true;
                                                                                                            if (z6) {
                                                                                                            }
                                                                                                        }
                                                                                                    } else {
                                                                                                        mediaCodec13 = mediaCodec6;
                                                                                                        if (i2 == -2) {
                                                                                                            MediaFormat outputFormat2 = mediaCodec11.getOutputFormat();
                                                                                                            if (i45 != -5 || outputFormat2 == null) {
                                                                                                                i48 = i44;
                                                                                                                str11 = str6;
                                                                                                            } else {
                                                                                                                i48 = i44;
                                                                                                                int addTrack2 = this.mediaMuxer.addTrack(outputFormat2, false);
                                                                                                                String str18 = str7;
                                                                                                                try {
                                                                                                                    if (outputFormat2.containsKey(str18)) {
                                                                                                                        i53 = addTrack2;
                                                                                                                        if (outputFormat2.getInteger(str18) == 1) {
                                                                                                                            str11 = str6;
                                                                                                                            try {
                                                                                                                                ByteBuffer byteBuffer10 = outputFormat2.getByteBuffer(str11);
                                                                                                                                str7 = str18;
                                                                                                                                ByteBuffer byteBuffer11 = outputFormat2.getByteBuffer("csd-1");
                                                                                                                                i40 = (byteBuffer10 == null ? 0 : byteBuffer10.limit()) + (byteBuffer11 == null ? 0 : byteBuffer11.limit());
                                                                                                                                i45 = i53;
                                                                                                                            } catch (Exception e49) {
                                                                                                                                e = e49;
                                                                                                                                mediaCodec6 = mediaCodec9;
                                                                                                                                mediaCodec4 = mediaCodec11;
                                                                                                                                i16 = i53;
                                                                                                                                i77 = i27;
                                                                                                                                i2 = i29;
                                                                                                                                exc = e;
                                                                                                                                if (exc instanceof IllegalStateException) {
                                                                                                                                }
                                                                                                                                z5 = false;
                                                                                                                                StringBuilder sb522222222222222222 = new StringBuilder();
                                                                                                                                sb522222222222222222.append("bitrate: ");
                                                                                                                                sb522222222222222222.append(i77);
                                                                                                                                sb522222222222222222.append(" framerate: ");
                                                                                                                                sb522222222222222222.append(i2);
                                                                                                                                sb522222222222222222.append(" size: ");
                                                                                                                                i8 = i14;
                                                                                                                                sb522222222222222222.append(i8);
                                                                                                                                str = "x";
                                                                                                                                sb522222222222222222.append(str);
                                                                                                                                i7 = i15;
                                                                                                                                sb522222222222222222.append(i7);
                                                                                                                                FileLog.e(sb522222222222222222.toString());
                                                                                                                                FileLog.e(exc);
                                                                                                                                i9 = i16;
                                                                                                                                mediaCodec7 = mediaCodec6;
                                                                                                                                outputSurface4 = outputSurface;
                                                                                                                                z7 = true;
                                                                                                                                this.extractor.unselectTrack(i13);
                                                                                                                                if (mediaCodec7 != null) {
                                                                                                                                }
                                                                                                                                mediaCodec3 = mediaCodec7;
                                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                                if (outputSurface2 != null) {
                                                                                                                                }
                                                                                                                                if (inputSurface2 != null) {
                                                                                                                                }
                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                }
                                                                                                                                if (audioRecoder != null) {
                                                                                                                                }
                                                                                                                                checkConversionCanceled();
                                                                                                                                mediaCodec5 = mediaCodec3;
                                                                                                                                i4 = i8;
                                                                                                                                i74 = i7;
                                                                                                                                z6 = z5;
                                                                                                                                int i8822222222222222222 = i9;
                                                                                                                                outputSurface3 = outputSurface2;
                                                                                                                                i10 = i8822222222222222222;
                                                                                                                                mediaExtractor2 = this.extractor;
                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                }
                                                                                                                                mP4Builder2 = this.mediaMuxer;
                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                }
                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                }
                                                                                                                                if (mediaCodec5 != null) {
                                                                                                                                }
                                                                                                                                if (outputSurface3 != null) {
                                                                                                                                }
                                                                                                                                if (inputSurface2 != null) {
                                                                                                                                }
                                                                                                                                str3 = str2;
                                                                                                                                i75 = i4;
                                                                                                                                if (z6) {
                                                                                                                                }
                                                                                                                            } catch (Throwable th61) {
                                                                                                                                th = th61;
                                                                                                                                z2 = z;
                                                                                                                                th = th;
                                                                                                                                mediaCodec = mediaCodec9;
                                                                                                                                mediaCodec2 = mediaCodec11;
                                                                                                                                i3 = i53;
                                                                                                                                i74 = i15;
                                                                                                                                i77 = i27;
                                                                                                                                str = "x";
                                                                                                                                i75 = i14;
                                                                                                                                i2 = i29;
                                                                                                                                inputSurface = inputSurface2;
                                                                                                                                z5 = false;
                                                                                                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                                                FileLog.e(th);
                                                                                                                                mediaExtractor = this.extractor;
                                                                                                                                if (mediaExtractor != null) {
                                                                                                                                }
                                                                                                                                mP4Builder = this.mediaMuxer;
                                                                                                                                if (mP4Builder != null) {
                                                                                                                                }
                                                                                                                                if (mediaCodec2 != null) {
                                                                                                                                }
                                                                                                                                if (mediaCodec != null) {
                                                                                                                                }
                                                                                                                                if (outputSurface != null) {
                                                                                                                                }
                                                                                                                                if (inputSurface != null) {
                                                                                                                                }
                                                                                                                                str3 = str2;
                                                                                                                                z6 = z5;
                                                                                                                                z7 = true;
                                                                                                                                if (z6) {
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        i53 = addTrack2;
                                                                                                                    }
                                                                                                                    str7 = str18;
                                                                                                                    str11 = str6;
                                                                                                                    i45 = i53;
                                                                                                                } catch (Exception e50) {
                                                                                                                    e = e50;
                                                                                                                    i53 = addTrack2;
                                                                                                                } catch (Throwable th62) {
                                                                                                                    th = th62;
                                                                                                                    i53 = addTrack2;
                                                                                                                }
                                                                                                            }
                                                                                                            mediaCodec10 = mediaCodec9;
                                                                                                            i47 = i2;
                                                                                                            j20 = j18;
                                                                                                        } else {
                                                                                                            i48 = i44;
                                                                                                            str11 = str6;
                                                                                                            if (i2 < 0) {
                                                                                                                throw new RuntimeException("unexpected result from encoder.dequeueOutputBuffer: " + i2);
                                                                                                            }
                                                                                                            if (Build.VERSION.SDK_INT < 21) {
                                                                                                                outputBuffer = byteBufferArr[i2];
                                                                                                            } else {
                                                                                                                outputBuffer = mediaCodec11.getOutputBuffer(i2);
                                                                                                            }
                                                                                                            if (outputBuffer == null) {
                                                                                                                throw new RuntimeException("encoderOutputBuffer " + i2 + " was null");
                                                                                                            }
                                                                                                            int i93 = bufferInfo4.size;
                                                                                                            j20 = j18;
                                                                                                            try {
                                                                                                                if (i93 > 1) {
                                                                                                                    try {
                                                                                                                        int i94 = bufferInfo4.flags;
                                                                                                                        if ((i94 & 2) == 0) {
                                                                                                                            if (i40 != 0 && (i94 & 1) != 0) {
                                                                                                                                bufferInfo4.offset += i40;
                                                                                                                                bufferInfo4.size = i93 - i40;
                                                                                                                            }
                                                                                                                            if (!z15 || (i94 & 1) == 0) {
                                                                                                                                z22 = z15;
                                                                                                                            } else {
                                                                                                                                cutOfNalData(this.outputMimeType, outputBuffer, bufferInfo4);
                                                                                                                                z22 = false;
                                                                                                                            }
                                                                                                                            mediaCodec10 = mediaCodec9;
                                                                                                                            i52 = i2;
                                                                                                                            try {
                                                                                                                                long writeSampleData3 = this.mediaMuxer.writeSampleData(i45, outputBuffer, bufferInfo4, true);
                                                                                                                                if (writeSampleData3 != 0 && (videoConvertorListener = this.callback) != null) {
                                                                                                                                    long j39 = bufferInfo4.presentationTimeUs;
                                                                                                                                    if (j39 - j12 > j16) {
                                                                                                                                        j16 = j39 - j12;
                                                                                                                                    }
                                                                                                                                    videoConvertorListener.didWriteData(writeSampleData3, (((float) j16) / 1000.0f) / f);
                                                                                                                                }
                                                                                                                                i16 = i45;
                                                                                                                                z15 = z22;
                                                                                                                                i50 = i31;
                                                                                                                                i49 = i32;
                                                                                                                            } catch (Exception e51) {
                                                                                                                                e = e51;
                                                                                                                                exc = e;
                                                                                                                                mediaCodec4 = mediaCodec11;
                                                                                                                                i16 = i45;
                                                                                                                                i77 = i27;
                                                                                                                                mediaCodec6 = mediaCodec10;
                                                                                                                                i2 = i29;
                                                                                                                                if (exc instanceof IllegalStateException) {
                                                                                                                                }
                                                                                                                                z5 = false;
                                                                                                                                StringBuilder sb5222222222222222222 = new StringBuilder();
                                                                                                                                sb5222222222222222222.append("bitrate: ");
                                                                                                                                sb5222222222222222222.append(i77);
                                                                                                                                sb5222222222222222222.append(" framerate: ");
                                                                                                                                sb5222222222222222222.append(i2);
                                                                                                                                sb5222222222222222222.append(" size: ");
                                                                                                                                i8 = i14;
                                                                                                                                sb5222222222222222222.append(i8);
                                                                                                                                str = "x";
                                                                                                                                sb5222222222222222222.append(str);
                                                                                                                                i7 = i15;
                                                                                                                                sb5222222222222222222.append(i7);
                                                                                                                                FileLog.e(sb5222222222222222222.toString());
                                                                                                                                FileLog.e(exc);
                                                                                                                                i9 = i16;
                                                                                                                                mediaCodec7 = mediaCodec6;
                                                                                                                                outputSurface4 = outputSurface;
                                                                                                                                z7 = true;
                                                                                                                                this.extractor.unselectTrack(i13);
                                                                                                                                if (mediaCodec7 != null) {
                                                                                                                                }
                                                                                                                                mediaCodec3 = mediaCodec7;
                                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                                if (outputSurface2 != null) {
                                                                                                                                }
                                                                                                                                if (inputSurface2 != null) {
                                                                                                                                }
                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                }
                                                                                                                                if (audioRecoder != null) {
                                                                                                                                }
                                                                                                                                checkConversionCanceled();
                                                                                                                                mediaCodec5 = mediaCodec3;
                                                                                                                                i4 = i8;
                                                                                                                                i74 = i7;
                                                                                                                                z6 = z5;
                                                                                                                                int i88222222222222222222 = i9;
                                                                                                                                outputSurface3 = outputSurface2;
                                                                                                                                i10 = i88222222222222222222;
                                                                                                                                mediaExtractor2 = this.extractor;
                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                }
                                                                                                                                mP4Builder2 = this.mediaMuxer;
                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                }
                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                }
                                                                                                                                if (mediaCodec5 != null) {
                                                                                                                                }
                                                                                                                                if (outputSurface3 != null) {
                                                                                                                                }
                                                                                                                                if (inputSurface2 != null) {
                                                                                                                                }
                                                                                                                                str3 = str2;
                                                                                                                                i75 = i4;
                                                                                                                                if (z6) {
                                                                                                                                }
                                                                                                                            } catch (Throwable th63) {
                                                                                                                                th = th63;
                                                                                                                                z2 = z;
                                                                                                                                th = th;
                                                                                                                                mediaCodec2 = mediaCodec11;
                                                                                                                                i3 = i45;
                                                                                                                                i74 = i15;
                                                                                                                                i77 = i27;
                                                                                                                                str = "x";
                                                                                                                                mediaCodec = mediaCodec10;
                                                                                                                                i75 = i14;
                                                                                                                                i2 = i29;
                                                                                                                                inputSurface = inputSurface2;
                                                                                                                                z5 = false;
                                                                                                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                                                FileLog.e(th);
                                                                                                                                mediaExtractor = this.extractor;
                                                                                                                                if (mediaExtractor != null) {
                                                                                                                                }
                                                                                                                                mP4Builder = this.mediaMuxer;
                                                                                                                                if (mP4Builder != null) {
                                                                                                                                }
                                                                                                                                if (mediaCodec2 != null) {
                                                                                                                                }
                                                                                                                                if (mediaCodec != null) {
                                                                                                                                }
                                                                                                                                if (outputSurface != null) {
                                                                                                                                }
                                                                                                                                if (inputSurface != null) {
                                                                                                                                }
                                                                                                                                str3 = str2;
                                                                                                                                z6 = z5;
                                                                                                                                z7 = true;
                                                                                                                                if (z6) {
                                                                                                                                }
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            mediaCodec10 = mediaCodec9;
                                                                                                                            i52 = i2;
                                                                                                                            if (i45 == -5) {
                                                                                                                                byte[] bArr2 = new byte[i93];
                                                                                                                                outputBuffer.limit(bufferInfo4.offset + i93);
                                                                                                                                outputBuffer.position(bufferInfo4.offset);
                                                                                                                                outputBuffer.get(bArr2);
                                                                                                                                byte b2 = 1;
                                                                                                                                int i95 = bufferInfo4.size - 1;
                                                                                                                                while (i95 >= 0 && i95 > 3) {
                                                                                                                                    if (bArr2[i95] == b2 && bArr2[i95 - 1] == 0 && bArr2[i95 - 2] == 0) {
                                                                                                                                        int i96 = i95 - 3;
                                                                                                                                        if (bArr2[i96] == 0) {
                                                                                                                                            byteBuffer4 = ByteBuffer.allocate(i96);
                                                                                                                                            byteBuffer3 = ByteBuffer.allocate(bufferInfo4.size - i96);
                                                                                                                                            byteBuffer4.put(bArr2, 0, i96).position(0);
                                                                                                                                            byteBuffer3.put(bArr2, i96, bufferInfo4.size - i96).position(0);
                                                                                                                                            break;
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                    i95--;
                                                                                                                                    b2 = 1;
                                                                                                                                }
                                                                                                                                byteBuffer3 = null;
                                                                                                                                byteBuffer4 = null;
                                                                                                                                i50 = i31;
                                                                                                                                i49 = i32;
                                                                                                                                MediaFormat createVideoFormat4 = MediaFormat.createVideoFormat(this.outputMimeType, i50, i49);
                                                                                                                                if (byteBuffer4 != null && byteBuffer3 != null) {
                                                                                                                                    createVideoFormat4.setByteBuffer(str11, byteBuffer4);
                                                                                                                                    createVideoFormat4.setByteBuffer("csd-1", byteBuffer3);
                                                                                                                                }
                                                                                                                                i16 = this.mediaMuxer.addTrack(createVideoFormat4, false);
                                                                                                                            }
                                                                                                                        }
                                                                                                                        if ((bufferInfo4.flags & 4) == 0) {
                                                                                                                            i47 = i52;
                                                                                                                            z23 = false;
                                                                                                                            z24 = true;
                                                                                                                        } else {
                                                                                                                            i47 = i52;
                                                                                                                            z23 = false;
                                                                                                                            z24 = false;
                                                                                                                        }
                                                                                                                        mediaCodec11.releaseOutputBuffer(i47, z23);
                                                                                                                        i45 = i16;
                                                                                                                        z12 = z24;
                                                                                                                        i51 = -1;
                                                                                                                    } catch (Exception e52) {
                                                                                                                        e = e52;
                                                                                                                        mediaCodec10 = mediaCodec9;
                                                                                                                    } catch (Throwable th64) {
                                                                                                                        th = th64;
                                                                                                                        mediaCodec10 = mediaCodec9;
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    mediaCodec10 = mediaCodec9;
                                                                                                                    i52 = i2;
                                                                                                                }
                                                                                                                if ((bufferInfo4.flags & 4) == 0) {
                                                                                                                }
                                                                                                                mediaCodec11.releaseOutputBuffer(i47, z23);
                                                                                                                i45 = i16;
                                                                                                                z12 = z24;
                                                                                                                i51 = -1;
                                                                                                            } catch (Exception e53) {
                                                                                                                mediaCodec6 = mediaCodec10;
                                                                                                                i2 = i29;
                                                                                                                exc = e53;
                                                                                                                mediaCodec4 = mediaCodec11;
                                                                                                                i77 = i27;
                                                                                                                if (exc instanceof IllegalStateException) {
                                                                                                                }
                                                                                                                z5 = false;
                                                                                                                StringBuilder sb52222222222222222222 = new StringBuilder();
                                                                                                                sb52222222222222222222.append("bitrate: ");
                                                                                                                sb52222222222222222222.append(i77);
                                                                                                                sb52222222222222222222.append(" framerate: ");
                                                                                                                sb52222222222222222222.append(i2);
                                                                                                                sb52222222222222222222.append(" size: ");
                                                                                                                i8 = i14;
                                                                                                                sb52222222222222222222.append(i8);
                                                                                                                str = "x";
                                                                                                                sb52222222222222222222.append(str);
                                                                                                                i7 = i15;
                                                                                                                sb52222222222222222222.append(i7);
                                                                                                                FileLog.e(sb52222222222222222222.toString());
                                                                                                                FileLog.e(exc);
                                                                                                                i9 = i16;
                                                                                                                mediaCodec7 = mediaCodec6;
                                                                                                                outputSurface4 = outputSurface;
                                                                                                                z7 = true;
                                                                                                                this.extractor.unselectTrack(i13);
                                                                                                                if (mediaCodec7 != null) {
                                                                                                                }
                                                                                                                mediaCodec3 = mediaCodec7;
                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                if (outputSurface2 != null) {
                                                                                                                }
                                                                                                                if (inputSurface2 != null) {
                                                                                                                }
                                                                                                                if (mediaCodec4 != null) {
                                                                                                                }
                                                                                                                if (audioRecoder != null) {
                                                                                                                }
                                                                                                                checkConversionCanceled();
                                                                                                                mediaCodec5 = mediaCodec3;
                                                                                                                i4 = i8;
                                                                                                                i74 = i7;
                                                                                                                z6 = z5;
                                                                                                                int i882222222222222222222 = i9;
                                                                                                                outputSurface3 = outputSurface2;
                                                                                                                i10 = i882222222222222222222;
                                                                                                                mediaExtractor2 = this.extractor;
                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                }
                                                                                                                mP4Builder2 = this.mediaMuxer;
                                                                                                                if (mP4Builder2 != null) {
                                                                                                                }
                                                                                                                if (mediaCodec4 != null) {
                                                                                                                }
                                                                                                                if (mediaCodec5 != null) {
                                                                                                                }
                                                                                                                if (outputSurface3 != null) {
                                                                                                                }
                                                                                                                if (inputSurface2 != null) {
                                                                                                                }
                                                                                                                str3 = str2;
                                                                                                                i75 = i4;
                                                                                                                if (z6) {
                                                                                                                }
                                                                                                            } catch (Throwable th65) {
                                                                                                                i2 = i29;
                                                                                                                th = th65;
                                                                                                                mediaCodec2 = mediaCodec11;
                                                                                                                mediaCodec = mediaCodec10;
                                                                                                                i74 = i15;
                                                                                                                i77 = i27;
                                                                                                                str = "x";
                                                                                                                i75 = i14;
                                                                                                                z5 = false;
                                                                                                                i3 = i16;
                                                                                                                inputSurface = inputSurface2;
                                                                                                                z2 = z;
                                                                                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                                FileLog.e(th);
                                                                                                                mediaExtractor = this.extractor;
                                                                                                                if (mediaExtractor != null) {
                                                                                                                }
                                                                                                                mP4Builder = this.mediaMuxer;
                                                                                                                if (mP4Builder != null) {
                                                                                                                }
                                                                                                                if (mediaCodec2 != null) {
                                                                                                                }
                                                                                                                if (mediaCodec != null) {
                                                                                                                }
                                                                                                                if (outputSurface != null) {
                                                                                                                }
                                                                                                                if (inputSurface != null) {
                                                                                                                }
                                                                                                                str3 = str2;
                                                                                                                z6 = z5;
                                                                                                                z7 = true;
                                                                                                                if (z6) {
                                                                                                                }
                                                                                                            }
                                                                                                            i50 = i31;
                                                                                                            i49 = i32;
                                                                                                            i16 = i45;
                                                                                                        }
                                                                                                    }
                                                                                                    i50 = i31;
                                                                                                    i49 = i32;
                                                                                                    i51 = -1;
                                                                                                }
                                                                                            } catch (Exception e54) {
                                                                                                exc = e54;
                                                                                                inputSurface2 = -1;
                                                                                                outputSurface = outputSurface7;
                                                                                                mediaCodec4 = mediaCodec11;
                                                                                                i16 = i45;
                                                                                                i77 = i27;
                                                                                                if (exc instanceof IllegalStateException) {
                                                                                                }
                                                                                                z5 = false;
                                                                                                StringBuilder sb522222222222222222222 = new StringBuilder();
                                                                                                sb522222222222222222222.append("bitrate: ");
                                                                                                sb522222222222222222222.append(i77);
                                                                                                sb522222222222222222222.append(" framerate: ");
                                                                                                sb522222222222222222222.append(i2);
                                                                                                sb522222222222222222222.append(" size: ");
                                                                                                i8 = i14;
                                                                                                sb522222222222222222222.append(i8);
                                                                                                str = "x";
                                                                                                sb522222222222222222222.append(str);
                                                                                                i7 = i15;
                                                                                                sb522222222222222222222.append(i7);
                                                                                                FileLog.e(sb522222222222222222222.toString());
                                                                                                FileLog.e(exc);
                                                                                                i9 = i16;
                                                                                                mediaCodec7 = mediaCodec6;
                                                                                                outputSurface4 = outputSurface;
                                                                                                z7 = true;
                                                                                                this.extractor.unselectTrack(i13);
                                                                                                if (mediaCodec7 != null) {
                                                                                                }
                                                                                                mediaCodec3 = mediaCodec7;
                                                                                                outputSurface2 = outputSurface4;
                                                                                                if (outputSurface2 != null) {
                                                                                                }
                                                                                                if (inputSurface2 != null) {
                                                                                                }
                                                                                                if (mediaCodec4 != null) {
                                                                                                }
                                                                                                if (audioRecoder != null) {
                                                                                                }
                                                                                                checkConversionCanceled();
                                                                                                mediaCodec5 = mediaCodec3;
                                                                                                i4 = i8;
                                                                                                i74 = i7;
                                                                                                z6 = z5;
                                                                                                int i8822222222222222222222 = i9;
                                                                                                outputSurface3 = outputSurface2;
                                                                                                i10 = i8822222222222222222222;
                                                                                                mediaExtractor2 = this.extractor;
                                                                                                if (mediaExtractor2 != null) {
                                                                                                }
                                                                                                mP4Builder2 = this.mediaMuxer;
                                                                                                if (mP4Builder2 != null) {
                                                                                                }
                                                                                                if (mediaCodec4 != null) {
                                                                                                }
                                                                                                if (mediaCodec5 != null) {
                                                                                                }
                                                                                                if (outputSurface3 != null) {
                                                                                                }
                                                                                                if (inputSurface2 != null) {
                                                                                                }
                                                                                                str3 = str2;
                                                                                                i75 = i4;
                                                                                                if (z6) {
                                                                                                }
                                                                                            } catch (Throwable th66) {
                                                                                                th = th66;
                                                                                                inputSurface = -1;
                                                                                                outputSurface = outputSurface7;
                                                                                                mediaCodec12 = mediaCodec6;
                                                                                                mediaCodec2 = mediaCodec11;
                                                                                                mediaCodec = mediaCodec12;
                                                                                                i74 = i15;
                                                                                                i77 = i27;
                                                                                                i75 = i14;
                                                                                                z5 = false;
                                                                                                z2 = z;
                                                                                                i3 = i45;
                                                                                                str = "x";
                                                                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                FileLog.e(th);
                                                                                                mediaExtractor = this.extractor;
                                                                                                if (mediaExtractor != null) {
                                                                                                }
                                                                                                mP4Builder = this.mediaMuxer;
                                                                                                if (mP4Builder != null) {
                                                                                                }
                                                                                                if (mediaCodec2 != null) {
                                                                                                }
                                                                                                if (mediaCodec != null) {
                                                                                                }
                                                                                                if (outputSurface != null) {
                                                                                                }
                                                                                                if (inputSurface != null) {
                                                                                                }
                                                                                                str3 = str2;
                                                                                                z6 = z5;
                                                                                                z7 = true;
                                                                                                if (z6) {
                                                                                                }
                                                                                            }
                                                                                            if (i47 == i51) {
                                                                                                i31 = i50;
                                                                                                i32 = i49;
                                                                                                str6 = str11;
                                                                                                z18 = z20;
                                                                                                z19 = z21;
                                                                                                mediaCodec6 = mediaCodec13;
                                                                                                i44 = i48;
                                                                                                j18 = j20;
                                                                                                mediaCodec9 = mediaCodec10;
                                                                                            } else {
                                                                                                if (z14) {
                                                                                                    i31 = i50;
                                                                                                    j21 = j12;
                                                                                                    i54 = i49;
                                                                                                    j22 = j16;
                                                                                                    j23 = j20;
                                                                                                    mediaCodec6 = mediaCodec10;
                                                                                                } else {
                                                                                                    i31 = i50;
                                                                                                    mediaCodec6 = mediaCodec10;
                                                                                                    try {
                                                                                                        dequeueOutputBuffer = mediaCodec6.dequeueOutputBuffer(bufferInfo4, 2500L);
                                                                                                    } catch (Exception e55) {
                                                                                                        e = e55;
                                                                                                        i2 = i29;
                                                                                                        exc = e;
                                                                                                        mediaCodec4 = mediaCodec11;
                                                                                                        i16 = i45;
                                                                                                        i77 = i27;
                                                                                                        if (exc instanceof IllegalStateException) {
                                                                                                        }
                                                                                                        z5 = false;
                                                                                                        StringBuilder sb5222222222222222222222 = new StringBuilder();
                                                                                                        sb5222222222222222222222.append("bitrate: ");
                                                                                                        sb5222222222222222222222.append(i77);
                                                                                                        sb5222222222222222222222.append(" framerate: ");
                                                                                                        sb5222222222222222222222.append(i2);
                                                                                                        sb5222222222222222222222.append(" size: ");
                                                                                                        i8 = i14;
                                                                                                        sb5222222222222222222222.append(i8);
                                                                                                        str = "x";
                                                                                                        sb5222222222222222222222.append(str);
                                                                                                        i7 = i15;
                                                                                                        sb5222222222222222222222.append(i7);
                                                                                                        FileLog.e(sb5222222222222222222222.toString());
                                                                                                        FileLog.e(exc);
                                                                                                        i9 = i16;
                                                                                                        mediaCodec7 = mediaCodec6;
                                                                                                        outputSurface4 = outputSurface;
                                                                                                        z7 = true;
                                                                                                        this.extractor.unselectTrack(i13);
                                                                                                        if (mediaCodec7 != null) {
                                                                                                        }
                                                                                                        mediaCodec3 = mediaCodec7;
                                                                                                        outputSurface2 = outputSurface4;
                                                                                                        if (outputSurface2 != null) {
                                                                                                        }
                                                                                                        if (inputSurface2 != null) {
                                                                                                        }
                                                                                                        if (mediaCodec4 != null) {
                                                                                                        }
                                                                                                        if (audioRecoder != null) {
                                                                                                        }
                                                                                                        checkConversionCanceled();
                                                                                                        mediaCodec5 = mediaCodec3;
                                                                                                        i4 = i8;
                                                                                                        i74 = i7;
                                                                                                        z6 = z5;
                                                                                                        int i88222222222222222222222 = i9;
                                                                                                        outputSurface3 = outputSurface2;
                                                                                                        i10 = i88222222222222222222222;
                                                                                                        mediaExtractor2 = this.extractor;
                                                                                                        if (mediaExtractor2 != null) {
                                                                                                        }
                                                                                                        mP4Builder2 = this.mediaMuxer;
                                                                                                        if (mP4Builder2 != null) {
                                                                                                        }
                                                                                                        if (mediaCodec4 != null) {
                                                                                                        }
                                                                                                        if (mediaCodec5 != null) {
                                                                                                        }
                                                                                                        if (outputSurface3 != null) {
                                                                                                        }
                                                                                                        if (inputSurface2 != null) {
                                                                                                        }
                                                                                                        str3 = str2;
                                                                                                        i75 = i4;
                                                                                                        if (z6) {
                                                                                                        }
                                                                                                    } catch (Throwable th67) {
                                                                                                        th = th67;
                                                                                                        i2 = i29;
                                                                                                        th = th;
                                                                                                        inputSurface = inputSurface2;
                                                                                                        mediaCodec12 = mediaCodec6;
                                                                                                        mediaCodec2 = mediaCodec11;
                                                                                                        mediaCodec = mediaCodec12;
                                                                                                        i74 = i15;
                                                                                                        i77 = i27;
                                                                                                        i75 = i14;
                                                                                                        z5 = false;
                                                                                                        z2 = z;
                                                                                                        i3 = i45;
                                                                                                        str = "x";
                                                                                                        FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                        FileLog.e(th);
                                                                                                        mediaExtractor = this.extractor;
                                                                                                        if (mediaExtractor != null) {
                                                                                                        }
                                                                                                        mP4Builder = this.mediaMuxer;
                                                                                                        if (mP4Builder != null) {
                                                                                                        }
                                                                                                        if (mediaCodec2 != null) {
                                                                                                        }
                                                                                                        if (mediaCodec != null) {
                                                                                                        }
                                                                                                        if (outputSurface != null) {
                                                                                                        }
                                                                                                        if (inputSurface != null) {
                                                                                                        }
                                                                                                        str3 = str2;
                                                                                                        z6 = z5;
                                                                                                        z7 = true;
                                                                                                        if (z6) {
                                                                                                        }
                                                                                                    }
                                                                                                    if (dequeueOutputBuffer == -1) {
                                                                                                        j21 = j12;
                                                                                                        i54 = i49;
                                                                                                        j22 = j16;
                                                                                                        j18 = j20;
                                                                                                        i2 = i29;
                                                                                                        outputSurface10 = outputSurface;
                                                                                                        inputSurface3 = inputSurface2;
                                                                                                    } else {
                                                                                                        if (dequeueOutputBuffer != -3) {
                                                                                                            if (dequeueOutputBuffer == -2) {
                                                                                                                try {
                                                                                                                    MediaFormat outputFormat3 = mediaCodec6.getOutputFormat();
                                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                                        FileLog.d("newFormat = " + outputFormat3);
                                                                                                                    }
                                                                                                                } catch (Exception e56) {
                                                                                                                    exc = e56;
                                                                                                                    mediaCodec4 = mediaCodec11;
                                                                                                                    i16 = i45;
                                                                                                                    i77 = i27;
                                                                                                                    i2 = i29;
                                                                                                                    if (exc instanceof IllegalStateException) {
                                                                                                                    }
                                                                                                                    z5 = false;
                                                                                                                    StringBuilder sb52222222222222222222222 = new StringBuilder();
                                                                                                                    sb52222222222222222222222.append("bitrate: ");
                                                                                                                    sb52222222222222222222222.append(i77);
                                                                                                                    sb52222222222222222222222.append(" framerate: ");
                                                                                                                    sb52222222222222222222222.append(i2);
                                                                                                                    sb52222222222222222222222.append(" size: ");
                                                                                                                    i8 = i14;
                                                                                                                    sb52222222222222222222222.append(i8);
                                                                                                                    str = "x";
                                                                                                                    sb52222222222222222222222.append(str);
                                                                                                                    i7 = i15;
                                                                                                                    sb52222222222222222222222.append(i7);
                                                                                                                    FileLog.e(sb52222222222222222222222.toString());
                                                                                                                    FileLog.e(exc);
                                                                                                                    i9 = i16;
                                                                                                                    mediaCodec7 = mediaCodec6;
                                                                                                                    outputSurface4 = outputSurface;
                                                                                                                    z7 = true;
                                                                                                                    this.extractor.unselectTrack(i13);
                                                                                                                    if (mediaCodec7 != null) {
                                                                                                                    }
                                                                                                                    mediaCodec3 = mediaCodec7;
                                                                                                                    outputSurface2 = outputSurface4;
                                                                                                                    if (outputSurface2 != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface2 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                    }
                                                                                                                    if (audioRecoder != null) {
                                                                                                                    }
                                                                                                                    checkConversionCanceled();
                                                                                                                    mediaCodec5 = mediaCodec3;
                                                                                                                    i4 = i8;
                                                                                                                    i74 = i7;
                                                                                                                    z6 = z5;
                                                                                                                    int i882222222222222222222222 = i9;
                                                                                                                    outputSurface3 = outputSurface2;
                                                                                                                    i10 = i882222222222222222222222;
                                                                                                                    mediaExtractor2 = this.extractor;
                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                    }
                                                                                                                    mP4Builder2 = this.mediaMuxer;
                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                    }
                                                                                                                    if (mediaCodec5 != null) {
                                                                                                                    }
                                                                                                                    if (outputSurface3 != null) {
                                                                                                                    }
                                                                                                                    if (inputSurface2 != null) {
                                                                                                                    }
                                                                                                                    str3 = str2;
                                                                                                                    i75 = i4;
                                                                                                                    if (z6) {
                                                                                                                    }
                                                                                                                } catch (Throwable th68) {
                                                                                                                    z2 = z;
                                                                                                                    th = th68;
                                                                                                                    mediaCodec2 = mediaCodec11;
                                                                                                                    mediaCodec = mediaCodec6;
                                                                                                                    i74 = i15;
                                                                                                                    i77 = i27;
                                                                                                                    i75 = i14;
                                                                                                                    i2 = i29;
                                                                                                                }
                                                                                                            } else if (dequeueOutputBuffer < 0) {
                                                                                                                throw new RuntimeException("unexpected result from decoder.dequeueOutputBuffer: " + dequeueOutputBuffer);
                                                                                                            } else {
                                                                                                                try {
                                                                                                                    i54 = i49;
                                                                                                                    boolean z50 = bufferInfo4.size != 0;
                                                                                                                    long j40 = bufferInfo4.presentationTimeUs;
                                                                                                                    if (j35 > 0 && j40 >= j35) {
                                                                                                                        bufferInfo4.flags |= 4;
                                                                                                                        z50 = false;
                                                                                                                        z13 = true;
                                                                                                                        z14 = true;
                                                                                                                    }
                                                                                                                    if (j9 >= 0) {
                                                                                                                        try {
                                                                                                                            if ((bufferInfo4.flags & 4) != 0) {
                                                                                                                                z25 = z50;
                                                                                                                                i2 = i29;
                                                                                                                                try {
                                                                                                                                    try {
                                                                                                                                        j22 = j16;
                                                                                                                                        if (Math.abs(j9 - j12) > MediaController.VIDEO_BITRATE_480 / i2) {
                                                                                                                                            if (j12 > 0) {
                                                                                                                                                try {
                                                                                                                                                    this.extractor.seekTo(j12, 0);
                                                                                                                                                    j21 = j12;
                                                                                                                                                } catch (Throwable th69) {
                                                                                                                                                    th = th69;
                                                                                                                                                    z2 = z;
                                                                                                                                                    th = th;
                                                                                                                                                    mediaCodec2 = mediaCodec11;
                                                                                                                                                    mediaCodec = mediaCodec6;
                                                                                                                                                    i74 = i15;
                                                                                                                                                    i77 = i27;
                                                                                                                                                    i75 = i14;
                                                                                                                                                    inputSurface = inputSurface2;
                                                                                                                                                    z5 = false;
                                                                                                                                                    i3 = i45;
                                                                                                                                                    str = "x";
                                                                                                                                                    FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                                                                    FileLog.e(th);
                                                                                                                                                    mediaExtractor = this.extractor;
                                                                                                                                                    if (mediaExtractor != null) {
                                                                                                                                                    }
                                                                                                                                                    mP4Builder = this.mediaMuxer;
                                                                                                                                                    if (mP4Builder != null) {
                                                                                                                                                    }
                                                                                                                                                    if (mediaCodec2 != null) {
                                                                                                                                                    }
                                                                                                                                                    if (mediaCodec != null) {
                                                                                                                                                    }
                                                                                                                                                    if (outputSurface != null) {
                                                                                                                                                    }
                                                                                                                                                    if (inputSurface != null) {
                                                                                                                                                    }
                                                                                                                                                    str3 = str2;
                                                                                                                                                    z6 = z5;
                                                                                                                                                    z7 = true;
                                                                                                                                                    if (z6) {
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            } else {
                                                                                                                                                j21 = j12;
                                                                                                                                                this.extractor.seekTo(0L, 0);
                                                                                                                                            }
                                                                                                                                            j17 = j20 + j10;
                                                                                                                                            bufferInfo4.flags &= -5;
                                                                                                                                            mediaCodec6.flush();
                                                                                                                                            j35 = j9;
                                                                                                                                            j9 = -1;
                                                                                                                                            z26 = true;
                                                                                                                                            j24 = 0;
                                                                                                                                            z25 = false;
                                                                                                                                            z13 = false;
                                                                                                                                            z14 = false;
                                                                                                                                            if (j15 > j24 || bufferInfo4.presentationTimeUs - j15 >= j4 || (bufferInfo4.flags & 4) != 0) {
                                                                                                                                                j25 = 0;
                                                                                                                                            } else {
                                                                                                                                                j25 = 0;
                                                                                                                                                z25 = false;
                                                                                                                                            }
                                                                                                                                            j26 = j9 >= j25 ? j9 : j21;
                                                                                                                                            if (j26 > j25 && j14 == -1) {
                                                                                                                                                if (j40 >= j26) {
                                                                                                                                                    if (BuildVars.LOGS_ENABLED) {
                                                                                                                                                        FileLog.d("drop frame startTime = " + j26 + " present time = " + bufferInfo4.presentationTimeUs);
                                                                                                                                                    }
                                                                                                                                                    z27 = false;
                                                                                                                                                    if (z26) {
                                                                                                                                                        if (j9 == -1 && j17 != 0) {
                                                                                                                                                            bufferInfo4.presentationTimeUs += j17;
                                                                                                                                                        }
                                                                                                                                                        mediaCodec6.releaseOutputBuffer(dequeueOutputBuffer, z27);
                                                                                                                                                    } else {
                                                                                                                                                        j14 = -1;
                                                                                                                                                    }
                                                                                                                                                    if (z27) {
                                                                                                                                                        outputSurface9 = outputSurface;
                                                                                                                                                        inputSurface4 = inputSurface2;
                                                                                                                                                        j18 = j20;
                                                                                                                                                    } else {
                                                                                                                                                        try {
                                                                                                                                                            long j41 = bufferInfo4.presentationTimeUs;
                                                                                                                                                            long j42 = j20;
                                                                                                                                                            j18 = j9 >= 0 ? Math.max(j42, j41) : j42;
                                                                                                                                                            try {
                                                                                                                                                                outputSurface.awaitNewImage();
                                                                                                                                                                z28 = false;
                                                                                                                                                            } catch (Exception e57) {
                                                                                                                                                                FileLog.e(e57);
                                                                                                                                                                z28 = true;
                                                                                                                                                            }
                                                                                                                                                            if (z28) {
                                                                                                                                                                outputSurface8 = outputSurface;
                                                                                                                                                                inputSurface4 = inputSurface2;
                                                                                                                                                            } else {
                                                                                                                                                                outputSurface8 = outputSurface;
                                                                                                                                                                try {
                                                                                                                                                                    outputSurface8.drawImage(bufferInfo4.presentationTimeUs * 1000);
                                                                                                                                                                    inputSurface4 = inputSurface2;
                                                                                                                                                                } catch (Exception e58) {
                                                                                                                                                                    e = e58;
                                                                                                                                                                } catch (Throwable th70) {
                                                                                                                                                                    th = th70;
                                                                                                                                                                    inputSurface4 = inputSurface2;
                                                                                                                                                                }
                                                                                                                                                                try {
                                                                                                                                                                    inputSurface4.setPresentationTime(bufferInfo4.presentationTimeUs * 1000);
                                                                                                                                                                    inputSurface4.swapBuffers();
                                                                                                                                                                } catch (Exception e59) {
                                                                                                                                                                    e = e59;
                                                                                                                                                                    inputSurface2 = inputSurface4;
                                                                                                                                                                    outputSurface = outputSurface8;
                                                                                                                                                                    mediaCodec4 = mediaCodec11;
                                                                                                                                                                    i16 = i45;
                                                                                                                                                                    i77 = i27;
                                                                                                                                                                    exc = e;
                                                                                                                                                                    if (exc instanceof IllegalStateException) {
                                                                                                                                                                    }
                                                                                                                                                                    z5 = false;
                                                                                                                                                                    StringBuilder sb522222222222222222222222 = new StringBuilder();
                                                                                                                                                                    sb522222222222222222222222.append("bitrate: ");
                                                                                                                                                                    sb522222222222222222222222.append(i77);
                                                                                                                                                                    sb522222222222222222222222.append(" framerate: ");
                                                                                                                                                                    sb522222222222222222222222.append(i2);
                                                                                                                                                                    sb522222222222222222222222.append(" size: ");
                                                                                                                                                                    i8 = i14;
                                                                                                                                                                    sb522222222222222222222222.append(i8);
                                                                                                                                                                    str = "x";
                                                                                                                                                                    sb522222222222222222222222.append(str);
                                                                                                                                                                    i7 = i15;
                                                                                                                                                                    sb522222222222222222222222.append(i7);
                                                                                                                                                                    FileLog.e(sb522222222222222222222222.toString());
                                                                                                                                                                    FileLog.e(exc);
                                                                                                                                                                    i9 = i16;
                                                                                                                                                                    mediaCodec7 = mediaCodec6;
                                                                                                                                                                    outputSurface4 = outputSurface;
                                                                                                                                                                    z7 = true;
                                                                                                                                                                    this.extractor.unselectTrack(i13);
                                                                                                                                                                    if (mediaCodec7 != null) {
                                                                                                                                                                    }
                                                                                                                                                                    mediaCodec3 = mediaCodec7;
                                                                                                                                                                    outputSurface2 = outputSurface4;
                                                                                                                                                                    if (outputSurface2 != null) {
                                                                                                                                                                    }
                                                                                                                                                                    if (inputSurface2 != null) {
                                                                                                                                                                    }
                                                                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                                                                    }
                                                                                                                                                                    if (audioRecoder != null) {
                                                                                                                                                                    }
                                                                                                                                                                    checkConversionCanceled();
                                                                                                                                                                    mediaCodec5 = mediaCodec3;
                                                                                                                                                                    i4 = i8;
                                                                                                                                                                    i74 = i7;
                                                                                                                                                                    z6 = z5;
                                                                                                                                                                    int i8822222222222222222222222 = i9;
                                                                                                                                                                    outputSurface3 = outputSurface2;
                                                                                                                                                                    i10 = i8822222222222222222222222;
                                                                                                                                                                    mediaExtractor2 = this.extractor;
                                                                                                                                                                    if (mediaExtractor2 != null) {
                                                                                                                                                                    }
                                                                                                                                                                    mP4Builder2 = this.mediaMuxer;
                                                                                                                                                                    if (mP4Builder2 != null) {
                                                                                                                                                                    }
                                                                                                                                                                    if (mediaCodec4 != null) {
                                                                                                                                                                    }
                                                                                                                                                                    if (mediaCodec5 != null) {
                                                                                                                                                                    }
                                                                                                                                                                    if (outputSurface3 != null) {
                                                                                                                                                                    }
                                                                                                                                                                    if (inputSurface2 != null) {
                                                                                                                                                                    }
                                                                                                                                                                    str3 = str2;
                                                                                                                                                                    i75 = i4;
                                                                                                                                                                    if (z6) {
                                                                                                                                                                    }
                                                                                                                                                                } catch (Throwable th71) {
                                                                                                                                                                    th = th71;
                                                                                                                                                                    z2 = z;
                                                                                                                                                                    th = th;
                                                                                                                                                                    inputSurface = inputSurface4;
                                                                                                                                                                    outputSurface = outputSurface8;
                                                                                                                                                                    mediaCodec2 = mediaCodec11;
                                                                                                                                                                    mediaCodec = mediaCodec6;
                                                                                                                                                                    i74 = i15;
                                                                                                                                                                    i77 = i27;
                                                                                                                                                                    i75 = i14;
                                                                                                                                                                    z5 = false;
                                                                                                                                                                    i3 = i45;
                                                                                                                                                                    str = "x";
                                                                                                                                                                    FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                                                                                    FileLog.e(th);
                                                                                                                                                                    mediaExtractor = this.extractor;
                                                                                                                                                                    if (mediaExtractor != null) {
                                                                                                                                                                    }
                                                                                                                                                                    mP4Builder = this.mediaMuxer;
                                                                                                                                                                    if (mP4Builder != null) {
                                                                                                                                                                    }
                                                                                                                                                                    if (mediaCodec2 != null) {
                                                                                                                                                                    }
                                                                                                                                                                    if (mediaCodec != null) {
                                                                                                                                                                    }
                                                                                                                                                                    if (outputSurface != null) {
                                                                                                                                                                    }
                                                                                                                                                                    if (inputSurface != null) {
                                                                                                                                                                    }
                                                                                                                                                                    str3 = str2;
                                                                                                                                                                    z6 = z5;
                                                                                                                                                                    z7 = true;
                                                                                                                                                                    if (z6) {
                                                                                                                                                                    }
                                                                                                                                                                }
                                                                                                                                                            }
                                                                                                                                                            j15 = j41;
                                                                                                                                                            outputSurface9 = outputSurface8;
                                                                                                                                                        } catch (Exception e60) {
                                                                                                                                                            e = e60;
                                                                                                                                                            mediaCodec4 = mediaCodec11;
                                                                                                                                                            i16 = i45;
                                                                                                                                                            i77 = i27;
                                                                                                                                                            exc = e;
                                                                                                                                                            if (exc instanceof IllegalStateException) {
                                                                                                                                                            }
                                                                                                                                                            z5 = false;
                                                                                                                                                            StringBuilder sb5222222222222222222222222 = new StringBuilder();
                                                                                                                                                            sb5222222222222222222222222.append("bitrate: ");
                                                                                                                                                            sb5222222222222222222222222.append(i77);
                                                                                                                                                            sb5222222222222222222222222.append(" framerate: ");
                                                                                                                                                            sb5222222222222222222222222.append(i2);
                                                                                                                                                            sb5222222222222222222222222.append(" size: ");
                                                                                                                                                            i8 = i14;
                                                                                                                                                            sb5222222222222222222222222.append(i8);
                                                                                                                                                            str = "x";
                                                                                                                                                            sb5222222222222222222222222.append(str);
                                                                                                                                                            i7 = i15;
                                                                                                                                                            sb5222222222222222222222222.append(i7);
                                                                                                                                                            FileLog.e(sb5222222222222222222222222.toString());
                                                                                                                                                            FileLog.e(exc);
                                                                                                                                                            i9 = i16;
                                                                                                                                                            mediaCodec7 = mediaCodec6;
                                                                                                                                                            outputSurface4 = outputSurface;
                                                                                                                                                            z7 = true;
                                                                                                                                                            this.extractor.unselectTrack(i13);
                                                                                                                                                            if (mediaCodec7 != null) {
                                                                                                                                                            }
                                                                                                                                                            mediaCodec3 = mediaCodec7;
                                                                                                                                                            outputSurface2 = outputSurface4;
                                                                                                                                                            if (outputSurface2 != null) {
                                                                                                                                                            }
                                                                                                                                                            if (inputSurface2 != null) {
                                                                                                                                                            }
                                                                                                                                                            if (mediaCodec4 != null) {
                                                                                                                                                            }
                                                                                                                                                            if (audioRecoder != null) {
                                                                                                                                                            }
                                                                                                                                                            checkConversionCanceled();
                                                                                                                                                            mediaCodec5 = mediaCodec3;
                                                                                                                                                            i4 = i8;
                                                                                                                                                            i74 = i7;
                                                                                                                                                            z6 = z5;
                                                                                                                                                            int i88222222222222222222222222 = i9;
                                                                                                                                                            outputSurface3 = outputSurface2;
                                                                                                                                                            i10 = i88222222222222222222222222;
                                                                                                                                                            mediaExtractor2 = this.extractor;
                                                                                                                                                            if (mediaExtractor2 != null) {
                                                                                                                                                            }
                                                                                                                                                            mP4Builder2 = this.mediaMuxer;
                                                                                                                                                            if (mP4Builder2 != null) {
                                                                                                                                                            }
                                                                                                                                                            if (mediaCodec4 != null) {
                                                                                                                                                            }
                                                                                                                                                            if (mediaCodec5 != null) {
                                                                                                                                                            }
                                                                                                                                                            if (outputSurface3 != null) {
                                                                                                                                                            }
                                                                                                                                                            if (inputSurface2 != null) {
                                                                                                                                                            }
                                                                                                                                                            str3 = str2;
                                                                                                                                                            i75 = i4;
                                                                                                                                                            if (z6) {
                                                                                                                                                            }
                                                                                                                                                        } catch (Throwable th72) {
                                                                                                                                                            th = th72;
                                                                                                                                                            z2 = z;
                                                                                                                                                            th = th;
                                                                                                                                                            inputSurface = inputSurface2;
                                                                                                                                                            mediaCodec2 = mediaCodec11;
                                                                                                                                                            mediaCodec = mediaCodec6;
                                                                                                                                                            i74 = i15;
                                                                                                                                                            i77 = i27;
                                                                                                                                                            i75 = i14;
                                                                                                                                                            z5 = false;
                                                                                                                                                            i3 = i45;
                                                                                                                                                            str = "x";
                                                                                                                                                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                                                                            FileLog.e(th);
                                                                                                                                                            mediaExtractor = this.extractor;
                                                                                                                                                            if (mediaExtractor != null) {
                                                                                                                                                            }
                                                                                                                                                            mP4Builder = this.mediaMuxer;
                                                                                                                                                            if (mP4Builder != null) {
                                                                                                                                                            }
                                                                                                                                                            if (mediaCodec2 != null) {
                                                                                                                                                            }
                                                                                                                                                            if (mediaCodec != null) {
                                                                                                                                                            }
                                                                                                                                                            if (outputSurface != null) {
                                                                                                                                                            }
                                                                                                                                                            if (inputSurface != null) {
                                                                                                                                                            }
                                                                                                                                                            str3 = str2;
                                                                                                                                                            z6 = z5;
                                                                                                                                                            z7 = true;
                                                                                                                                                            if (z6) {
                                                                                                                                                            }
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                    if ((bufferInfo4.flags & 4) == 0) {
                                                                                                                                                        if (BuildVars.LOGS_ENABLED) {
                                                                                                                                                            FileLog.d("decoder stream end");
                                                                                                                                                        }
                                                                                                                                                        mediaCodec11.signalEndOfInputStream();
                                                                                                                                                        inputSurface3 = inputSurface4;
                                                                                                                                                        outputSurface10 = outputSurface9;
                                                                                                                                                    } else {
                                                                                                                                                        inputSurface3 = inputSurface4;
                                                                                                                                                        outputSurface7 = outputSurface9;
                                                                                                                                                        i29 = i2;
                                                                                                                                                        inputSurface2 = inputSurface3;
                                                                                                                                                        outputSurface = outputSurface7;
                                                                                                                                                        mediaCodec9 = mediaCodec6;
                                                                                                                                                        str6 = str11;
                                                                                                                                                        z18 = z20;
                                                                                                                                                        z19 = z21;
                                                                                                                                                        mediaCodec6 = mediaCodec13;
                                                                                                                                                        i44 = i48;
                                                                                                                                                        j16 = j22;
                                                                                                                                                        i32 = i54;
                                                                                                                                                        j12 = j21;
                                                                                                                                                    }
                                                                                                                                                } else {
                                                                                                                                                    long j43 = bufferInfo4.presentationTimeUs;
                                                                                                                                                    if (j20 != -2147483648L) {
                                                                                                                                                        j17 -= j43;
                                                                                                                                                    }
                                                                                                                                                    j14 = j43;
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            z27 = z25;
                                                                                                                                            if (z26) {
                                                                                                                                            }
                                                                                                                                            if (z27) {
                                                                                                                                            }
                                                                                                                                            if ((bufferInfo4.flags & 4) == 0) {
                                                                                                                                            }
                                                                                                                                        } else {
                                                                                                                                            j21 = j12;
                                                                                                                                            z26 = false;
                                                                                                                                            j24 = 0;
                                                                                                                                            if (j15 > j24) {
                                                                                                                                            }
                                                                                                                                            j25 = 0;
                                                                                                                                            if (j9 >= j25) {
                                                                                                                                            }
                                                                                                                                            if (j26 > j25) {
                                                                                                                                                if (j40 >= j26) {
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                            z27 = z25;
                                                                                                                                            if (z26) {
                                                                                                                                            }
                                                                                                                                            if (z27) {
                                                                                                                                            }
                                                                                                                                            if ((bufferInfo4.flags & 4) == 0) {
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    } catch (Exception e61) {
                                                                                                                                        e = e61;
                                                                                                                                        exc = e;
                                                                                                                                        mediaCodec4 = mediaCodec11;
                                                                                                                                        i16 = i45;
                                                                                                                                        i77 = i27;
                                                                                                                                        if (exc instanceof IllegalStateException) {
                                                                                                                                            z2 = z;
                                                                                                                                            if (!z2) {
                                                                                                                                                z5 = true;
                                                                                                                                                StringBuilder sb52222222222222222222222222 = new StringBuilder();
                                                                                                                                                sb52222222222222222222222222.append("bitrate: ");
                                                                                                                                                sb52222222222222222222222222.append(i77);
                                                                                                                                                sb52222222222222222222222222.append(" framerate: ");
                                                                                                                                                sb52222222222222222222222222.append(i2);
                                                                                                                                                sb52222222222222222222222222.append(" size: ");
                                                                                                                                                i8 = i14;
                                                                                                                                                sb52222222222222222222222222.append(i8);
                                                                                                                                                str = "x";
                                                                                                                                                sb52222222222222222222222222.append(str);
                                                                                                                                                i7 = i15;
                                                                                                                                                sb52222222222222222222222222.append(i7);
                                                                                                                                                FileLog.e(sb52222222222222222222222222.toString());
                                                                                                                                                FileLog.e(exc);
                                                                                                                                                i9 = i16;
                                                                                                                                                mediaCodec7 = mediaCodec6;
                                                                                                                                                outputSurface4 = outputSurface;
                                                                                                                                                z7 = true;
                                                                                                                                                this.extractor.unselectTrack(i13);
                                                                                                                                                if (mediaCodec7 != null) {
                                                                                                                                                }
                                                                                                                                                mediaCodec3 = mediaCodec7;
                                                                                                                                                outputSurface2 = outputSurface4;
                                                                                                                                                if (outputSurface2 != null) {
                                                                                                                                                }
                                                                                                                                                if (inputSurface2 != null) {
                                                                                                                                                }
                                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                                }
                                                                                                                                                if (audioRecoder != null) {
                                                                                                                                                }
                                                                                                                                                checkConversionCanceled();
                                                                                                                                                mediaCodec5 = mediaCodec3;
                                                                                                                                                i4 = i8;
                                                                                                                                                i74 = i7;
                                                                                                                                                z6 = z5;
                                                                                                                                                int i882222222222222222222222222 = i9;
                                                                                                                                                outputSurface3 = outputSurface2;
                                                                                                                                                i10 = i882222222222222222222222222;
                                                                                                                                                mediaExtractor2 = this.extractor;
                                                                                                                                                if (mediaExtractor2 != null) {
                                                                                                                                                }
                                                                                                                                                mP4Builder2 = this.mediaMuxer;
                                                                                                                                                if (mP4Builder2 != null) {
                                                                                                                                                }
                                                                                                                                                if (mediaCodec4 != null) {
                                                                                                                                                }
                                                                                                                                                if (mediaCodec5 != null) {
                                                                                                                                                }
                                                                                                                                                if (outputSurface3 != null) {
                                                                                                                                                }
                                                                                                                                                if (inputSurface2 != null) {
                                                                                                                                                }
                                                                                                                                                str3 = str2;
                                                                                                                                                i75 = i4;
                                                                                                                                                if (z6) {
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        } else {
                                                                                                                                            z2 = z;
                                                                                                                                        }
                                                                                                                                        z5 = false;
                                                                                                                                        StringBuilder sb522222222222222222222222222 = new StringBuilder();
                                                                                                                                        sb522222222222222222222222222.append("bitrate: ");
                                                                                                                                        sb522222222222222222222222222.append(i77);
                                                                                                                                        sb522222222222222222222222222.append(" framerate: ");
                                                                                                                                        sb522222222222222222222222222.append(i2);
                                                                                                                                        sb522222222222222222222222222.append(" size: ");
                                                                                                                                        i8 = i14;
                                                                                                                                        sb522222222222222222222222222.append(i8);
                                                                                                                                        str = "x";
                                                                                                                                        sb522222222222222222222222222.append(str);
                                                                                                                                        i7 = i15;
                                                                                                                                        sb522222222222222222222222222.append(i7);
                                                                                                                                        FileLog.e(sb522222222222222222222222222.toString());
                                                                                                                                        FileLog.e(exc);
                                                                                                                                        i9 = i16;
                                                                                                                                        mediaCodec7 = mediaCodec6;
                                                                                                                                        outputSurface4 = outputSurface;
                                                                                                                                        z7 = true;
                                                                                                                                        this.extractor.unselectTrack(i13);
                                                                                                                                        if (mediaCodec7 != null) {
                                                                                                                                        }
                                                                                                                                        mediaCodec3 = mediaCodec7;
                                                                                                                                        outputSurface2 = outputSurface4;
                                                                                                                                        if (outputSurface2 != null) {
                                                                                                                                        }
                                                                                                                                        if (inputSurface2 != null) {
                                                                                                                                        }
                                                                                                                                        if (mediaCodec4 != null) {
                                                                                                                                        }
                                                                                                                                        if (audioRecoder != null) {
                                                                                                                                        }
                                                                                                                                        checkConversionCanceled();
                                                                                                                                        mediaCodec5 = mediaCodec3;
                                                                                                                                        i4 = i8;
                                                                                                                                        i74 = i7;
                                                                                                                                        z6 = z5;
                                                                                                                                        int i8822222222222222222222222222 = i9;
                                                                                                                                        outputSurface3 = outputSurface2;
                                                                                                                                        i10 = i8822222222222222222222222222;
                                                                                                                                        mediaExtractor2 = this.extractor;
                                                                                                                                        if (mediaExtractor2 != null) {
                                                                                                                                        }
                                                                                                                                        mP4Builder2 = this.mediaMuxer;
                                                                                                                                        if (mP4Builder2 != null) {
                                                                                                                                        }
                                                                                                                                        if (mediaCodec4 != null) {
                                                                                                                                        }
                                                                                                                                        if (mediaCodec5 != null) {
                                                                                                                                        }
                                                                                                                                        if (outputSurface3 != null) {
                                                                                                                                        }
                                                                                                                                        if (inputSurface2 != null) {
                                                                                                                                        }
                                                                                                                                        str3 = str2;
                                                                                                                                        i75 = i4;
                                                                                                                                        if (z6) {
                                                                                                                                        }
                                                                                                                                    }
                                                                                                                                } catch (Throwable th73) {
                                                                                                                                    th = th73;
                                                                                                                                    z2 = z;
                                                                                                                                    th = th;
                                                                                                                                    mediaCodec2 = mediaCodec11;
                                                                                                                                    mediaCodec = mediaCodec6;
                                                                                                                                    i74 = i15;
                                                                                                                                    i77 = i27;
                                                                                                                                    i75 = i14;
                                                                                                                                    inputSurface = inputSurface2;
                                                                                                                                    z5 = false;
                                                                                                                                    i3 = i45;
                                                                                                                                    str = "x";
                                                                                                                                    FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                                                    FileLog.e(th);
                                                                                                                                    mediaExtractor = this.extractor;
                                                                                                                                    if (mediaExtractor != null) {
                                                                                                                                    }
                                                                                                                                    mP4Builder = this.mediaMuxer;
                                                                                                                                    if (mP4Builder != null) {
                                                                                                                                    }
                                                                                                                                    if (mediaCodec2 != null) {
                                                                                                                                    }
                                                                                                                                    if (mediaCodec != null) {
                                                                                                                                    }
                                                                                                                                    if (outputSurface != null) {
                                                                                                                                    }
                                                                                                                                    if (inputSurface != null) {
                                                                                                                                    }
                                                                                                                                    str3 = str2;
                                                                                                                                    z6 = z5;
                                                                                                                                    z7 = true;
                                                                                                                                    if (z6) {
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            }
                                                                                                                        } catch (Exception e62) {
                                                                                                                            e = e62;
                                                                                                                            i2 = i29;
                                                                                                                        } catch (Throwable th74) {
                                                                                                                            th = th74;
                                                                                                                            i2 = i29;
                                                                                                                        }
                                                                                                                    }
                                                                                                                    z25 = z50;
                                                                                                                    j21 = j12;
                                                                                                                    j22 = j16;
                                                                                                                    i2 = i29;
                                                                                                                    z26 = false;
                                                                                                                    j24 = 0;
                                                                                                                    if (j15 > j24) {
                                                                                                                    }
                                                                                                                    j25 = 0;
                                                                                                                    if (j9 >= j25) {
                                                                                                                    }
                                                                                                                    if (j26 > j25) {
                                                                                                                    }
                                                                                                                    z27 = z25;
                                                                                                                    if (z26) {
                                                                                                                    }
                                                                                                                    if (z27) {
                                                                                                                    }
                                                                                                                    if ((bufferInfo4.flags & 4) == 0) {
                                                                                                                    }
                                                                                                                } catch (Exception e63) {
                                                                                                                    e = e63;
                                                                                                                    i2 = i29;
                                                                                                                } catch (Throwable th75) {
                                                                                                                    th = th75;
                                                                                                                    i2 = i29;
                                                                                                                }
                                                                                                            }
                                                                                                            inputSurface = inputSurface2;
                                                                                                            z5 = false;
                                                                                                            i3 = i45;
                                                                                                            str = "x";
                                                                                                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                                            FileLog.e(th);
                                                                                                            mediaExtractor = this.extractor;
                                                                                                            if (mediaExtractor != null) {
                                                                                                                mediaExtractor.release();
                                                                                                            }
                                                                                                            mP4Builder = this.mediaMuxer;
                                                                                                            if (mP4Builder != null) {
                                                                                                                try {
                                                                                                                    mP4Builder.finishMovie();
                                                                                                                    this.endPresentationTime = this.mediaMuxer.getLastFrameTimestamp(i3);
                                                                                                                } catch (Throwable th76) {
                                                                                                                    FileLog.e(th76);
                                                                                                                }
                                                                                                            }
                                                                                                            if (mediaCodec2 != null) {
                                                                                                                try {
                                                                                                                    mediaCodec2.release();
                                                                                                                } catch (Exception unused6) {
                                                                                                                }
                                                                                                            }
                                                                                                            if (mediaCodec != null) {
                                                                                                                try {
                                                                                                                    mediaCodec.release();
                                                                                                                } catch (Exception unused7) {
                                                                                                                }
                                                                                                            }
                                                                                                            if (outputSurface != null) {
                                                                                                                try {
                                                                                                                    outputSurface.release();
                                                                                                                } catch (Exception unused8) {
                                                                                                                }
                                                                                                            }
                                                                                                            if (inputSurface != null) {
                                                                                                                try {
                                                                                                                    inputSurface.release();
                                                                                                                } catch (Exception unused9) {
                                                                                                                }
                                                                                                            }
                                                                                                            str3 = str2;
                                                                                                            z6 = z5;
                                                                                                            z7 = true;
                                                                                                            if (z6) {
                                                                                                                return convertVideoInternal(convertVideoParams, true, i + 1);
                                                                                                            }
                                                                                                            if (z7 && z4 && i < 3) {
                                                                                                                return convertVideoInternal(convertVideoParams, z2, i + 1);
                                                                                                            }
                                                                                                            long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                                                                                                            if (BuildVars.LOGS_ENABLED) {
                                                                                                                FileLog.d("compression completed time=" + currentTimeMillis2 + " needCompress=" + z3 + " w=" + i74 + " h=" + i75 + " bitrate=" + i77 + " file size=" + AndroidUtilities.formatFileSize(file.length()) + " encoder_name=" + str3);
                                                                                                            }
                                                                                                            return z7;
                                                                                                        }
                                                                                                        j21 = j12;
                                                                                                        i54 = i49;
                                                                                                        j22 = j16;
                                                                                                        j23 = j20;
                                                                                                    }
                                                                                                    z20 = false;
                                                                                                    outputSurface7 = outputSurface10;
                                                                                                    i29 = i2;
                                                                                                    inputSurface2 = inputSurface3;
                                                                                                    outputSurface = outputSurface7;
                                                                                                    mediaCodec9 = mediaCodec6;
                                                                                                    str6 = str11;
                                                                                                    z18 = z20;
                                                                                                    z19 = z21;
                                                                                                    mediaCodec6 = mediaCodec13;
                                                                                                    i44 = i48;
                                                                                                    j16 = j22;
                                                                                                    i32 = i54;
                                                                                                    j12 = j21;
                                                                                                }
                                                                                                i2 = i29;
                                                                                                outputSurface7 = outputSurface;
                                                                                                inputSurface3 = inputSurface2;
                                                                                                j18 = j23;
                                                                                                i29 = i2;
                                                                                                inputSurface2 = inputSurface3;
                                                                                                outputSurface = outputSurface7;
                                                                                                mediaCodec9 = mediaCodec6;
                                                                                                str6 = str11;
                                                                                                z18 = z20;
                                                                                                z19 = z21;
                                                                                                mediaCodec6 = mediaCodec13;
                                                                                                i44 = i48;
                                                                                                j16 = j22;
                                                                                                i32 = i54;
                                                                                                j12 = j21;
                                                                                            }
                                                                                            int i97 = i46;
                                                                                            mediaCodec4 = mediaCodec11;
                                                                                            i39 = i97;
                                                                                            outputSurface7 = outputSurface7;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                j13 = j18;
                                                                                i30 = i44;
                                                                                audioRecoder2 = audioRecoder;
                                                                                z11 = z17;
                                                                                inputBuffers = byteBufferArr2;
                                                                                outputSurface4 = outputSurface;
                                                                                mediaCodec7 = mediaCodec9;
                                                                                i35 = i43;
                                                                                bufferInfo3 = bufferInfo4;
                                                                                i9 = i45;
                                                                                i41 = outputSurface7;
                                                                            }
                                                                            z2 = z;
                                                                            audioRecoder = audioRecoder2;
                                                                            i7 = i15;
                                                                            i77 = i27;
                                                                            str = "x";
                                                                            i8 = i14;
                                                                            i2 = i29;
                                                                            z7 = false;
                                                                            z5 = false;
                                                                            this.extractor.unselectTrack(i13);
                                                                            if (mediaCodec7 != null) {
                                                                                mediaCodec7.stop();
                                                                                mediaCodec7.release();
                                                                            }
                                                                            mediaCodec3 = mediaCodec7;
                                                                            outputSurface2 = outputSurface4;
                                                                        }
                                                                    }
                                                                }
                                                                mediaCodec4 = mediaCodec14;
                                                                outputSurface6 = outputSurface5;
                                                                inputSurface2 = inputSurface;
                                                                f2 = f4;
                                                                i30 = i6;
                                                                str2 = name;
                                                                j8 = j31;
                                                                obj2 = obj;
                                                                i31 = i28;
                                                                z10 = z38;
                                                                j9 = j6;
                                                                j10 = j3;
                                                                bufferInfo2 = bufferInfo;
                                                                j11 = j7;
                                                                mediaFormat = trackFormat;
                                                                i32 = i26;
                                                                if (!z41) {
                                                                    try {
                                                                    } catch (Exception e64) {
                                                                        e = e64;
                                                                        i14 = i14;
                                                                        outputSurface4 = outputSurface6;
                                                                    } catch (Throwable th77) {
                                                                        th = th77;
                                                                        i14 = i14;
                                                                        outputSurface4 = outputSurface6;
                                                                    }
                                                                    if (Math.max(i14, i14) / Math.max(i73, i72) < 0.9f) {
                                                                        i14 = i14;
                                                                        bufferInfo3 = bufferInfo2;
                                                                        outputSurface4 = outputSurface6;
                                                                        try {
                                                                            try {
                                                                                createFragmentShader = createFragmentShader(i72, i73, i15, i14, true, z43 ? 0 : 3);
                                                                                i33 = z43 ? 0 : 3;
                                                                                i34 = 0;
                                                                            } catch (Exception e65) {
                                                                                e = e65;
                                                                                exc = e;
                                                                                outputSurface = outputSurface4;
                                                                                i77 = i27;
                                                                                i2 = i29;
                                                                                i16 = -5;
                                                                                mediaCodec6 = null;
                                                                                audioRecoder = null;
                                                                                if (exc instanceof IllegalStateException) {
                                                                                }
                                                                                z5 = false;
                                                                                StringBuilder sb5222222222222222222222222222 = new StringBuilder();
                                                                                sb5222222222222222222222222222.append("bitrate: ");
                                                                                sb5222222222222222222222222222.append(i77);
                                                                                sb5222222222222222222222222222.append(" framerate: ");
                                                                                sb5222222222222222222222222222.append(i2);
                                                                                sb5222222222222222222222222222.append(" size: ");
                                                                                i8 = i14;
                                                                                sb5222222222222222222222222222.append(i8);
                                                                                str = "x";
                                                                                sb5222222222222222222222222222.append(str);
                                                                                i7 = i15;
                                                                                sb5222222222222222222222222222.append(i7);
                                                                                FileLog.e(sb5222222222222222222222222222.toString());
                                                                                FileLog.e(exc);
                                                                                i9 = i16;
                                                                                mediaCodec7 = mediaCodec6;
                                                                                outputSurface4 = outputSurface;
                                                                                z7 = true;
                                                                                this.extractor.unselectTrack(i13);
                                                                                if (mediaCodec7 != null) {
                                                                                }
                                                                                mediaCodec3 = mediaCodec7;
                                                                                outputSurface2 = outputSurface4;
                                                                                if (outputSurface2 != null) {
                                                                                }
                                                                                if (inputSurface2 != null) {
                                                                                }
                                                                                if (mediaCodec4 != null) {
                                                                                }
                                                                                if (audioRecoder != null) {
                                                                                }
                                                                                checkConversionCanceled();
                                                                                mediaCodec5 = mediaCodec3;
                                                                                i4 = i8;
                                                                                i74 = i7;
                                                                                z6 = z5;
                                                                                int i88222222222222222222222222222 = i9;
                                                                                outputSurface3 = outputSurface2;
                                                                                i10 = i88222222222222222222222222222;
                                                                                mediaExtractor2 = this.extractor;
                                                                                if (mediaExtractor2 != null) {
                                                                                }
                                                                                mP4Builder2 = this.mediaMuxer;
                                                                                if (mP4Builder2 != null) {
                                                                                }
                                                                                if (mediaCodec4 != null) {
                                                                                }
                                                                                if (mediaCodec5 != null) {
                                                                                }
                                                                                if (outputSurface3 != null) {
                                                                                }
                                                                                if (inputSurface2 != null) {
                                                                                }
                                                                                str3 = str2;
                                                                                i75 = i4;
                                                                                if (z6) {
                                                                                }
                                                                            }
                                                                            try {
                                                                                outputSurface4.changeFragmentShader(createFragmentShader, createFragmentShader(i72, i73, i15, i14, false, i33), false);
                                                                                mediaCodec7 = getDecoderByFormat(mediaFormat);
                                                                                mediaCodec7.configure(mediaFormat, outputSurface4.getSurface(), (MediaCrypto) null, i34);
                                                                                mediaCodec7.start();
                                                                                if (i23 >= 21) {
                                                                                }
                                                                                ?? mP4Builder42 = new MP4Builder();
                                                                                ?? equals2 = this.outputMimeType.equals(obj2);
                                                                                this.mediaMuxer = mP4Builder42.createMovie(mp4Movie2, z10, equals2);
                                                                                if (i30 < 0) {
                                                                                }
                                                                                if (audioRecoder2 != null) {
                                                                                }
                                                                                checkConversionCanceled();
                                                                                byteBufferArr = outputBuffers;
                                                                                i39 = i37;
                                                                                z11 = z49;
                                                                                byteBuffer2 = byteBuffer;
                                                                                j13 = -2147483648L;
                                                                                j14 = -1;
                                                                                j15 = -1;
                                                                                j16 = 0;
                                                                                i9 = -5;
                                                                                j17 = 0;
                                                                                z12 = false;
                                                                                z13 = false;
                                                                                z14 = false;
                                                                                i40 = 0;
                                                                                z15 = true;
                                                                                i41 = i34;
                                                                                while (true) {
                                                                                    if (z12) {
                                                                                    }
                                                                                    checkConversionCanceled();
                                                                                    if (audioRecoder2 == null) {
                                                                                    }
                                                                                    if (z13) {
                                                                                    }
                                                                                    z18 = !z14;
                                                                                    j18 = j13;
                                                                                    i45 = i42;
                                                                                    z19 = true;
                                                                                    outputSurface7 = i41;
                                                                                    while (true) {
                                                                                        if (!z18) {
                                                                                        }
                                                                                        checkConversionCanceled();
                                                                                        z20 = z18;
                                                                                        if (z) {
                                                                                        }
                                                                                        i2 = mediaCodec11.dequeueOutputBuffer(bufferInfo4, j19);
                                                                                        if (i2 != -1) {
                                                                                        }
                                                                                        if (i47 == i51) {
                                                                                        }
                                                                                        int i972 = i46;
                                                                                        mediaCodec4 = mediaCodec11;
                                                                                        i39 = i972;
                                                                                        outputSurface7 = outputSurface7;
                                                                                    }
                                                                                    j13 = j18;
                                                                                    i30 = i44;
                                                                                    audioRecoder2 = audioRecoder;
                                                                                    z11 = z17;
                                                                                    inputBuffers = byteBufferArr2;
                                                                                    outputSurface4 = outputSurface;
                                                                                    mediaCodec7 = mediaCodec9;
                                                                                    i35 = i43;
                                                                                    bufferInfo3 = bufferInfo4;
                                                                                    i9 = i45;
                                                                                    i41 = outputSurface7;
                                                                                }
                                                                                z2 = z;
                                                                                audioRecoder = audioRecoder2;
                                                                                i7 = i15;
                                                                                i77 = i27;
                                                                                str = "x";
                                                                                i8 = i14;
                                                                                i2 = i29;
                                                                                z7 = false;
                                                                                z5 = false;
                                                                                this.extractor.unselectTrack(i13);
                                                                                if (mediaCodec7 != null) {
                                                                                }
                                                                                mediaCodec3 = mediaCodec7;
                                                                                outputSurface2 = outputSurface4;
                                                                            } catch (Throwable th78) {
                                                                                th = th78;
                                                                                z2 = z;
                                                                                th = th;
                                                                                outputSurface = outputSurface4;
                                                                                mediaCodec2 = mediaCodec4;
                                                                                i74 = i15;
                                                                                i77 = i27;
                                                                                str = "x";
                                                                                i75 = i14;
                                                                                i2 = i29;
                                                                                inputSurface = inputSurface2;
                                                                                i3 = -5;
                                                                                mediaCodec = null;
                                                                                z5 = false;
                                                                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                                FileLog.e(th);
                                                                                mediaExtractor = this.extractor;
                                                                                if (mediaExtractor != null) {
                                                                                }
                                                                                mP4Builder = this.mediaMuxer;
                                                                                if (mP4Builder != null) {
                                                                                }
                                                                                if (mediaCodec2 != null) {
                                                                                }
                                                                                if (mediaCodec != null) {
                                                                                }
                                                                                if (outputSurface != null) {
                                                                                }
                                                                                if (inputSurface != null) {
                                                                                }
                                                                                str3 = str2;
                                                                                z6 = z5;
                                                                                z7 = true;
                                                                                if (z6) {
                                                                                }
                                                                            }
                                                                        } catch (Throwable th79) {
                                                                            th = th79;
                                                                            z2 = z;
                                                                            th = th;
                                                                            outputSurface = outputSurface4;
                                                                            mediaCodec2 = mediaCodec4;
                                                                            i74 = i15;
                                                                            i77 = i27;
                                                                            str = "x";
                                                                            i75 = i14;
                                                                            i2 = i29;
                                                                            inputSurface = inputSurface2;
                                                                            i3 = -5;
                                                                            mediaCodec = null;
                                                                            z5 = false;
                                                                            FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                                                            FileLog.e(th);
                                                                            mediaExtractor = this.extractor;
                                                                            if (mediaExtractor != null) {
                                                                            }
                                                                            mP4Builder = this.mediaMuxer;
                                                                            if (mP4Builder != null) {
                                                                            }
                                                                            if (mediaCodec2 != null) {
                                                                            }
                                                                            if (mediaCodec != null) {
                                                                            }
                                                                            if (outputSurface != null) {
                                                                            }
                                                                            if (inputSurface != null) {
                                                                            }
                                                                            str3 = str2;
                                                                            z6 = z5;
                                                                            z7 = true;
                                                                            if (z6) {
                                                                            }
                                                                        }
                                                                    } else {
                                                                        i14 = i14;
                                                                    }
                                                                }
                                                                bufferInfo3 = bufferInfo2;
                                                                i34 = 0;
                                                                outputSurface4 = outputSurface6;
                                                                mediaCodec7 = getDecoderByFormat(mediaFormat);
                                                                mediaCodec7.configure(mediaFormat, outputSurface4.getSurface(), (MediaCrypto) null, i34);
                                                                mediaCodec7.start();
                                                                if (i23 >= 21) {
                                                                }
                                                                ?? mP4Builder422 = new MP4Builder();
                                                                ?? equals22 = this.outputMimeType.equals(obj2);
                                                                this.mediaMuxer = mP4Builder422.createMovie(mp4Movie2, z10, equals22);
                                                                if (i30 < 0) {
                                                                }
                                                                if (audioRecoder2 != null) {
                                                                }
                                                                checkConversionCanceled();
                                                                byteBufferArr = outputBuffers;
                                                                i39 = i37;
                                                                z11 = z49;
                                                                byteBuffer2 = byteBuffer;
                                                                j13 = -2147483648L;
                                                                j14 = -1;
                                                                j15 = -1;
                                                                j16 = 0;
                                                                i9 = -5;
                                                                j17 = 0;
                                                                z12 = false;
                                                                z13 = false;
                                                                z14 = false;
                                                                i40 = 0;
                                                                z15 = true;
                                                                i41 = i34;
                                                                while (true) {
                                                                    if (z12) {
                                                                    }
                                                                    checkConversionCanceled();
                                                                    if (audioRecoder2 == null) {
                                                                    }
                                                                    if (z13) {
                                                                    }
                                                                    z18 = !z14;
                                                                    j18 = j13;
                                                                    i45 = i42;
                                                                    z19 = true;
                                                                    outputSurface7 = i41;
                                                                    while (true) {
                                                                        if (!z18) {
                                                                        }
                                                                        checkConversionCanceled();
                                                                        z20 = z18;
                                                                        if (z) {
                                                                        }
                                                                        i2 = mediaCodec11.dequeueOutputBuffer(bufferInfo4, j19);
                                                                        if (i2 != -1) {
                                                                        }
                                                                        if (i47 == i51) {
                                                                        }
                                                                        int i9722 = i46;
                                                                        mediaCodec4 = mediaCodec11;
                                                                        i39 = i9722;
                                                                        outputSurface7 = outputSurface7;
                                                                    }
                                                                    j13 = j18;
                                                                    i30 = i44;
                                                                    audioRecoder2 = audioRecoder;
                                                                    z11 = z17;
                                                                    inputBuffers = byteBufferArr2;
                                                                    outputSurface4 = outputSurface;
                                                                    mediaCodec7 = mediaCodec9;
                                                                    i35 = i43;
                                                                    bufferInfo3 = bufferInfo4;
                                                                    i9 = i45;
                                                                    i41 = outputSurface7;
                                                                }
                                                                z2 = z;
                                                                audioRecoder = audioRecoder2;
                                                                i7 = i15;
                                                                i77 = i27;
                                                                str = "x";
                                                                i8 = i14;
                                                                i2 = i29;
                                                                z7 = false;
                                                                z5 = false;
                                                                this.extractor.unselectTrack(i13);
                                                                if (mediaCodec7 != null) {
                                                                }
                                                                mediaCodec3 = mediaCodec7;
                                                                outputSurface2 = outputSurface4;
                                                            }
                                                        }
                                                        if (i23 >= 24) {
                                                            if (hDRInfo.getHDRType() != 0) {
                                                            }
                                                        }
                                                        mediaCodec4 = mediaCodec14;
                                                        outputSurface6 = outputSurface5;
                                                        inputSurface2 = inputSurface;
                                                        f2 = f4;
                                                        i30 = i6;
                                                        str2 = name;
                                                        j8 = j31;
                                                        obj2 = obj;
                                                        i31 = i28;
                                                        z10 = z38;
                                                        j9 = j6;
                                                        j10 = j3;
                                                        bufferInfo2 = bufferInfo;
                                                        j11 = j7;
                                                        mediaFormat = trackFormat;
                                                        i32 = i26;
                                                        if (!z41) {
                                                        }
                                                        bufferInfo3 = bufferInfo2;
                                                        i34 = 0;
                                                        outputSurface4 = outputSurface6;
                                                        mediaCodec7 = getDecoderByFormat(mediaFormat);
                                                        mediaCodec7.configure(mediaFormat, outputSurface4.getSurface(), (MediaCrypto) null, i34);
                                                        mediaCodec7.start();
                                                        if (i23 >= 21) {
                                                        }
                                                        ?? mP4Builder4222 = new MP4Builder();
                                                        ?? equals222 = this.outputMimeType.equals(obj2);
                                                        this.mediaMuxer = mP4Builder4222.createMovie(mp4Movie2, z10, equals222);
                                                        if (i30 < 0) {
                                                        }
                                                        if (audioRecoder2 != null) {
                                                        }
                                                        checkConversionCanceled();
                                                        byteBufferArr = outputBuffers;
                                                        i39 = i37;
                                                        z11 = z49;
                                                        byteBuffer2 = byteBuffer;
                                                        j13 = -2147483648L;
                                                        j14 = -1;
                                                        j15 = -1;
                                                        j16 = 0;
                                                        i9 = -5;
                                                        j17 = 0;
                                                        z12 = false;
                                                        z13 = false;
                                                        z14 = false;
                                                        i40 = 0;
                                                        z15 = true;
                                                        i41 = i34;
                                                        while (true) {
                                                            if (z12) {
                                                            }
                                                            checkConversionCanceled();
                                                            if (audioRecoder2 == null) {
                                                            }
                                                            if (z13) {
                                                            }
                                                            z18 = !z14;
                                                            j18 = j13;
                                                            i45 = i42;
                                                            z19 = true;
                                                            outputSurface7 = i41;
                                                            while (true) {
                                                                if (!z18) {
                                                                }
                                                                checkConversionCanceled();
                                                                z20 = z18;
                                                                if (z) {
                                                                }
                                                                i2 = mediaCodec11.dequeueOutputBuffer(bufferInfo4, j19);
                                                                if (i2 != -1) {
                                                                }
                                                                if (i47 == i51) {
                                                                }
                                                                int i97222 = i46;
                                                                mediaCodec4 = mediaCodec11;
                                                                i39 = i97222;
                                                                outputSurface7 = outputSurface7;
                                                            }
                                                            j13 = j18;
                                                            i30 = i44;
                                                            audioRecoder2 = audioRecoder;
                                                            z11 = z17;
                                                            inputBuffers = byteBufferArr2;
                                                            outputSurface4 = outputSurface;
                                                            mediaCodec7 = mediaCodec9;
                                                            i35 = i43;
                                                            bufferInfo3 = bufferInfo4;
                                                            i9 = i45;
                                                            i41 = outputSurface7;
                                                        }
                                                        z2 = z;
                                                        audioRecoder = audioRecoder2;
                                                        i7 = i15;
                                                        i77 = i27;
                                                        str = "x";
                                                        i8 = i14;
                                                        i2 = i29;
                                                        z7 = false;
                                                        z5 = false;
                                                        this.extractor.unselectTrack(i13);
                                                        if (mediaCodec7 != null) {
                                                        }
                                                        mediaCodec3 = mediaCodec7;
                                                        outputSurface2 = outputSurface4;
                                                    }
                                                } else {
                                                    i26 = i22;
                                                }
                                                i27 = i19;
                                                name = mediaCodec14.getName();
                                                z4 = "c2.qti.avc.encoder".equalsIgnoreCase(name);
                                                FileLog.d("selected encoder " + name);
                                                mediaCodec14.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                                                i28 = i21;
                                                inputSurface = new InputSurface(mediaCodec14.createInputSurface());
                                                inputSurface.makeCurrent();
                                                mediaCodec14.start();
                                                i14 = i4;
                                                i29 = i76;
                                                i15 = i74;
                                                outputSurface5 = new OutputSurface(savedFilterState, null, str15, str16, arrayList, cropState3, i15, i14, i72, i73, i20, i76, false, num, num2, hDRInfo, convertVideoParams);
                                                if (hDRInfo == null) {
                                                }
                                                if (i23 >= 24) {
                                                }
                                                mediaCodec4 = mediaCodec14;
                                                outputSurface6 = outputSurface5;
                                                inputSurface2 = inputSurface;
                                                f2 = f4;
                                                i30 = i6;
                                                str2 = name;
                                                j8 = j31;
                                                obj2 = obj;
                                                i31 = i28;
                                                z10 = z38;
                                                j9 = j6;
                                                j10 = j3;
                                                bufferInfo2 = bufferInfo;
                                                j11 = j7;
                                                mediaFormat = trackFormat;
                                                i32 = i26;
                                                if (!z41) {
                                                }
                                                bufferInfo3 = bufferInfo2;
                                                i34 = 0;
                                                outputSurface4 = outputSurface6;
                                                mediaCodec7 = getDecoderByFormat(mediaFormat);
                                                mediaCodec7.configure(mediaFormat, outputSurface4.getSurface(), (MediaCrypto) null, i34);
                                                mediaCodec7.start();
                                                if (i23 >= 21) {
                                                }
                                                ?? mP4Builder42222 = new MP4Builder();
                                                ?? equals2222 = this.outputMimeType.equals(obj2);
                                                this.mediaMuxer = mP4Builder42222.createMovie(mp4Movie2, z10, equals2222);
                                                if (i30 < 0) {
                                                }
                                                if (audioRecoder2 != null) {
                                                }
                                                checkConversionCanceled();
                                                byteBufferArr = outputBuffers;
                                                i39 = i37;
                                                z11 = z49;
                                                byteBuffer2 = byteBuffer;
                                                j13 = -2147483648L;
                                                j14 = -1;
                                                j15 = -1;
                                                j16 = 0;
                                                i9 = -5;
                                                j17 = 0;
                                                z12 = false;
                                                z13 = false;
                                                z14 = false;
                                                i40 = 0;
                                                z15 = true;
                                                i41 = i34;
                                                while (true) {
                                                    if (z12) {
                                                    }
                                                    checkConversionCanceled();
                                                    if (audioRecoder2 == null) {
                                                    }
                                                    if (z13) {
                                                    }
                                                    z18 = !z14;
                                                    j18 = j13;
                                                    i45 = i42;
                                                    z19 = true;
                                                    outputSurface7 = i41;
                                                    while (true) {
                                                        if (!z18) {
                                                        }
                                                        checkConversionCanceled();
                                                        z20 = z18;
                                                        if (z) {
                                                        }
                                                        i2 = mediaCodec11.dequeueOutputBuffer(bufferInfo4, j19);
                                                        if (i2 != -1) {
                                                        }
                                                        if (i47 == i51) {
                                                        }
                                                        int i972222 = i46;
                                                        mediaCodec4 = mediaCodec11;
                                                        i39 = i972222;
                                                        outputSurface7 = outputSurface7;
                                                    }
                                                    j13 = j18;
                                                    i30 = i44;
                                                    audioRecoder2 = audioRecoder;
                                                    z11 = z17;
                                                    inputBuffers = byteBufferArr2;
                                                    outputSurface4 = outputSurface;
                                                    mediaCodec7 = mediaCodec9;
                                                    i35 = i43;
                                                    bufferInfo3 = bufferInfo4;
                                                    i9 = i45;
                                                    i41 = outputSurface7;
                                                }
                                                z2 = z;
                                                audioRecoder = audioRecoder2;
                                                i7 = i15;
                                                i77 = i27;
                                                str = "x";
                                                i8 = i14;
                                                i2 = i29;
                                                z7 = false;
                                                z5 = false;
                                                this.extractor.unselectTrack(i13);
                                                if (mediaCodec7 != null) {
                                                }
                                                mediaCodec3 = mediaCodec7;
                                                outputSurface2 = outputSurface4;
                                            }
                                        } else {
                                            str9 = "mime";
                                            mp4Movie2 = mp4Movie;
                                            i24 = 0;
                                            i25 = 0;
                                        }
                                        z9 = false;
                                        if (i23 >= 23) {
                                        }
                                        i27 = i19;
                                        name = mediaCodec14.getName();
                                        z4 = "c2.qti.avc.encoder".equalsIgnoreCase(name);
                                        FileLog.d("selected encoder " + name);
                                        mediaCodec14.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                                        i28 = i21;
                                        inputSurface = new InputSurface(mediaCodec14.createInputSurface());
                                        inputSurface.makeCurrent();
                                        mediaCodec14.start();
                                        i14 = i4;
                                        i29 = i76;
                                        i15 = i74;
                                        outputSurface5 = new OutputSurface(savedFilterState, null, str15, str16, arrayList, cropState3, i15, i14, i72, i73, i20, i76, false, num, num2, hDRInfo, convertVideoParams);
                                        if (hDRInfo == null) {
                                        }
                                        if (i23 >= 24) {
                                        }
                                        mediaCodec4 = mediaCodec14;
                                        outputSurface6 = outputSurface5;
                                        inputSurface2 = inputSurface;
                                        f2 = f4;
                                        i30 = i6;
                                        str2 = name;
                                        j8 = j31;
                                        obj2 = obj;
                                        i31 = i28;
                                        z10 = z38;
                                        j9 = j6;
                                        j10 = j3;
                                        bufferInfo2 = bufferInfo;
                                        j11 = j7;
                                        mediaFormat = trackFormat;
                                        i32 = i26;
                                        if (!z41) {
                                        }
                                        bufferInfo3 = bufferInfo2;
                                        i34 = 0;
                                        outputSurface4 = outputSurface6;
                                        mediaCodec7 = getDecoderByFormat(mediaFormat);
                                        mediaCodec7.configure(mediaFormat, outputSurface4.getSurface(), (MediaCrypto) null, i34);
                                        mediaCodec7.start();
                                        if (i23 >= 21) {
                                        }
                                        ?? mP4Builder422222 = new MP4Builder();
                                        ?? equals22222 = this.outputMimeType.equals(obj2);
                                        this.mediaMuxer = mP4Builder422222.createMovie(mp4Movie2, z10, equals22222);
                                        if (i30 < 0) {
                                        }
                                        if (audioRecoder2 != null) {
                                        }
                                        checkConversionCanceled();
                                        byteBufferArr = outputBuffers;
                                        i39 = i37;
                                        z11 = z49;
                                        byteBuffer2 = byteBuffer;
                                        j13 = -2147483648L;
                                        j14 = -1;
                                        j15 = -1;
                                        j16 = 0;
                                        i9 = -5;
                                        j17 = 0;
                                        z12 = false;
                                        z13 = false;
                                        z14 = false;
                                        i40 = 0;
                                        z15 = true;
                                        i41 = i34;
                                        while (true) {
                                            if (z12) {
                                            }
                                            checkConversionCanceled();
                                            if (audioRecoder2 == null) {
                                            }
                                            if (z13) {
                                            }
                                            z18 = !z14;
                                            j18 = j13;
                                            i45 = i42;
                                            z19 = true;
                                            outputSurface7 = i41;
                                            while (true) {
                                                if (!z18) {
                                                }
                                                checkConversionCanceled();
                                                z20 = z18;
                                                if (z) {
                                                }
                                                i2 = mediaCodec11.dequeueOutputBuffer(bufferInfo4, j19);
                                                if (i2 != -1) {
                                                }
                                                if (i47 == i51) {
                                                }
                                                int i9722222 = i46;
                                                mediaCodec4 = mediaCodec11;
                                                i39 = i9722222;
                                                outputSurface7 = outputSurface7;
                                            }
                                            j13 = j18;
                                            i30 = i44;
                                            audioRecoder2 = audioRecoder;
                                            z11 = z17;
                                            inputBuffers = byteBufferArr2;
                                            outputSurface4 = outputSurface;
                                            mediaCodec7 = mediaCodec9;
                                            i35 = i43;
                                            bufferInfo3 = bufferInfo4;
                                            i9 = i45;
                                            i41 = outputSurface7;
                                        }
                                        z2 = z;
                                        audioRecoder = audioRecoder2;
                                        i7 = i15;
                                        i77 = i27;
                                        str = "x";
                                        i8 = i14;
                                        i2 = i29;
                                        z7 = false;
                                        z5 = false;
                                        this.extractor.unselectTrack(i13);
                                        if (mediaCodec7 != null) {
                                        }
                                        mediaCodec3 = mediaCodec7;
                                        outputSurface2 = outputSurface4;
                                    }
                                    createByCodecName = null;
                                    if (createByCodecName == null) {
                                    }
                                    MediaCodec mediaCodec142 = createByCodecName;
                                    if (BuildVars.LOGS_ENABLED) {
                                    }
                                    createVideoFormat = MediaFormat.createVideoFormat(this.outputMimeType, i21, i22);
                                    createVideoFormat.setInteger("color-format", 2130708361);
                                    createVideoFormat.setInteger("bitrate", i19);
                                    if (z44) {
                                        createVideoFormat.setInteger("bitrate-mode", 2);
                                    }
                                    createVideoFormat.setInteger("max-bitrate", i19);
                                    createVideoFormat.setInteger("frame-rate", i76);
                                    createVideoFormat.setInteger("i-frame-interval", 1);
                                    i23 = Build.VERSION.SDK_INT;
                                    if (i23 >= 24) {
                                    }
                                    z9 = false;
                                    if (i23 >= 23) {
                                    }
                                    i27 = i19;
                                    name = mediaCodec142.getName();
                                    z4 = "c2.qti.avc.encoder".equalsIgnoreCase(name);
                                    FileLog.d("selected encoder " + name);
                                    mediaCodec142.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                                    i28 = i21;
                                    inputSurface = new InputSurface(mediaCodec142.createInputSurface());
                                    inputSurface.makeCurrent();
                                    mediaCodec142.start();
                                    i14 = i4;
                                    i29 = i76;
                                    i15 = i74;
                                    outputSurface5 = new OutputSurface(savedFilterState, null, str15, str16, arrayList, cropState3, i15, i14, i72, i73, i20, i76, false, num, num2, hDRInfo, convertVideoParams);
                                    if (hDRInfo == null) {
                                    }
                                    if (i23 >= 24) {
                                    }
                                    mediaCodec4 = mediaCodec142;
                                    outputSurface6 = outputSurface5;
                                    inputSurface2 = inputSurface;
                                    f2 = f4;
                                    i30 = i6;
                                    str2 = name;
                                    j8 = j31;
                                    obj2 = obj;
                                    i31 = i28;
                                    z10 = z38;
                                    j9 = j6;
                                    j10 = j3;
                                    bufferInfo2 = bufferInfo;
                                    j11 = j7;
                                    mediaFormat = trackFormat;
                                    i32 = i26;
                                    if (!z41) {
                                    }
                                    bufferInfo3 = bufferInfo2;
                                    i34 = 0;
                                    outputSurface4 = outputSurface6;
                                    mediaCodec7 = getDecoderByFormat(mediaFormat);
                                    mediaCodec7.configure(mediaFormat, outputSurface4.getSurface(), (MediaCrypto) null, i34);
                                    mediaCodec7.start();
                                    if (i23 >= 21) {
                                    }
                                    ?? mP4Builder4222222 = new MP4Builder();
                                    ?? equals222222 = this.outputMimeType.equals(obj2);
                                    this.mediaMuxer = mP4Builder4222222.createMovie(mp4Movie2, z10, equals222222);
                                    if (i30 < 0) {
                                    }
                                    if (audioRecoder2 != null) {
                                    }
                                    checkConversionCanceled();
                                    byteBufferArr = outputBuffers;
                                    i39 = i37;
                                    z11 = z49;
                                    byteBuffer2 = byteBuffer;
                                    j13 = -2147483648L;
                                    j14 = -1;
                                    j15 = -1;
                                    j16 = 0;
                                    i9 = -5;
                                    j17 = 0;
                                    z12 = false;
                                    z13 = false;
                                    z14 = false;
                                    i40 = 0;
                                    z15 = true;
                                    i41 = i34;
                                    while (true) {
                                        if (z12) {
                                        }
                                        checkConversionCanceled();
                                        if (audioRecoder2 == null) {
                                        }
                                        if (z13) {
                                        }
                                        z18 = !z14;
                                        j18 = j13;
                                        i45 = i42;
                                        z19 = true;
                                        outputSurface7 = i41;
                                        while (true) {
                                            if (!z18) {
                                            }
                                            checkConversionCanceled();
                                            z20 = z18;
                                            if (z) {
                                            }
                                            i2 = mediaCodec11.dequeueOutputBuffer(bufferInfo4, j19);
                                            if (i2 != -1) {
                                            }
                                            if (i47 == i51) {
                                            }
                                            int i97222222 = i46;
                                            mediaCodec4 = mediaCodec11;
                                            i39 = i97222222;
                                            outputSurface7 = outputSurface7;
                                        }
                                        j13 = j18;
                                        i30 = i44;
                                        audioRecoder2 = audioRecoder;
                                        z11 = z17;
                                        inputBuffers = byteBufferArr2;
                                        outputSurface4 = outputSurface;
                                        mediaCodec7 = mediaCodec9;
                                        i35 = i43;
                                        bufferInfo3 = bufferInfo4;
                                        i9 = i45;
                                        i41 = outputSurface7;
                                    }
                                    z2 = z;
                                    audioRecoder = audioRecoder2;
                                    i7 = i15;
                                    i77 = i27;
                                    str = "x";
                                    i8 = i14;
                                    i2 = i29;
                                    z7 = false;
                                    z5 = false;
                                    this.extractor.unselectTrack(i13);
                                    if (mediaCodec7 != null) {
                                    }
                                    mediaCodec3 = mediaCodec7;
                                    outputSurface2 = outputSurface4;
                                }
                            }
                            if (cropState3 == null) {
                            }
                            i20 = i71;
                            i21 = i74;
                            i22 = i4;
                            if (str8 != null) {
                            }
                            createByCodecName = null;
                            if (createByCodecName == null) {
                            }
                            MediaCodec mediaCodec1422 = createByCodecName;
                            if (BuildVars.LOGS_ENABLED) {
                            }
                            createVideoFormat = MediaFormat.createVideoFormat(this.outputMimeType, i21, i22);
                            createVideoFormat.setInteger("color-format", 2130708361);
                            createVideoFormat.setInteger("bitrate", i19);
                            if (z44) {
                            }
                            createVideoFormat.setInteger("max-bitrate", i19);
                            createVideoFormat.setInteger("frame-rate", i76);
                            createVideoFormat.setInteger("i-frame-interval", 1);
                            i23 = Build.VERSION.SDK_INT;
                            if (i23 >= 24) {
                            }
                            z9 = false;
                            if (i23 >= 23) {
                            }
                            i27 = i19;
                            name = mediaCodec1422.getName();
                            z4 = "c2.qti.avc.encoder".equalsIgnoreCase(name);
                            FileLog.d("selected encoder " + name);
                            mediaCodec1422.configure(createVideoFormat, (Surface) null, (MediaCrypto) null, 1);
                            i28 = i21;
                            inputSurface = new InputSurface(mediaCodec1422.createInputSurface());
                            inputSurface.makeCurrent();
                            mediaCodec1422.start();
                            i14 = i4;
                            i29 = i76;
                            i15 = i74;
                            outputSurface5 = new OutputSurface(savedFilterState, null, str15, str16, arrayList, cropState3, i15, i14, i72, i73, i20, i76, false, num, num2, hDRInfo, convertVideoParams);
                            if (hDRInfo == null) {
                            }
                            if (i23 >= 24) {
                            }
                            mediaCodec4 = mediaCodec1422;
                            outputSurface6 = outputSurface5;
                            inputSurface2 = inputSurface;
                            f2 = f4;
                            i30 = i6;
                            str2 = name;
                            j8 = j31;
                            obj2 = obj;
                            i31 = i28;
                            z10 = z38;
                            j9 = j6;
                            j10 = j3;
                            bufferInfo2 = bufferInfo;
                            j11 = j7;
                            mediaFormat = trackFormat;
                            i32 = i26;
                            if (!z41) {
                            }
                            bufferInfo3 = bufferInfo2;
                            i34 = 0;
                            outputSurface4 = outputSurface6;
                            mediaCodec7 = getDecoderByFormat(mediaFormat);
                            mediaCodec7.configure(mediaFormat, outputSurface4.getSurface(), (MediaCrypto) null, i34);
                            mediaCodec7.start();
                            if (i23 >= 21) {
                            }
                            ?? mP4Builder42222222 = new MP4Builder();
                            ?? equals2222222 = this.outputMimeType.equals(obj2);
                            this.mediaMuxer = mP4Builder42222222.createMovie(mp4Movie2, z10, equals2222222);
                            if (i30 < 0) {
                            }
                            if (audioRecoder2 != null) {
                            }
                            checkConversionCanceled();
                            byteBufferArr = outputBuffers;
                            i39 = i37;
                            z11 = z49;
                            byteBuffer2 = byteBuffer;
                            j13 = -2147483648L;
                            j14 = -1;
                            j15 = -1;
                            j16 = 0;
                            i9 = -5;
                            j17 = 0;
                            z12 = false;
                            z13 = false;
                            z14 = false;
                            i40 = 0;
                            z15 = true;
                            i41 = i34;
                            while (true) {
                                if (z12) {
                                }
                                checkConversionCanceled();
                                if (audioRecoder2 == null) {
                                }
                                if (z13) {
                                }
                                z18 = !z14;
                                j18 = j13;
                                i45 = i42;
                                z19 = true;
                                outputSurface7 = i41;
                                while (true) {
                                    if (!z18) {
                                    }
                                    checkConversionCanceled();
                                    z20 = z18;
                                    if (z) {
                                    }
                                    i2 = mediaCodec11.dequeueOutputBuffer(bufferInfo4, j19);
                                    if (i2 != -1) {
                                    }
                                    if (i47 == i51) {
                                    }
                                    int i972222222 = i46;
                                    mediaCodec4 = mediaCodec11;
                                    i39 = i972222222;
                                    outputSurface7 = outputSurface7;
                                }
                                j13 = j18;
                                i30 = i44;
                                audioRecoder2 = audioRecoder;
                                z11 = z17;
                                inputBuffers = byteBufferArr2;
                                outputSurface4 = outputSurface;
                                mediaCodec7 = mediaCodec9;
                                i35 = i43;
                                bufferInfo3 = bufferInfo4;
                                i9 = i45;
                                i41 = outputSurface7;
                            }
                            z2 = z;
                            audioRecoder = audioRecoder2;
                            i7 = i15;
                            i77 = i27;
                            str = "x";
                            i8 = i14;
                            i2 = i29;
                            z7 = false;
                            z5 = false;
                            this.extractor.unselectTrack(i13);
                            if (mediaCodec7 != null) {
                            }
                            mediaCodec3 = mediaCodec7;
                            outputSurface2 = outputSurface4;
                        } else {
                            z2 = z;
                            i2 = i76;
                            i7 = i74;
                            i8 = i4;
                            str = "x";
                            i77 = i77;
                            mediaCodec3 = null;
                            outputSurface2 = null;
                            z7 = false;
                            i9 = -5;
                            str2 = null;
                            mediaCodec4 = null;
                            audioRecoder = null;
                            z4 = false;
                            z5 = false;
                            inputSurface2 = null;
                        }
                        if (outputSurface2 != null) {
                            try {
                                outputSurface2.release();
                                outputSurface2 = null;
                            } catch (Throwable th80) {
                                th = th80;
                                mediaCodec = mediaCodec3;
                                outputSurface = outputSurface2;
                                i74 = i7;
                                i3 = i9;
                                mediaCodec2 = mediaCodec4;
                                inputSurface = inputSurface2;
                                i75 = i8;
                            }
                        }
                        if (inputSurface2 != null) {
                            inputSurface2.release();
                            inputSurface2 = null;
                        }
                        if (mediaCodec4 != null) {
                            mediaCodec4.stop();
                            mediaCodec4.release();
                            mediaCodec4 = null;
                        }
                        if (audioRecoder != null) {
                            audioRecoder.release();
                        }
                        checkConversionCanceled();
                        mediaCodec5 = mediaCodec3;
                        i4 = i8;
                        i74 = i7;
                        z6 = z5;
                        int i882222222222222222222222222222 = i9;
                        outputSurface3 = outputSurface2;
                        i10 = i882222222222222222222222222222;
                        mediaExtractor2 = this.extractor;
                        if (mediaExtractor2 != null) {
                            mediaExtractor2.release();
                        }
                        mP4Builder2 = this.mediaMuxer;
                        if (mP4Builder2 != null) {
                            try {
                                mP4Builder2.finishMovie();
                                this.endPresentationTime = this.mediaMuxer.getLastFrameTimestamp(i10);
                            } catch (Throwable th81) {
                                FileLog.e(th81);
                            }
                        }
                        if (mediaCodec4 != null) {
                            try {
                                mediaCodec4.release();
                            } catch (Exception unused10) {
                            }
                        }
                        if (mediaCodec5 != null) {
                            try {
                                mediaCodec5.release();
                            } catch (Exception unused11) {
                            }
                        }
                        if (outputSurface3 != null) {
                            try {
                                outputSurface3.release();
                            } catch (Exception unused12) {
                            }
                        }
                        if (inputSurface2 != null) {
                            try {
                                inputSurface2.release();
                            } catch (Exception unused13) {
                            }
                        }
                        str3 = str2;
                        i75 = i4;
                        if (z6) {
                        }
                    } else {
                        try {
                            try {
                                MP4Builder createMovie = new MP4Builder().createMovie(mp4Movie, z38, false);
                                this.mediaMuxer = createMovie;
                                MediaExtractor mediaExtractor5 = this.extractor;
                                if (i77 == -1 || z42) {
                                    j27 = j31;
                                    z30 = false;
                                } else {
                                    j27 = j31;
                                    z30 = true;
                                }
                                z3 = z39;
                                file = file2;
                                readAndWriteTracks(mediaExtractor5, createMovie, bufferInfo, r3, j29, j27, file, z30);
                                z2 = z;
                                i77 = i77;
                                i74 = i74;
                                i10 = -5;
                                z7 = false;
                                outputSurface3 = null;
                                str2 = null;
                                mediaCodec4 = null;
                                mediaCodec5 = null;
                                z6 = false;
                                z4 = false;
                                inputSurface2 = null;
                                mediaExtractor2 = this.extractor;
                                if (mediaExtractor2 != null) {
                                }
                                mP4Builder2 = this.mediaMuxer;
                                if (mP4Builder2 != null) {
                                }
                                if (mediaCodec4 != null) {
                                }
                                if (mediaCodec5 != null) {
                                }
                                if (outputSurface3 != null) {
                                }
                                if (inputSurface2 != null) {
                                }
                                str3 = str2;
                                i75 = i4;
                            } catch (Throwable th82) {
                                th = th82;
                                z3 = z39;
                                file = file2;
                                z2 = z;
                                th = th;
                                str = "x";
                                i75 = i4;
                                i2 = i76;
                                mediaCodec2 = null;
                                inputSurface = null;
                                i3 = -5;
                                str2 = null;
                                mediaCodec = null;
                                z4 = false;
                                z5 = false;
                                outputSurface = null;
                                FileLog.e("bitrate: " + i77 + " framerate: " + i2 + " size: " + i75 + str + i74);
                                FileLog.e(th);
                                mediaExtractor = this.extractor;
                                if (mediaExtractor != null) {
                                }
                                mP4Builder = this.mediaMuxer;
                                if (mP4Builder != null) {
                                }
                                if (mediaCodec2 != null) {
                                }
                                if (mediaCodec != null) {
                                }
                                if (outputSurface != null) {
                                }
                                if (inputSurface != null) {
                                }
                                str3 = str2;
                                z6 = z5;
                                z7 = true;
                                if (z6) {
                                }
                            }
                        } catch (Throwable th83) {
                            th = th83;
                            z3 = z39;
                            file = file2;
                        }
                        if (z6) {
                        }
                    }
                }
            } else {
                i6 = i5;
            }
            z8 = false;
            if (!z39) {
            }
            long j352 = j29;
            z3 = z39;
            file = file2;
            if (findTrack < 0) {
            }
            if (outputSurface2 != null) {
            }
            if (inputSurface2 != null) {
            }
            if (mediaCodec4 != null) {
            }
            if (audioRecoder != null) {
            }
            checkConversionCanceled();
            mediaCodec5 = mediaCodec3;
            i4 = i8;
            i74 = i7;
            z6 = z5;
            int i8822222222222222222222222222222 = i9;
            outputSurface3 = outputSurface2;
            i10 = i8822222222222222222222222222222;
            mediaExtractor2 = this.extractor;
            if (mediaExtractor2 != null) {
            }
            mP4Builder2 = this.mediaMuxer;
            if (mP4Builder2 != null) {
            }
            if (mediaCodec4 != null) {
            }
            if (mediaCodec5 != null) {
            }
            if (outputSurface3 != null) {
            }
            if (inputSurface2 != null) {
            }
            str3 = str2;
            i75 = i4;
            if (z6) {
            }
        }
    }

    private static void applyAudioInputs(ArrayList<MixedSoundInfo> arrayList, ArrayList<AudioInput> arrayList2) throws IOException {
        for (int i = 0; i < arrayList.size(); i++) {
            MixedSoundInfo mixedSoundInfo = arrayList.get(i);
            GeneralAudioInput generalAudioInput = new GeneralAudioInput(mixedSoundInfo.audioFile);
            generalAudioInput.setVolume(mixedSoundInfo.volume);
            long j = mixedSoundInfo.startTime;
            if (j > 0) {
                generalAudioInput.setStartOffsetUs(j);
            }
            long j2 = mixedSoundInfo.audioOffset;
            if (j2 > 0) {
                generalAudioInput.setStartTimeUs(j2);
            } else {
                j2 = 0;
            }
            long j3 = mixedSoundInfo.duration;
            if (j3 > 0) {
                generalAudioInput.setEndTimeUs(j2 + j3);
            }
            arrayList2.add(generalAudioInput);
        }
    }

    private MediaCodec createEncoderForMimeType() throws IOException {
        MediaCodec createEncoderByType;
        if (this.outputMimeType.equals("video/hevc") && Build.VERSION.SDK_INT >= 29) {
            String findGoodHevcEncoder = SharedConfig.findGoodHevcEncoder();
            createEncoderByType = findGoodHevcEncoder != null ? MediaCodec.createByCodecName(findGoodHevcEncoder) : null;
        } else {
            this.outputMimeType = MediaController.VIDEO_MIME_TYPE;
            createEncoderByType = MediaCodec.createEncoderByType(MediaController.VIDEO_MIME_TYPE);
        }
        if (createEncoderByType == null && this.outputMimeType.equals("video/hevc")) {
            this.outputMimeType = MediaController.VIDEO_MIME_TYPE;
            return MediaCodec.createEncoderByType(MediaController.VIDEO_MIME_TYPE);
        }
        return createEncoderByType;
    }

    public static void cutOfNalData(String str, ByteBuffer byteBuffer, MediaCodec.BufferInfo bufferInfo) {
        int i = str.equals("video/hevc") ? 3 : 1;
        if (bufferInfo.size > 100) {
            byteBuffer.position(bufferInfo.offset);
            byte[] bArr = new byte[100];
            byteBuffer.get(bArr);
            int i2 = 0;
            for (int i3 = 0; i3 < 96; i3++) {
                if (bArr[i3] == 0 && bArr[i3 + 1] == 0 && bArr[i3 + 2] == 0 && bArr[i3 + 3] == 1 && (i2 = i2 + 1) > i) {
                    bufferInfo.offset += i3;
                    bufferInfo.size -= i3;
                    return;
                }
            }
        }
    }

    private boolean isMediatekAvcEncoder(MediaCodec mediaCodec) {
        return mediaCodec.getName().equals("c2.mtk.avc.encoder");
    }

    /* JADX WARN: Code restructure failed: missing block: B:73:0x0123, code lost:
        if (r9[r6 + 3] != 1) goto L62;
     */
    /* JADX WARN: Removed duplicated region for block: B:119:0x01d0  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x01d5  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x01ec  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00df  */
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
        int i9;
        boolean z2;
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
            i3 = mP4Builder.addTrack(trackFormat, false);
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
            i2 = i14;
        } else {
            i2 = 0;
            i3 = -1;
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
                    i2 = Math.max(trackFormat2.getInteger("max-input-size"), i2);
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
        if (i2 <= 0) {
            i2 = 65536;
        }
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(i2);
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
                    if (sampleSize > i2) {
                        int i16 = (int) (sampleSize + 1024);
                        i2 = i16;
                        allocateDirect = ByteBuffer.allocateDirect(i16);
                    }
                } else {
                    i5 = i;
                }
                bufferInfo.size = mediaExtractor.readSampleData(allocateDirect, 0);
                int sampleTrackIndex = mediaExtractor.getSampleTrackIndex();
                if (sampleTrackIndex == findTrack) {
                    i6 = i5;
                    i8 = i3;
                } else {
                    i6 = i5;
                    if (sampleTrackIndex == i6) {
                        i8 = i4;
                    } else {
                        i7 = -1;
                        i8 = -1;
                        if (i8 == i7) {
                            if (i15 < 21) {
                                allocateDirect.position(0);
                                allocateDirect.limit(bufferInfo.size);
                            }
                            if (sampleTrackIndex != i6 && (array = allocateDirect.array()) != null) {
                                int arrayOffset = allocateDirect.arrayOffset();
                                int limit = arrayOffset + allocateDirect.limit();
                                i9 = i4;
                                int i17 = arrayOffset;
                                int i18 = -1;
                                while (true) {
                                    z2 = z4;
                                    int i19 = limit - 4;
                                    if (i17 > i19) {
                                        break;
                                    }
                                    if (array[i17] == 0 && array[i17 + 1] == 0 && array[i17 + 2] == 0) {
                                        i12 = i2;
                                        i13 = i6;
                                    } else {
                                        i12 = i2;
                                        i13 = i6;
                                    }
                                    if (i17 != i19) {
                                        i17++;
                                        z4 = z2;
                                        i6 = i13;
                                        i2 = i12;
                                    }
                                    if (i18 != -1) {
                                        int i20 = (i17 - i18) - (i17 == i19 ? 0 : 4);
                                        array[i18] = (byte) (i20 >> 24);
                                        array[i18 + 1] = (byte) (i20 >> 16);
                                        array[i18 + 2] = (byte) (i20 >> 8);
                                        array[i18 + 3] = (byte) i20;
                                    }
                                    i18 = i17;
                                    i17++;
                                    z4 = z2;
                                    i6 = i13;
                                    i2 = i12;
                                }
                            } else {
                                i9 = i4;
                                z2 = z4;
                            }
                            i10 = i2;
                            i11 = i6;
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
                                    long writeSampleData = mP4Builder.writeSampleData(i8, allocateDirect, bufferInfo, false);
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
                            i10 = i2;
                            i11 = i6;
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
                        i2 = i10;
                    }
                }
                i7 = -1;
                if (i8 == i7) {
                }
                if (!z3) {
                }
                i4 = i9;
                i = i11;
                i2 = i10;
            }
            int i21 = i;
            if (findTrack >= 0) {
                mediaExtractor.unselectTrack(findTrack);
            }
            if (i21 >= 0) {
                mediaExtractor.unselectTrack(i21);
            }
            return j7;
        }
        return -1L;
    }

    private void checkConversionCanceled() {
        MediaController.VideoConvertorListener videoConvertorListener = this.callback;
        if (videoConvertorListener != null && videoConvertorListener.checkConversionCanceled()) {
            throw new ConversionCanceledException();
        }
    }

    private static String hdrFragmentShader(int i, int i2, int i3, int i4, boolean z, StoryEntry.HDRInfo hDRInfo) {
        String readRes;
        if (z) {
            if (hDRInfo.getHDRType() == 1) {
                readRes = RLottieDrawable.readRes(null, R.raw.yuv_hlg2rgb);
            } else {
                readRes = RLottieDrawable.readRes(null, R.raw.yuv_pq2rgb);
            }
            String replace = readRes.replace("$dstWidth", i3 + ".0");
            String replace2 = replace.replace("$dstHeight", i4 + ".0");
            return replace2 + "\nin vec2 vTextureCoord;\nout vec4 fragColor;\nvoid main() {\n    fragColor = TEX(vTextureCoord);\n}";
        }
        return "#version 320 es\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform sampler2D sTexture;\nout vec4 fragColor;\nvoid main() {\nfragColor = texture(sTexture, vTextureCoord);\n}\n";
    }

    private static String createFragmentShader(int i, int i2, int i3, int i4, boolean z, int i5) {
        int clamp = (int) Utilities.clamp((Math.max(i, i2) / Math.max(i4, i3)) * 0.8f, 2.0f, 1.0f);
        if (clamp > 1 && SharedConfig.deviceIsAverage()) {
            clamp = 1;
        }
        int min = Math.min(i5, clamp);
        FileLog.d("source size " + i + "x" + i2 + "    dest size " + i3 + i4 + "   kernelRadius " + min);
        if (z) {
            return "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nconst float kernel = " + min + ".0;\nconst float pixelSizeX = 1.0 / " + i + ".0;\nconst float pixelSizeY = 1.0 / " + i2 + ".0;\nuniform samplerExternalOES sTexture;\nvoid main() {\nvec3 accumulation = vec3(0);\nvec3 weightsum = vec3(0);\nfor (float x = -kernel; x <= kernel; x++){\n   for (float y = -kernel; y <= kernel; y++){\n       accumulation += texture2D(sTexture, vTextureCoord + vec2(x * pixelSizeX, y * pixelSizeY)).xyz;\n       weightsum += 1.0;\n   }\n}\ngl_FragColor = vec4(accumulation / weightsum, 1.0);\n}\n";
        }
        return "precision mediump float;\nvarying vec2 vTextureCoord;\nconst float kernel = " + min + ".0;\nconst float pixelSizeX = 1.0 / " + i2 + ".0;\nconst float pixelSizeY = 1.0 / " + i + ".0;\nuniform sampler2D sTexture;\nvoid main() {\nvec3 accumulation = vec3(0);\nvec3 weightsum = vec3(0);\nfor (float x = -kernel; x <= kernel; x++){\n   for (float y = -kernel; y <= kernel; y++){\n       accumulation += texture2D(sTexture, vTextureCoord + vec2(x * pixelSizeX, y * pixelSizeY)).xyz;\n       weightsum += 1.0;\n   }\n}\ngl_FragColor = vec4(accumulation / weightsum, 1.0);\n}\n";
    }

    /* loaded from: classes3.dex */
    public class ConversionCanceledException extends RuntimeException {
        public ConversionCanceledException() {
            super("canceled conversion");
        }
    }

    private MediaCodec getDecoderByFormat(MediaFormat mediaFormat) {
        if (mediaFormat == null) {
            throw new RuntimeException("getDecoderByFormat: format is null");
        }
        ArrayList arrayList = new ArrayList();
        String string = mediaFormat.getString("mime");
        arrayList.add(string);
        if ("video/dolby-vision".equals(string)) {
            arrayList.add("video/hevc");
            arrayList.add(MediaController.VIDEO_MIME_TYPE);
        }
        Exception exc = null;
        while (!arrayList.isEmpty()) {
            try {
                String str = (String) arrayList.remove(0);
                mediaFormat.setString("mime", str);
                return MediaCodec.createDecoderByType(str);
            } catch (Exception e) {
                if (exc == null) {
                    exc = e;
                }
            }
        }
        throw new RuntimeException(exc);
    }

    /* loaded from: classes3.dex */
    public static class ConvertVideoParams {
        int account;
        long avatarStartTime;
        String backgroundPath;
        int bitrate;
        String blurPath;
        File cacheFile;
        MediaController.VideoConvertorListener callback;
        MediaController.CropState cropState;
        long duration;
        long endTime;
        int framerate;
        Integer gradientBottomColor;
        Integer gradientTopColor;
        StoryEntry.HDRInfo hdrInfo;
        boolean isDark;
        boolean isPhoto;
        boolean isRound;
        boolean isSecret;
        boolean isStory;
        ArrayList<VideoEditedInfo.MediaEntity> mediaEntities;
        String messagePath;
        String messageVideoMaskPath;
        boolean muted;
        boolean needCompress;
        int originalBitrate;
        int originalHeight;
        int originalWidth;
        String paintPath;
        int resultHeight;
        int resultWidth;
        int rotationValue;
        MediaController.SavedFilterState savedFilterState;
        public ArrayList<MixedSoundInfo> soundInfos = new ArrayList<>();
        long startTime;
        String videoPath;
        float volume;
        long wallpaperPeerId;

        private ConvertVideoParams() {
        }

        public static ConvertVideoParams of(String str, File file, int i, boolean z, int i2, int i3, int i4, int i5, int i6, int i7, int i8, long j, long j2, long j3, boolean z2, long j4, MediaController.VideoConvertorListener videoConvertorListener, VideoEditedInfo videoEditedInfo) {
            ConvertVideoParams convertVideoParams = new ConvertVideoParams();
            convertVideoParams.videoPath = str;
            convertVideoParams.cacheFile = file;
            convertVideoParams.rotationValue = i;
            convertVideoParams.isSecret = z;
            convertVideoParams.originalWidth = i2;
            convertVideoParams.originalHeight = i3;
            convertVideoParams.resultWidth = i4;
            convertVideoParams.resultHeight = i5;
            convertVideoParams.framerate = i6;
            convertVideoParams.bitrate = i7;
            convertVideoParams.originalBitrate = i8;
            convertVideoParams.startTime = j;
            convertVideoParams.endTime = j2;
            convertVideoParams.avatarStartTime = j3;
            convertVideoParams.needCompress = z2;
            convertVideoParams.duration = j4;
            convertVideoParams.savedFilterState = videoEditedInfo.filterState;
            convertVideoParams.paintPath = videoEditedInfo.paintPath;
            convertVideoParams.blurPath = videoEditedInfo.blurPath;
            convertVideoParams.mediaEntities = videoEditedInfo.mediaEntities;
            convertVideoParams.isPhoto = videoEditedInfo.isPhoto;
            convertVideoParams.cropState = videoEditedInfo.cropState;
            convertVideoParams.isRound = videoEditedInfo.roundVideo;
            convertVideoParams.callback = videoConvertorListener;
            convertVideoParams.gradientTopColor = videoEditedInfo.gradientTopColor;
            convertVideoParams.gradientBottomColor = videoEditedInfo.gradientBottomColor;
            convertVideoParams.muted = videoEditedInfo.muted;
            convertVideoParams.volume = videoEditedInfo.volume;
            convertVideoParams.isStory = videoEditedInfo.isStory;
            convertVideoParams.hdrInfo = videoEditedInfo.hdrInfo;
            convertVideoParams.isDark = videoEditedInfo.isDark;
            convertVideoParams.wallpaperPeerId = videoEditedInfo.wallpaperPeerId;
            convertVideoParams.account = videoEditedInfo.account;
            convertVideoParams.messagePath = videoEditedInfo.messagePath;
            convertVideoParams.messageVideoMaskPath = videoEditedInfo.messageVideoMaskPath;
            convertVideoParams.backgroundPath = videoEditedInfo.backgroundPath;
            return convertVideoParams;
        }
    }

    /* loaded from: classes3.dex */
    public static class MixedSoundInfo {
        final String audioFile;
        public long audioOffset;
        public long duration;
        public long startTime;
        public float volume = 1.0f;

        public MixedSoundInfo(String str) {
            this.audioFile = str;
        }
    }
}
