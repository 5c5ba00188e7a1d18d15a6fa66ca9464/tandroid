package org.telegram.ui.web;

import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.util.Base64;
import com.google.firebase.FirebaseOptions;
import java.io.File;
import java.io.FileOutputStream;
import java.net.IDN;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.TreeSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildConfig;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
/* loaded from: classes.dex */
public class SafeBrowsing {
    public static SafeBrowsing Instance;
    private static Pattern ipv4Pattern;
    private Database db;
    private boolean done;
    public final File file;
    private long updatedTime;
    private boolean updating;
    private long waitTime;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private ArrayList<String> threatTypes = null;

    public static SafeBrowsing getInstance() {
        if (Instance == null) {
            Instance = new SafeBrowsing();
        }
        return Instance;
    }

    public SafeBrowsing() {
        this.waitTime = 3600000L;
        File file = new File(ApplicationLoader.getFilesDirFixed(), "safe_browsing.dat");
        this.file = file;
        if (file.exists()) {
            try {
                Database database = new Database();
                this.db = database;
                database.readParams(new SerializedData(file), true);
            } catch (Exception e) {
                FileLog.e(e);
                this.db = null;
            }
        }
        Database database2 = this.db;
        if (database2 != null) {
            this.waitTime = database2.waitTime;
            this.updatedTime = database2.lastUpdatedTime;
            this.done = database2.done;
        }
    }

    private void save() {
        Database database = this.db;
        if (database == null) {
            return;
        }
        try {
            SerializedData serializedData = new SerializedData(database.getObjectSize());
            this.db.serializeToStream(serializedData);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(this.file);
                fileOutputStream.write(serializedData.toByteArray());
                fileOutputStream.close();
            } catch (Exception e) {
                FileLog.e(e);
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
    }

    /* loaded from: classes.dex */
    public static class HashList extends TLObject {
        public String type = "";
        public String state = "";
        public TreeSet<String> hashes = new TreeSet<>();

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData abstractSerializedData) {
            abstractSerializedData.writeString(this.type);
            abstractSerializedData.writeString(this.state);
            abstractSerializedData.writeInt32(this.hashes.size());
            Iterator<String> it = this.hashes.iterator();
            while (it.hasNext()) {
                abstractSerializedData.writeByteArray(Utilities.hexToBytes(it.next()));
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
            this.type = abstractSerializedData.readString(z);
            this.state = abstractSerializedData.readString(z);
            this.hashes.clear();
            int readInt32 = abstractSerializedData.readInt32(z);
            for (int i = 0; i < readInt32; i++) {
                this.hashes.add(Utilities.bytesToHex(abstractSerializedData.readByteArray(z)));
            }
        }
    }

    /* loaded from: classes.dex */
    public static class Database extends TLObject {
        public boolean done;
        public long lastUpdatedTime;
        public ArrayList<HashList> lists = new ArrayList<>();
        public long waitTime;

        @Override // org.telegram.tgnet.TLObject
        public void serializeToStream(AbstractSerializedData abstractSerializedData) {
            abstractSerializedData.writeInt64(this.waitTime);
            abstractSerializedData.writeInt64(this.lastUpdatedTime);
            abstractSerializedData.writeBool(this.done);
            abstractSerializedData.writeInt32(this.lists.size());
            Iterator<HashList> it = this.lists.iterator();
            while (it.hasNext()) {
                it.next().serializeToStream(abstractSerializedData);
            }
        }

        @Override // org.telegram.tgnet.TLObject
        public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
            this.waitTime = abstractSerializedData.readInt64(z);
            this.lastUpdatedTime = abstractSerializedData.readInt64(z);
            this.done = abstractSerializedData.readBool(z);
            int readInt32 = abstractSerializedData.readInt32(z);
            for (int i = 0; i < readInt32; i++) {
                HashList hashList = new HashList();
                hashList.readParams(abstractSerializedData, z);
                this.lists.add(hashList);
            }
        }
    }

    public void update() {
        update(false, 0);
    }

