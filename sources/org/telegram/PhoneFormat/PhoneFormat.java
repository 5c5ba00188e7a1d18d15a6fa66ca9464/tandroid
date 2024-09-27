package org.telegram.PhoneFormat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.NotificationCenter;
/* loaded from: classes.dex */
public class PhoneFormat {
    private static volatile PhoneFormat Instance;
    public ByteBuffer buffer;
    public HashMap callingCodeCountries;
    public HashMap callingCodeData;
    public HashMap callingCodeOffsets;
    public HashMap countryCallingCode;
    public byte[] data;
    public String defaultCallingCode;
    public String defaultCountry;
    private boolean initialzed = false;

    public PhoneFormat() {
        init(null);
    }

    public static PhoneFormat getInstance() {
        PhoneFormat phoneFormat = Instance;
        if (phoneFormat == null) {
            synchronized (PhoneFormat.class) {
                try {
                    phoneFormat = Instance;
                    if (phoneFormat == null) {
                        phoneFormat = new PhoneFormat();
                        Instance = phoneFormat;
                    }
                } finally {
                }
            }
        }
        return phoneFormat;
    }

    public static String strip(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int length = sb.length() - 1; length >= 0; length--) {
            if (!"0123456789+*#".contains(sb.substring(length, length + 1))) {
                sb.deleteCharAt(length);
            }
        }
        return sb.toString();
    }

    public static String stripExceptNumbers(String str) {
        return stripExceptNumbers(str, false);
    }

    public static String stripExceptNumbers(String str, boolean z) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(str);
        String str2 = z ? "0123456789+" : "0123456789";
        for (int length = sb.length() - 1; length >= 0; length--) {
            if (!str2.contains(sb.substring(length, length + 1))) {
                sb.deleteCharAt(length);
            }
        }
        return sb.toString();
    }

    /* JADX WARN: Type inference failed for: r2v10, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r2v12 */
    /* JADX WARN: Type inference failed for: r2v9 */
    public CallingCodeInfo callingCodeInfo(String str) {
        Integer num;
        byte[] bArr;
        ?? r2;
        PhoneFormat phoneFormat = this;
        CallingCodeInfo callingCodeInfo = (CallingCodeInfo) phoneFormat.callingCodeData.get(str);
        if (callingCodeInfo != null || (num = (Integer) phoneFormat.callingCodeOffsets.get(str)) == null) {
            return callingCodeInfo;
        }
        byte[] bArr2 = phoneFormat.data;
        int intValue = num.intValue();
        CallingCodeInfo callingCodeInfo2 = new CallingCodeInfo();
        callingCodeInfo2.callingCode = str;
        callingCodeInfo2.countries = (ArrayList) phoneFormat.callingCodeCountries.get(str);
        phoneFormat.callingCodeData.put(str, callingCodeInfo2);
        short value16 = phoneFormat.value16(intValue);
        short value162 = phoneFormat.value16(intValue + 4);
        short value163 = phoneFormat.value16(intValue + 8);
        int i = intValue + 12;
        ArrayList arrayList = new ArrayList(5);
        while (true) {
            String valueString = phoneFormat.valueString(i);
            if (valueString.length() == 0) {
                break;
            }
            arrayList.add(valueString);
            i += valueString.length() + 1;
        }
        callingCodeInfo2.trunkPrefixes = arrayList;
        int i2 = i + 1;
        ArrayList arrayList2 = new ArrayList(5);
        while (true) {
            String valueString2 = phoneFormat.valueString(i2);
            if (valueString2.length() == 0) {
                break;
            }
            arrayList2.add(valueString2);
            i2 += valueString2.length() + 1;
        }
        callingCodeInfo2.intlPrefixes = arrayList2;
        ArrayList arrayList3 = new ArrayList(value163);
        int i3 = intValue + value16;
        int i4 = i3;
        int i5 = 0;
        while (i5 < value163) {
            RuleSet ruleSet = new RuleSet();
            ruleSet.matchLen = phoneFormat.value16(i4);
            short value164 = phoneFormat.value16(i4 + 2);
            i4 += 4;
            ArrayList arrayList4 = new ArrayList(value164);
            int i6 = 0;
            while (i6 < value164) {
                PhoneRule phoneRule = new PhoneRule();
                phoneRule.minVal = phoneFormat.value32(i4);
                phoneRule.maxVal = phoneFormat.value32(i4 + 4);
                phoneRule.byte8 = bArr2[i4 + 8];
                phoneRule.maxLen = bArr2[i4 + 9];
                phoneRule.otherFlag = bArr2[i4 + 10];
                phoneRule.prefixLen = bArr2[i4 + 11];
                phoneRule.flag12 = bArr2[i4 + 12];
                phoneRule.flag13 = bArr2[i4 + 13];
                short value165 = phoneFormat.value16(i4 + 14);
                i4 += 16;
                String valueString3 = phoneFormat.valueString(i3 + value162 + value165);
                phoneRule.format = valueString3;
                int indexOf = valueString3.indexOf("[[");
                if (indexOf != -1) {
                    bArr = bArr2;
                    r2 = 1;
                    phoneRule.format = String.format("%s%s", phoneRule.format.substring(0, indexOf), phoneRule.format.substring(phoneRule.format.indexOf("]]") + 2));
                } else {
                    bArr = bArr2;
                    r2 = 1;
                }
                arrayList4.add(phoneRule);
                if (phoneRule.hasIntlPrefix) {
                    ruleSet.hasRuleWithIntlPrefix = r2;
                }
                if (phoneRule.hasTrunkPrefix) {
                    ruleSet.hasRuleWithTrunkPrefix = r2;
                }
                i6 += r2;
                phoneFormat = this;
                bArr2 = bArr;
            }
            ruleSet.rules = arrayList4;
            arrayList3.add(ruleSet);
            i5++;
            phoneFormat = this;
            bArr2 = bArr2;
        }
        callingCodeInfo2.ruleSets = arrayList3;
        return callingCodeInfo2;
    }

    public CallingCodeInfo findCallingCodeInfo(String str) {
        CallingCodeInfo callingCodeInfo = null;
        int i = 0;
        while (i < 3 && i < str.length()) {
            i++;
            callingCodeInfo = callingCodeInfo(str.substring(0, i));
            if (callingCodeInfo != null) {
                break;
            }
        }
        return callingCodeInfo;
    }

    public String format(String str) {
        if (this.initialzed) {
            try {
                String strip = strip(str);
                if (strip.startsWith("+")) {
                    String substring = strip.substring(1);
                    CallingCodeInfo findCallingCodeInfo = findCallingCodeInfo(substring);
                    if (findCallingCodeInfo != null) {
                        String format = findCallingCodeInfo.format(substring);
                        return "+" + format;
                    }
                    return str;
                }
                CallingCodeInfo callingCodeInfo = callingCodeInfo(this.defaultCallingCode);
                if (callingCodeInfo == null) {
                    return str;
                }
                String matchingAccessCode = callingCodeInfo.matchingAccessCode(strip);
                if (matchingAccessCode != null) {
                    String substring2 = strip.substring(matchingAccessCode.length());
                    CallingCodeInfo findCallingCodeInfo2 = findCallingCodeInfo(substring2);
                    if (findCallingCodeInfo2 != null) {
                        substring2 = findCallingCodeInfo2.format(substring2);
                    }
                    return substring2.length() == 0 ? matchingAccessCode : String.format("%s %s", matchingAccessCode, substring2);
                }
                return callingCodeInfo.format(strip);
            } catch (Exception e) {
                FileLog.e(e);
                return str;
            }
        }
        return str;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00bb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x00b1 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.io.InputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void init(String str) {
        ByteArrayOutputStream byteArrayOutputStream;
        InputStream inputStream;
        ByteArrayOutputStream byteArrayOutputStream2;
        ?? r1;
        ByteArrayOutputStream byteArrayOutputStream3 = null;
        try {
            inputStream = ApplicationLoader.applicationContext.getAssets().open("PhoneFormats.dat");
            try {
                try {
                    byteArrayOutputStream2 = new ByteArrayOutputStream();
                } catch (Exception e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
                InputStream inputStream2 = inputStream;
                byteArrayOutputStream = byteArrayOutputStream3;
                byteArrayOutputStream3 = inputStream2;
                byteArrayOutputStream2 = byteArrayOutputStream;
                r1 = byteArrayOutputStream3;
                if (byteArrayOutputStream2 != null) {
                    try {
                        byteArrayOutputStream2.close();
                    } catch (Exception e2) {
                        FileLog.e(e2);
                    }
                }
                if (r1 != 0) {
                    try {
                        r1.close();
                    } catch (Exception e3) {
                        FileLog.e(e3);
                    }
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
            byteArrayOutputStream = null;
            byteArrayOutputStream2 = byteArrayOutputStream;
            r1 = byteArrayOutputStream3;
            if (byteArrayOutputStream2 != null) {
            }
            if (r1 != 0) {
            }
            throw th;
        }
        try {
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr, 0, 1024);
                if (read == -1) {
                    break;
                }
                byteArrayOutputStream2.write(bArr, 0, read);
            }
            byte[] byteArray = byteArrayOutputStream2.toByteArray();
            this.data = byteArray;
            ByteBuffer wrap = ByteBuffer.wrap(byteArray);
            this.buffer = wrap;
            wrap.order(ByteOrder.LITTLE_ENDIAN);
            try {
                byteArrayOutputStream2.close();
            } catch (Exception e5) {
                FileLog.e(e5);
            }
            try {
                inputStream.close();
            } catch (Exception e6) {
                FileLog.e(e6);
            }
            if (str == null || str.length() == 0) {
                str = Locale.getDefault().getCountry().toLowerCase();
            }
            this.defaultCountry = str;
            this.callingCodeOffsets = new HashMap((int) NotificationCenter.messagePlayingSpeedChanged);
            this.callingCodeCountries = new HashMap((int) NotificationCenter.messagePlayingSpeedChanged);
            this.callingCodeData = new HashMap(10);
            this.countryCallingCode = new HashMap((int) NotificationCenter.messagePlayingSpeedChanged);
            parseDataHeader();
            this.initialzed = true;
        } catch (Exception e7) {
            e = e7;
            byteArrayOutputStream3 = byteArrayOutputStream2;
            e.printStackTrace();
            if (byteArrayOutputStream3 != null) {
                try {
                    byteArrayOutputStream3.close();
                } catch (Exception e8) {
                    FileLog.e(e8);
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e9) {
                    FileLog.e(e9);
                }
            }
        } catch (Throwable th3) {
            th = th3;
            r1 = inputStream;
            if (byteArrayOutputStream2 != null) {
            }
            if (r1 != 0) {
            }
            throw th;
        }
    }

    public void parseDataHeader() {
        int value32 = value32(0);
        int i = 4;
        int i2 = (value32 * 12) + 4;
        for (int i3 = 0; i3 < value32; i3++) {
            String valueString = valueString(i);
            String valueString2 = valueString(i + 4);
            int value322 = value32(i + 8) + i2;
            i += 12;
            if (valueString2.equals(this.defaultCountry)) {
                this.defaultCallingCode = valueString;
            }
            this.countryCallingCode.put(valueString2, valueString);
            this.callingCodeOffsets.put(valueString, Integer.valueOf(value322));
            ArrayList arrayList = (ArrayList) this.callingCodeCountries.get(valueString);
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.callingCodeCountries.put(valueString, arrayList);
            }
            arrayList.add(valueString2);
        }
        String str = this.defaultCallingCode;
        if (str != null) {
            callingCodeInfo(str);
        }
    }

    short value16(int i) {
        if (i + 2 <= this.data.length) {
            this.buffer.position(i);
            return this.buffer.getShort();
        }
        return (short) 0;
    }

    int value32(int i) {
        if (i + 4 <= this.data.length) {
            this.buffer.position(i);
            return this.buffer.getInt();
        }
        return 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0015, code lost:
        return new java.lang.String(r2, r5, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x000c, code lost:
        r1 = r1 - r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x000d, code lost:
        if (r5 != r1) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x000f, code lost:
        return "";
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public String valueString(int i) {
        int i2 = i;
        while (true) {
            try {
                byte[] bArr = this.data;
                if (i2 >= bArr.length) {
                    return "";
                }
                if (bArr[i2] == 0) {
                    break;
                }
                i2++;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    }
}