    private void update(boolean z, final int i) {
        PackageInfo packageInfo;
        HashList hashList;
        if (this.updating) {
            return;
        }
        if ((System.currentTimeMillis() - this.updatedTime >= this.waitTime || z) && i <= 10) {
            this.updating = true;
            if (this.threatTypes == null) {
                this.threatTypes = new ArrayList<>();
                Database database = this.db;
                if (database != null) {
                    Iterator<HashList> it = database.lists.iterator();
                    while (it.hasNext()) {
                        this.threatTypes.add(it.next().type);
                    }
                }
                if (this.threatTypes.isEmpty()) {
                    this.threatTypes = null;
                }
            }
            try {
                packageInfo = ApplicationLoader.applicationContext.getPackageManager().getPackageInfo(ApplicationLoader.applicationContext.getPackageName(), 0);
            } catch (Exception e) {
                FileLog.e(e);
                packageInfo = null;
            }
            String apiKey = FirebaseOptions.fromResource(ApplicationLoader.applicationContext).getApiKey();
            ArrayList<String> arrayList = this.threatTypes;
            String str = BuildConfig.LIBRARY_PACKAGE_NAME;
            if (arrayList == null) {
                this.threatTypes = new ArrayList<>();
                HttpGetTask header = new HttpGetTask(new Utilities.Callback() { // from class: org.telegram.ui.web.SafeBrowsing$$ExternalSyntheticLambda2
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj) {
                        SafeBrowsing.this.lambda$update$0(i, (String) obj);
                    }
                }).setHeader("Accept", "application/json");
                if (packageInfo != null) {
                    str = packageInfo.packageName;
                }
                header.setHeader("X-Android-Package", str).setHeader("X-Android-Cert", AndroidUtilities.getCertificateSHA1Fingerprint().toLowerCase()).executeOnExecutor(this.executor, "https://safebrowsing.googleapis.com/v4/threatLists?key=" + apiKey);
                return;
            }
            String str2 = packageInfo == null ? "15.0.0" : packageInfo.versionName;
            try {
                JSONObject jSONObject = new JSONObject();
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("clientId", LocaleController.getString(R.string.AppName));
                jSONObject2.put("clientVersion", str2);
                jSONObject.put("client", jSONObject2);
                JSONArray jSONArray = new JSONArray();
                int i2 = 0;
                while (i2 < this.threatTypes.size()) {
                    String str3 = this.threatTypes.get(i2);
                    JSONObject jSONObject3 = new JSONObject();
                    jSONObject3.put("threatType", str3);
                    jSONObject3.put("platformType", "ANDROID");
                    jSONObject3.put("threatEntryType", "URL");
                    if (this.db != null) {
                        for (int i3 = 0; i3 < this.db.lists.size(); i3++) {
                            if (TextUtils.equals(this.db.lists.get(i3).type, str3)) {
                                hashList = this.db.lists.get(i3);
                                break;
                            }
                        }
                    }
                    hashList = null;
                    StringBuilder sb = new StringBuilder();
                    String str4 = str;
                    sb.append("[safebrowsing] requesting ");
                    sb.append(str3);
                    sb.append(" with state ");
                    Object obj = "";
                    sb.append(hashList == null ? "" : hashList.state);
                    FileLog.d(sb.toString());
                    if (hashList != null) {
                        obj = hashList.state;
                    }
                    jSONObject3.put("state", obj);
                    JSONObject jSONObject4 = new JSONObject();
                    jSONObject4.put("maxUpdateEntries", ConnectionsManager.FileTypePhoto);
                    jSONObject4.put("maxDatabaseEntries", ConnectionsManager.FileTypePhoto);
                    Locale currentLocale = LocaleController.getInstance().getCurrentLocale();
                    if (currentLocale == null) {
                        currentLocale = Locale.getDefault();
                    }
                    jSONObject4.put("region", currentLocale.getLanguage().toUpperCase());
                    JSONArray jSONArray2 = new JSONArray();
                    jSONArray2.put("RAW");
                    jSONObject4.put("supportedCompressions", jSONArray2);
                    jSONObject3.put("constraints", jSONObject4);
                    jSONArray.put(jSONObject3);
                    i2++;
                    str = str4;
                }
                String str5 = str;
                jSONObject.put("listUpdateRequests", jSONArray);
                new HttpPostTask("application/json", jSONObject.toString(), new Utilities.Callback() { // from class: org.telegram.ui.web.SafeBrowsing$$ExternalSyntheticLambda3
                    @Override // org.telegram.messenger.Utilities.Callback
                    public final void run(Object obj2) {
                        SafeBrowsing.this.lambda$update$3(i, (String) obj2);
                    }
                }).setHeader("Accept", "application/json").setHeader("X-Android-Package", packageInfo != null ? packageInfo.packageName : str5).setHeader("X-Android-Cert", AndroidUtilities.getCertificateSHA1Fingerprint().toLowerCase()).execute("https://safebrowsing.googleapis.com/v4/threatListUpdates:fetch?key=" + apiKey);
            } catch (Exception e2) {
                FileLog.e(e2);
                this.updating = false;
                this.updatedTime = System.currentTimeMillis();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$0(int i, String str) {
        String optString;
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONArray("threatLists");
            this.threatTypes.clear();
            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i2);
                String optString2 = jSONObject.optString("platformType");
                String optString3 = jSONObject.optString("threatEntryType");
                if ("ANDROID".equals(optString2) && "URL".equals(optString3) && (optString = jSONObject.optString("threatType")) != null && !this.threatTypes.contains(optString)) {
                    this.threatTypes.add(optString);
                }
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        this.updating = false;
        update(true, i + 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$3(final int i, final String str) {
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.SafeBrowsing$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                SafeBrowsing.this.lambda$update$2(str, i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:103:0x02b0  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x02ba  */
    /* JADX WARN: Removed duplicated region for block: B:139:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public /* synthetic */ void lambda$update$2(String str, final int i) {
        boolean z;
        Database database;
        String str2;
        String str3;
        String str4 = "rawHashes";
        this.updatedTime = System.currentTimeMillis();
        if (this.db == null) {
            this.db = new Database();
        }
        int i2 = 0;
        try {
            JSONObject jSONObject = new JSONObject(str);
            try {
                String string = jSONObject.getString("minimumWaitDuration");
                if (string.endsWith("s")) {
                    this.waitTime = (long) (Double.parseDouble(string.substring(0, string.length() - 1)) * 1000.0d);
                } else if (string.endsWith("m")) {
                    this.waitTime = (long) (Double.parseDouble(string.substring(0, string.length() - 1)) * 1000.0d * 60.0d);
                } else if (string.endsWith("h")) {
                    this.waitTime = (long) (Double.parseDouble(string.substring(0, string.length() - 1)) * 1000.0d * 60.0d * 60.0d);
                }
            } catch (Exception unused) {
            }
            Database database2 = this.db;
            database2.waitTime = this.waitTime;
            database2.lastUpdatedTime = this.updatedTime;
            JSONArray jSONArray = jSONObject.getJSONArray("listUpdateResponses");
            int i3 = 0;
            z = false;
            while (i3 < jSONArray.length()) {
                try {
                    JSONObject jSONObject2 = jSONArray.getJSONObject(i3);
                    String string2 = jSONObject2.getString("threatType");
                    String string3 = jSONObject2.getString("newClientState");
                    HashList hashList = null;
                    int i4 = 0;
                    while (true) {
                        if (i4 >= this.db.lists.size()) {
                            break;
                        } else if (TextUtils.equals(this.db.lists.get(i4).type, string2)) {
                            hashList = this.db.lists.get(i4);
                            break;
                        } else {
                            i4++;
                        }
                    }
                    if (hashList == null) {
                        hashList = new HashList();
                        hashList.type = string2;
                        this.db.lists.add(hashList);
                    }
                    if (!TextUtils.equals(hashList.state, string3)) {
                        z = true;
                    }
                    try {
                        hashList.state = string3;
                        JSONArray optJSONArray = jSONObject2.optJSONArray("additions");
                        JSONArray optJSONArray2 = jSONObject2.optJSONArray("removals");
                        if (optJSONArray2 != null) {
                            int i5 = 0;
                            while (i5 < optJSONArray2.length()) {
                                JSONObject jSONObject3 = optJSONArray2.getJSONObject(i5);
                                jSONObject3.getString("compressionType");
                                JSONArray jSONArray2 = jSONObject3.getJSONObject("rawIndices").getJSONArray("indices");
                                HashSet hashSet = new HashSet();
                                JSONArray jSONArray3 = jSONArray;
                                while (i2 < jSONArray2.length()) {
                                    hashSet.add(Integer.valueOf(jSONArray2.getInt(i2)));
                                    i2++;
                                }
                                ArrayList arrayList = new ArrayList();
                                Iterator<String> it = hashList.hashes.iterator();
                                int i6 = 0;
                                while (it.hasNext()) {
                                    Iterator<String> it2 = it;
                                    String next = it.next();
                                    boolean z2 = z;
                                    try {
                                        if (hashSet.contains(Integer.valueOf(i6))) {
                                            arrayList.add(next);
                                        }
                                        i6++;
                                        z = z2;
                                        it = it2;
                                    } catch (Exception e) {
                                        e = e;
                                        z = z2;
                                        FileLog.e(e);
                                        boolean z3 = !z;
                                        this.done = z3;
                                        database = this.db;
                                        if (database != null) {
                                        }
                                        save();
                                        this.updating = false;
                                        if (z) {
                                        }
                                    }
                                }
                                boolean z4 = z;
                                Iterator it3 = arrayList.iterator();
                                while (it3.hasNext()) {
                                    hashList.hashes.remove((String) it3.next());
                                }
                                FileLog.d("[safebrowsing] update got -" + arrayList.size() + " on " + string2);
                                i5++;
                                jSONArray = jSONArray3;
                                z = z4;
                                i2 = 0;
                            }
                        }
                        JSONArray jSONArray4 = jSONArray;
                        boolean z5 = z;
                        int i7 = 2;
                        if (optJSONArray != null) {
                            z = z5;
                            int i8 = 0;
                            while (i8 < optJSONArray.length()) {
                                JSONObject jSONObject4 = optJSONArray.getJSONObject(i8);
                                jSONObject4.getString("compressionType");
                                JSONObject jSONObject5 = jSONObject4.getJSONObject(str4);
                                int optInt = jSONObject5.optInt("prefixSize", 4);
                                if (optInt <= 0) {
                                    str3 = str4;
                                } else {
                                    byte[] decode = Base64.decode(jSONObject5.getString(str4), i7);
                                    int i9 = 0;
                                    int i10 = 0;
                                    while (i9 < decode.length) {
                                        String str5 = str4;
                                        int i11 = i9 + optInt;
                                        hashList.hashes.add(Utilities.bytesToHex(Arrays.copyOfRange(decode, i9, i11)));
                                        i10++;
                                        i9 = i11;
                                        str4 = str5;
                                    }
                                    str3 = str4;
                                    FileLog.d("[safebrowsing] update got +" + i10 + " on " + string2);
                                    if (i10 == 16777216) {
                                        z = true;
                                    }
                                }
                                i8++;
                                str4 = str3;
                                i7 = 2;
                            }
                            str2 = str4;
                        } else {
                            str2 = str4;
                            z = z5;
                        }
                        String optString = jSONObject2.getJSONObject("checksum").optString("sha256");
                        if (optString != null) {
                            byte[] decode2 = Base64.decode(optString, 2);
                            ArrayList arrayList2 = new ArrayList();
                            Iterator<String> it4 = hashList.hashes.iterator();
                            int i12 = 0;
                            while (it4.hasNext()) {
                                byte[] hexToBytes = Utilities.hexToBytes(it4.next());
                                i12 += hexToBytes.length;
                                arrayList2.add(hexToBytes);
                            }
                            byte[] bArr = new byte[i12];
                            Iterator it5 = arrayList2.iterator();
                            int i13 = 0;
                            while (it5.hasNext()) {
                                byte[] bArr2 = (byte[]) it5.next();
                                System.arraycopy(bArr2, 0, bArr, i13, bArr2.length);
                                i13 += bArr2.length;
                            }
                            if (!Arrays.equals(decode2, Utilities.computeSHA256(bArr))) {
                                FileLog.e("CHECKSUM VALIDATION FAILED IN SAFE BROWSING");
                                hashList.state = "";
                                this.db.lists.remove(hashList);
                                z = true;
                            }
                        }
                        i3++;
                        jSONArray = jSONArray4;
                        str4 = str2;
                        i2 = 0;
                    } catch (Exception e2) {
                        e = e2;
                    }
                } catch (Exception e3) {
                    e = e3;
                }
            }
        } catch (Exception e4) {
            e = e4;
            z = false;
        }
        boolean z32 = !z;
        this.done = z32;
        database = this.db;
        if (database != null) {
            database.done = z32;
        }
        save();
        this.updating = false;
        if (z) {
            return;
        }
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.web.SafeBrowsing$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                SafeBrowsing.this.lambda$update$1(i);
            }
        }, 800L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$update$1(int i) {
        update(true, i + 1);
    }

    public boolean isDangerous(String str) {
        return getDangerousReason(str) != null;
    }

    public String getDangerousReason(String str) {
        ArrayList<String> expressions;
        update();
        if (this.db == null || TextUtils.isEmpty(str)) {
            return null;
        }
        if (Browser.isTonsite(str)) {
            if (Browser.isTonsitePunycode(str)) {
                return "TON_PUNYCODE";
            }
            return null;
        }
        String canonicalizeUrl = canonicalizeUrl(str);
        if (!TextUtils.isEmpty(canonicalizeUrl) && (expressions = getExpressions(canonicalizeUrl)) != null && expressions.size() > 0) {
            Iterator<String> it = expressions.iterator();
            while (it.hasNext()) {
                String substring = Utilities.bytesToHex(Utilities.computeSHA256(it.next().getBytes())).substring(0, 8);
                Iterator<HashList> it2 = this.db.lists.iterator();
                while (it2.hasNext()) {
                    HashList next = it2.next();
                    if (next.hashes.contains(substring)) {
                        return next.type;
                    }
                }
            }
        }
        return null;
    }

    private static ArrayList<String> getExpressions(String str) {
        Matcher matcher = AndroidUtilities.getURIParsePattern().matcher(str);
        if (matcher.matches()) {
            String group = matcher.group(4);
            String group2 = matcher.group(5);
            if (group2 == null) {
                return null;
            }
            if (TextUtils.isEmpty(group)) {
                ArrayList arrayList = new ArrayList(Arrays.asList(group2.split("/")));
                if (arrayList.size() <= 0) {
                    group = "";
                } else {
                    group = (String) arrayList.get(0);
                    arrayList.remove(0);
                    group2 = "/" + TextUtils.join("/", arrayList);
                }
            }
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(group);
            if (!isIP(group)) {
                String[] split = group.split("\\.");
                for (int min = Math.min(split.length - 2, 4) - 1; min >= 0; min--) {
                    StringBuilder sb = new StringBuilder();
                    for (int length = split.length - (min + 2); length < split.length; length++) {
                        if (sb.length() > 0) {
                            sb.append(".");
                        }
                        sb.append(split[length]);
                    }
                    arrayList2.add(sb.toString());
                }
            }
            ArrayList arrayList3 = new ArrayList();
            if (matcher.group(6) != null) {
                arrayList3.add(group2 + matcher.group(6));
            }
            arrayList3.add(group2);
            ArrayList arrayList4 = new ArrayList(Arrays.asList(group2.split("/")));
            if (arrayList4.size() > 0 && ((String) arrayList4.get(0)).isEmpty()) {
                arrayList4.remove(0);
            }
            for (int i = 0; i < Math.min(arrayList4.size(), 4); i++) {
                StringBuilder sb2 = new StringBuilder();
                for (int i2 = 0; i2 < i; i2++) {
                    if (sb2.length() > 0) {
                        sb2.append("/");
                    }
                    sb2.append((String) arrayList4.get(i2));
                }
                if (i < arrayList4.size()) {
                    sb2.append("/");
                }
                arrayList3.add(sb2.toString());
            }
            ArrayList<String> arrayList5 = new ArrayList<>();
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                String str2 = (String) it.next();
                Iterator it2 = arrayList3.iterator();
                while (it2.hasNext()) {
                    String str3 = (String) it2.next();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str2);
                    sb3.append(str3.startsWith("/") ? "" : "/");
                    sb3.append(str3);
                    arrayList5.add(sb3.toString());
                }
            }
            return arrayList5;
        }
        return null;
    }

    private static String canonicalizeUrl(String str) {
        String str2;
        char[] charArray;
        if (str == null) {
            return null;
        }
        String replaceAll = str.trim().replaceAll("[\t\r\n]", "");
        int indexOf = replaceAll.indexOf(35);
        if (indexOf >= 0) {
            replaceAll = replaceAll.substring(0, indexOf);
        }
        while (true) {
            try {
                str2 = URLDecoder.decode(replaceAll);
            } catch (Exception unused) {
                str2 = replaceAll;
            }
            if (TextUtils.equals(replaceAll, str2)) {
                break;
            }
            replaceAll = str2;
        }
        String replaceAll2 = str2.replaceAll("[\t\r\n]", "");
        Matcher matcher = AndroidUtilities.getURIParsePattern().matcher(replaceAll2);
        if (matcher.matches()) {
            String group = matcher.group(2);
            String group2 = matcher.group(4);
            String group3 = matcher.group(5);
            if (TextUtils.isEmpty(group2)) {
                ArrayList arrayList = new ArrayList(Arrays.asList(group3.split("/")));
                if (arrayList.size() <= 0) {
                    group2 = "";
                } else {
                    String str3 = (String) arrayList.get(0);
                    arrayList.remove(0);
                    group3 = "/" + TextUtils.join("/", arrayList);
                    group2 = str3;
                }
            }
            String resolvePath = resolvePath(group3);
            if (matcher.group(6) != null) {
                resolvePath = resolvePath + matcher.group(6);
            }
            if (matcher.group(8) != null) {
                resolvePath = resolvePath + matcher.group(8);
            }
            String parseIPv4Address = parseIPv4Address(group2.replaceAll("^\\.|\\.+$", "").replaceAll("\\.{2,}", ".").replaceAll(":\\d+$", ""));
            try {
                parseIPv4Address = IDN.toASCII(parseIPv4Address, 1);
            } catch (Exception unused2) {
            }
            if (TextUtils.isEmpty(group)) {
                group = "http";
            }
            String str4 = group + "://" + parseIPv4Address + resolvePath;
            StringBuilder sb = new StringBuilder();
            for (char c : str4.toLowerCase().toCharArray()) {
                if (c <= ' ' || c >= 127 || c == '#' || c == '%') {
                    try {
                        sb.append('%');
                        sb.append(String.format("%02X", Integer.valueOf(c)));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
        return replaceAll2;
    }

    private static boolean isIP(String str) {
        if (str == null) {
            return false;
        }
        if (ipv4Pattern == null) {
            ipv4Pattern = Pattern.compile("^(?:(?:0x[0-9a-fA-F]+|\\d+)\\.){0,3}(?:0x[0-9a-fA-F]+|\\d+)$");
        }
        return ipv4Pattern.matcher(str.toLowerCase()).matches();
    }

    private static String parseIPv4Address(String str) {
        long j;
        String lowerCase = str.toLowerCase();
        if (isIP(lowerCase)) {
            String[] split = lowerCase.split("\\.");
            long j2 = 0;
            for (int i = 0; i < Math.min(split.length, 4); i++) {
                String str2 = split[i];
                try {
                    if (str2.startsWith("0x")) {
                        j = Long.parseLong(str2.substring(2), 16);
                    } else if (str2.length() > 1 && str2.startsWith("0")) {
                        j = Long.parseLong(str2.substring(1), 8);
                    } else {
                        j = Long.parseLong(str2);
                    }
                } catch (Exception e) {
                    FileLog.e(e);
                    j = 0;
                }
                if (split.length == 1) {
                    return String.format(Locale.US, "%d.%d.%d.%d", Long.valueOf((j >> 24) & 255), Long.valueOf((j >> 16) & 255), Long.valueOf((j >> 8) & 255), Long.valueOf(j & 255));
                }
                if (j < 0 || j > 255) {
                    j = 0;
                }
                j2 = (j2 << 8) | j;
            }
            long length = j2 << ((4 - split.length) * 8);
            return String.format(Locale.US, "%d.%d.%d.%d", Long.valueOf((length >> 24) & 255), Long.valueOf((length >> 16) & 255), Long.valueOf((length >> 8) & 255), Long.valueOf(length & 255));
        }
        return str;
    }

    private static String resolvePath(String str) {
        String[] split;
        if (str == null || str.isEmpty()) {
            return "/";
        }
        LinkedList linkedList = new LinkedList();
        for (String str2 : str.split("/")) {
            if (str2.length() > 0 && !str2.equals(".")) {
                if (str2.equals("..")) {
                    if (!linkedList.isEmpty()) {
                        linkedList.removeLast();
                    }
                } else {
                    linkedList.add(str2);
                }
            }
        }
        return "/" + TextUtils.join("/", linkedList);
    }
}
