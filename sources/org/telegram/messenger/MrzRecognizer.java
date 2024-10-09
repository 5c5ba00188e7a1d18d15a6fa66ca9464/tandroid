package org.telegram.messenger;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseArray;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import java.util.Calendar;
import java.util.HashMap;

/* loaded from: classes3.dex */
public class MrzRecognizer {

    /* loaded from: classes3.dex */
    public static class Result {
        public static final int GENDER_FEMALE = 2;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_UNKNOWN = 0;
        public static final int TYPE_DRIVER_LICENSE = 4;
        public static final int TYPE_ID = 2;
        public static final int TYPE_INTERNAL_PASSPORT = 3;
        public static final int TYPE_PASSPORT = 1;
        public int birthDay;
        public int birthMonth;
        public int birthYear;
        public boolean doesNotExpire;
        public int expiryDay;
        public int expiryMonth;
        public int expiryYear;
        public String firstName;
        public int gender;
        public String issuingCountry;
        public String lastName;
        public boolean mainCheckDigitIsValid;
        public String middleName;
        public String nationality;
        public String number;
        public String rawMRZ;
        public int type;
    }

    private static native Rect[][] binarizeAndFindCharacters(Bitmap bitmap, Bitmap bitmap2);

    private static String capitalize(String str) {
        if (str == null) {
            return null;
        }
        char[] charArray = str.toCharArray();
        boolean z = true;
        for (int i = 0; i < charArray.length; i++) {
            if (z || !Character.isLetter(charArray[i])) {
                z = charArray[i] == ' ';
            } else {
                charArray[i] = Character.toLowerCase(charArray[i]);
            }
        }
        return new String(charArray);
    }

    private static int checksum(String str) {
        char[] charArray = str.toCharArray();
        int[] iArr = {7, 3, 1};
        int i = 0;
        for (int i2 = 0; i2 < charArray.length; i2++) {
            char c = charArray[i2];
            i += ((c < '0' || c > '9') ? (c < 'A' || c > 'Z') ? 0 : c - '7' : c - '0') * iArr[i2 % 3];
        }
        return i % 10;
    }

    private static String cyrillicToLatin(String str) {
        String[] strArr = {"A", "B", "V", "G", "D", "E", "E", "ZH", "Z", "I", "I", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "F", "KH", "TS", "CH", "SH", "SHCH", "IE", "Y", "", "E", "IU", "IA"};
        String str2 = str;
        int i = 0;
        while (i < 33) {
            int i2 = i + 1;
            str2 = str2.replace("АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".substring(i, i2), strArr[i]);
            i = i2;
        }
        return str2;
    }

    private static native int[] findCornerPoints(Bitmap bitmap);

    private static HashMap<String, String> getCountriesMap() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("AFG", "AF");
        hashMap.put("ALA", "AX");
        hashMap.put("ALB", "AL");
        hashMap.put("DZA", "DZ");
        hashMap.put("ASM", "AS");
        hashMap.put("AND", "AD");
        hashMap.put("AGO", "AO");
        hashMap.put("AIA", "AI");
        hashMap.put("ATA", "AQ");
        hashMap.put("ATG", "AG");
        hashMap.put("ARG", "AR");
        hashMap.put("ARM", "AM");
        hashMap.put("ABW", "AW");
        hashMap.put("AUS", "AU");
        hashMap.put("AUT", "AT");
        hashMap.put("AZE", "AZ");
        hashMap.put("BHS", "BS");
        hashMap.put("BHR", "BH");
        hashMap.put("BGD", "BD");
        hashMap.put("BRB", "BB");
        hashMap.put("BLR", "BY");
        hashMap.put("BEL", "BE");
        hashMap.put("BLZ", "BZ");
        hashMap.put("BEN", "BJ");
        hashMap.put("BMU", "BM");
        hashMap.put("BTN", "BT");
        hashMap.put("BOL", "BO");
        hashMap.put("BES", "BQ");
        hashMap.put("BIH", "BA");
        hashMap.put("BWA", "BW");
        hashMap.put("BVT", "BV");
        hashMap.put("BRA", "BR");
        hashMap.put("IOT", "IO");
        hashMap.put("BRN", "BN");
        hashMap.put("BGR", "BG");
        hashMap.put("BFA", "BF");
        hashMap.put("BDI", "BI");
        hashMap.put("CPV", "CV");
        hashMap.put("KHM", "KH");
        hashMap.put("CMR", "CM");
        hashMap.put("CAN", "CA");
        hashMap.put("CYM", "KY");
        hashMap.put("CAF", "CF");
        hashMap.put("TCD", "TD");
        hashMap.put("CHL", "CL");
        hashMap.put("CHN", "CN");
        hashMap.put("CXR", "CX");
        hashMap.put("CCK", "CC");
        hashMap.put("COL", "CO");
        hashMap.put("COM", "KM");
        hashMap.put("COG", "CG");
        hashMap.put("COD", "CD");
        hashMap.put("COK", "CK");
        hashMap.put("CRI", "CR");
        hashMap.put("CIV", "CI");
        hashMap.put("HRV", "HR");
        hashMap.put("CUB", "CU");
        hashMap.put("CUW", "CW");
        hashMap.put("CYP", "CY");
        hashMap.put("CZE", "CZ");
        hashMap.put("DNK", "DK");
        hashMap.put("DJI", "DJ");
        hashMap.put("DMA", "DM");
        hashMap.put("DOM", "DO");
        hashMap.put("ECU", "EC");
        hashMap.put("EGY", "EG");
        hashMap.put("SLV", "SV");
        hashMap.put("GNQ", "GQ");
        hashMap.put("ERI", "ER");
        hashMap.put("EST", "EE");
        hashMap.put("ETH", "ET");
        hashMap.put("FLK", "FK");
        hashMap.put("FRO", "FO");
        hashMap.put("FJI", "FJ");
        hashMap.put("FIN", "FI");
        hashMap.put("FRA", "FR");
        hashMap.put("GUF", "GF");
        hashMap.put("PYF", "PF");
        hashMap.put("ATF", "TF");
        hashMap.put("GAB", "GA");
        hashMap.put("GMB", "GM");
        hashMap.put("GEO", "GE");
        hashMap.put("D<<", "DE");
        hashMap.put("GHA", "GH");
        hashMap.put("GIB", "GI");
        hashMap.put("GRC", "GR");
        hashMap.put("GRL", "GL");
        hashMap.put("GRD", "GD");
        hashMap.put("GLP", "GP");
        hashMap.put("GUM", "GU");
        hashMap.put("GTM", "GT");
        hashMap.put("GGY", "GG");
        hashMap.put("GIN", "GN");
        hashMap.put("GNB", "GW");
        hashMap.put("GUY", "GY");
        hashMap.put("HTI", "HT");
        hashMap.put("HMD", "HM");
        hashMap.put("VAT", "VA");
        hashMap.put("HND", "HN");
        hashMap.put("HKG", "HK");
        hashMap.put("HUN", "HU");
        hashMap.put("ISL", "IS");
        hashMap.put("IND", "IN");
        hashMap.put("IDN", "ID");
        hashMap.put("IRN", "IR");
        hashMap.put("IRQ", "IQ");
        hashMap.put("IRL", "IE");
        hashMap.put("IMN", "IM");
        hashMap.put("ISR", "IL");
        hashMap.put("ITA", "IT");
        hashMap.put("JAM", "JM");
        hashMap.put("JPN", "JP");
        hashMap.put("JEY", "JE");
        hashMap.put("JOR", "JO");
        hashMap.put("KAZ", "KZ");
        hashMap.put("KEN", "KE");
        hashMap.put("KIR", "KI");
        hashMap.put("PRK", "KP");
        hashMap.put("KOR", "KR");
        hashMap.put("KWT", "KW");
        hashMap.put("KGZ", "KG");
        hashMap.put("LAO", "LA");
        hashMap.put("LVA", "LV");
        hashMap.put("LBN", "LB");
        hashMap.put("LSO", "LS");
        hashMap.put("LBR", "LR");
        hashMap.put("LBY", "LY");
        hashMap.put("LIE", "LI");
        hashMap.put("LTU", "LT");
        hashMap.put("LUX", "LU");
        hashMap.put("MAC", "MO");
        hashMap.put("MKD", "MK");
        hashMap.put("MDG", "MG");
        hashMap.put("MWI", "MW");
        hashMap.put("MYS", "MY");
        hashMap.put("MDV", "MV");
        hashMap.put("MLI", "ML");
        hashMap.put("MLT", "MT");
        hashMap.put("MHL", "MH");
        hashMap.put("MTQ", "MQ");
        hashMap.put("MRT", "MR");
        hashMap.put("MUS", "MU");
        hashMap.put("MYT", "YT");
        hashMap.put("MEX", "MX");
        hashMap.put("FSM", "FM");
        hashMap.put("MDA", "MD");
        hashMap.put("MCO", "MC");
        hashMap.put("MNG", "MN");
        hashMap.put("MNE", "ME");
        hashMap.put("MSR", "MS");
        hashMap.put("MAR", "MA");
        hashMap.put("MOZ", "MZ");
        hashMap.put("MMR", "MM");
        hashMap.put("NAM", "NA");
        hashMap.put("NRU", "NR");
        hashMap.put("NPL", "NP");
        hashMap.put("NLD", "NL");
        hashMap.put("NCL", "NC");
        hashMap.put("NZL", "NZ");
        hashMap.put("NIC", "NI");
        hashMap.put("NER", "NE");
        hashMap.put("NGA", "NG");
        hashMap.put("NIU", "NU");
        hashMap.put("NFK", "NF");
        hashMap.put("MNP", "MP");
        hashMap.put("NOR", "NO");
        hashMap.put("OMN", "OM");
        hashMap.put("PAK", "PK");
        hashMap.put("PLW", "PW");
        hashMap.put("PSE", "PS");
        hashMap.put("PAN", "PA");
        hashMap.put("PNG", "PG");
        hashMap.put("PRY", "PY");
        hashMap.put("PER", "PE");
        hashMap.put("PHL", "PH");
        hashMap.put("PCN", "PN");
        hashMap.put("POL", "PL");
        hashMap.put("PRT", "PT");
        hashMap.put("PRI", "PR");
        hashMap.put("QAT", "QA");
        hashMap.put("REU", "RE");
        hashMap.put("ROU", "RO");
        hashMap.put("RUS", "RU");
        hashMap.put("RWA", "RW");
        hashMap.put("BLM", "BL");
        hashMap.put("SHN", "SH");
        hashMap.put("KNA", "KN");
        hashMap.put("LCA", "LC");
        hashMap.put("MAF", "MF");
        hashMap.put("SPM", "PM");
        hashMap.put("VCT", "VC");
        hashMap.put("WSM", "WS");
        hashMap.put("SMR", "SM");
        hashMap.put("STP", "ST");
        hashMap.put("SAU", "SA");
        hashMap.put("SEN", "SN");
        hashMap.put("SRB", "RS");
        hashMap.put("SYC", "SC");
        hashMap.put("SLE", "SL");
        hashMap.put("SGP", "SG");
        hashMap.put("SXM", "SX");
        hashMap.put("SVK", "SK");
        hashMap.put("SVN", "SI");
        hashMap.put("SLB", "SB");
        hashMap.put("SOM", "SO");
        hashMap.put("ZAF", "ZA");
        hashMap.put("SGS", "GS");
        hashMap.put("SSD", "SS");
        hashMap.put("ESP", "ES");
        hashMap.put("LKA", "LK");
        hashMap.put("SDN", "SD");
        hashMap.put("SUR", "SR");
        hashMap.put("SJM", "SJ");
        hashMap.put("SWZ", "SZ");
        hashMap.put("SWE", "SE");
        hashMap.put("CHE", "CH");
        hashMap.put("SYR", "SY");
        hashMap.put("TWN", "TW");
        hashMap.put("TJK", "TJ");
        hashMap.put("TZA", "TZ");
        hashMap.put("THA", "TH");
        hashMap.put("TLS", "TL");
        hashMap.put("TGO", "TG");
        hashMap.put("TKL", "TK");
        hashMap.put("TON", "TO");
        hashMap.put("TTO", "TT");
        hashMap.put("TUN", "TN");
        hashMap.put("TUR", "TR");
        hashMap.put("TKM", "TM");
        hashMap.put("TCA", "TC");
        hashMap.put("TUV", "TV");
        hashMap.put("UGA", "UG");
        hashMap.put("UKR", "UA");
        hashMap.put("ARE", "AE");
        hashMap.put("GBR", "GB");
        hashMap.put("USA", "US");
        hashMap.put("UMI", "UM");
        hashMap.put("URY", "UY");
        hashMap.put("UZB", "UZ");
        hashMap.put("VUT", "VU");
        hashMap.put("VEN", "VE");
        hashMap.put("VNM", "VN");
        hashMap.put("VGB", "VG");
        hashMap.put("VIR", "VI");
        hashMap.put("WLF", "WF");
        hashMap.put("ESH", "EH");
        hashMap.put("YEM", "YE");
        hashMap.put("ZMB", "ZM");
        hashMap.put("ZWE", "ZW");
        return hashMap;
    }

    private static int getNumber(char c) {
        if (c == 'O') {
            return 0;
        }
        if (c == 'I') {
            return 1;
        }
        if (c == 'B') {
            return 8;
        }
        return c - '0';
    }

    private static void parseBirthDate(String str, Result result) {
        try {
            int parseInt = Integer.parseInt(str.substring(0, 2));
            result.birthYear = parseInt;
            result.birthYear += parseInt < (Calendar.getInstance().get(1) % 100) + (-5) ? 2000 : 1900;
            result.birthMonth = Integer.parseInt(str.substring(2, 4));
            result.birthDay = Integer.parseInt(str.substring(4));
        } catch (NumberFormatException unused) {
        }
    }

    private static void parseExpiryDate(String str, Result result) {
        try {
            if ("<<<<<<".equals(str)) {
                result.doesNotExpire = true;
            } else {
                result.expiryYear = Integer.parseInt(str.substring(0, 2)) + 2000;
                result.expiryMonth = Integer.parseInt(str.substring(2, 4));
                result.expiryDay = Integer.parseInt(str.substring(4));
            }
        } catch (NumberFormatException unused) {
        }
    }

    private static int parseGender(char c) {
        if (c != 'F') {
            return c != 'M' ? 0 : 1;
        }
        return 2;
    }

    private static native String performRecognition(Bitmap bitmap, int i, int i2, AssetManager assetManager);

    public static Result recognize(Bitmap bitmap, boolean z) {
        Result recognizeBarcode;
        Result recognizeBarcode2;
        if (z && (recognizeBarcode2 = recognizeBarcode(bitmap)) != null) {
            return recognizeBarcode2;
        }
        try {
            Result recognizeMRZ = recognizeMRZ(bitmap);
            if (recognizeMRZ != null) {
                return recognizeMRZ;
            }
        } catch (Exception unused) {
        }
        if (z || (recognizeBarcode = recognizeBarcode(bitmap)) == null) {
            return null;
        }
        return recognizeBarcode;
    }

    public static Result recognize(byte[] bArr, int i, int i2, int i3) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        setYuvBitmapPixels(createBitmap, bArr);
        Matrix matrix = new Matrix();
        matrix.setRotate(i3);
        int min = Math.min(i, i2);
        int round = Math.round(min * 0.704f);
        boolean z = i3 == 90 || i3 == 270;
        return recognize(Bitmap.createBitmap(createBitmap, z ? (i / 2) - (round / 2) : 0, z ? 0 : (i2 / 2) - (round / 2), z ? round : min, z ? min : round, matrix, false), false);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(16:12|13|(1:15)(1:49)|16|(12:18|(1:20)|22|(2:24|(2:26|(1:28))(1:29))|30|(1:32)|33|34|(1:38)|39|(1:43)|45)(1:48)|21|22|(0)|30|(0)|33|34|(2:36|38)|39|(2:41|43)|45) */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00d1  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00ef  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Result recognizeBarcode(Bitmap bitmap) {
        String str;
        String str2;
        String str3;
        String str4;
        BarcodeDetector build = new BarcodeDetector.Builder(ApplicationLoader.applicationContext).build();
        if (bitmap.getWidth() > 1500 || bitmap.getHeight() > 1500) {
            float max = 1500.0f / Math.max(bitmap.getWidth(), bitmap.getHeight());
            bitmap = Bitmap.createScaledBitmap(bitmap, Math.round(bitmap.getWidth() * max), Math.round(bitmap.getHeight() * max), true);
        }
        SparseArray detect = build.detect(new Frame.Builder().setBitmap(bitmap).build());
        int i = 0;
        for (int i2 = 0; i2 < detect.size(); i2++) {
            Barcode barcode = (Barcode) detect.valueAt(i2);
            int i3 = barcode.valueFormat;
            int i4 = 6;
            int i5 = 4;
            if (i3 == 12 && barcode.driverLicense != null) {
                Result result = new Result();
                result.type = "ID".equals(barcode.driverLicense.documentType) ? 2 : 4;
                String str5 = barcode.driverLicense.issuingCountry;
                str5.hashCode();
                if (!str5.equals("CAN")) {
                    str4 = str5.equals("USA") ? "US" : "CA";
                    result.firstName = capitalize(barcode.driverLicense.firstName);
                    result.lastName = capitalize(barcode.driverLicense.lastName);
                    result.middleName = capitalize(barcode.driverLicense.middleName);
                    Barcode.DriverLicense driverLicense = barcode.driverLicense;
                    result.number = driverLicense.licenseNumber;
                    str = driverLicense.gender;
                    if (str != null) {
                        if (str.equals("1")) {
                            result.gender = 1;
                        } else if (str.equals("2")) {
                            result.gender = 2;
                        }
                    }
                    if ("USA".equals(result.issuingCountry)) {
                        i = 4;
                        i4 = 2;
                        i5 = 0;
                    }
                    str2 = barcode.driverLicense.birthDate;
                    if (str2 != null && str2.length() == 8) {
                        result.birthYear = Integer.parseInt(barcode.driverLicense.birthDate.substring(i, i + 4));
                        result.birthMonth = Integer.parseInt(barcode.driverLicense.birthDate.substring(i5, i5 + 2));
                        result.birthDay = Integer.parseInt(barcode.driverLicense.birthDate.substring(i4, i4 + 2));
                    }
                    str3 = barcode.driverLicense.expiryDate;
                    if (str3 != null && str3.length() == 8) {
                        result.expiryYear = Integer.parseInt(barcode.driverLicense.expiryDate.substring(i, i + 4));
                        result.expiryMonth = Integer.parseInt(barcode.driverLicense.expiryDate.substring(i5, i5 + 2));
                        result.expiryDay = Integer.parseInt(barcode.driverLicense.expiryDate.substring(i4, i4 + 2));
                    }
                    return result;
                }
                result.issuingCountry = str4;
                result.nationality = str4;
                result.firstName = capitalize(barcode.driverLicense.firstName);
                result.lastName = capitalize(barcode.driverLicense.lastName);
                result.middleName = capitalize(barcode.driverLicense.middleName);
                Barcode.DriverLicense driverLicense2 = barcode.driverLicense;
                result.number = driverLicense2.licenseNumber;
                str = driverLicense2.gender;
                if (str != null) {
                }
                if ("USA".equals(result.issuingCountry)) {
                }
                str2 = barcode.driverLicense.birthDate;
                if (str2 != null) {
                    result.birthYear = Integer.parseInt(barcode.driverLicense.birthDate.substring(i, i + 4));
                    result.birthMonth = Integer.parseInt(barcode.driverLicense.birthDate.substring(i5, i5 + 2));
                    result.birthDay = Integer.parseInt(barcode.driverLicense.birthDate.substring(i4, i4 + 2));
                }
                str3 = barcode.driverLicense.expiryDate;
                if (str3 != null) {
                    result.expiryYear = Integer.parseInt(barcode.driverLicense.expiryDate.substring(i, i + 4));
                    result.expiryMonth = Integer.parseInt(barcode.driverLicense.expiryDate.substring(i5, i5 + 2));
                    result.expiryDay = Integer.parseInt(barcode.driverLicense.expiryDate.substring(i4, i4 + 2));
                }
                return result;
            }
            if (i3 == 7 && barcode.format == 2048 && barcode.rawValue.matches("^[A-Za-z0-9=]+$")) {
                try {
                    String[] split = new String(Base64.decode(barcode.rawValue, 0), "windows-1251").split("\\|");
                    if (split.length >= 10) {
                        Result result2 = new Result();
                        result2.type = 4;
                        result2.issuingCountry = "RU";
                        result2.nationality = "RU";
                        result2.number = split[0];
                        result2.expiryYear = Integer.parseInt(split[2].substring(0, 4));
                        result2.expiryMonth = Integer.parseInt(split[2].substring(4, 6));
                        result2.expiryDay = Integer.parseInt(split[2].substring(6));
                        result2.lastName = capitalize(cyrillicToLatin(split[3]));
                        result2.firstName = capitalize(cyrillicToLatin(split[4]));
                        result2.middleName = capitalize(cyrillicToLatin(split[5]));
                        result2.birthYear = Integer.parseInt(split[6].substring(0, 4));
                        result2.birthMonth = Integer.parseInt(split[6].substring(4, 6));
                        result2.birthDay = Integer.parseInt(split[6].substring(6));
                        return result2;
                    }
                    continue;
                } catch (Exception unused) {
                    continue;
                }
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:183:0x0278 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x01f7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static Result recognizeMRZ(Bitmap bitmap) {
        float max;
        Bitmap createScaledBitmap;
        String str;
        String str2;
        int i;
        int i2;
        int i3;
        int i4;
        Matrix matrix;
        int i5;
        Bitmap bitmap2 = bitmap;
        if (bitmap.getWidth() > 512 || bitmap.getHeight() > 512) {
            max = 512.0f / Math.max(bitmap.getWidth(), bitmap.getHeight());
            createScaledBitmap = Bitmap.createScaledBitmap(bitmap2, Math.round(bitmap.getWidth() * max), Math.round(bitmap.getHeight() * max), true);
        } else {
            createScaledBitmap = bitmap2;
            max = 1.0f;
        }
        int[] findCornerPoints = findCornerPoints(createScaledBitmap);
        float f = 1.0f / max;
        if (findCornerPoints != null) {
            Point point = new Point(findCornerPoints[0], findCornerPoints[1]);
            Point point2 = new Point(findCornerPoints[2], findCornerPoints[3]);
            Point point3 = new Point(findCornerPoints[4], findCornerPoints[5]);
            Point point4 = new Point(findCornerPoints[6], findCornerPoints[7]);
            if (point2.x < point.x) {
                point3 = point4;
                point4 = point3;
            } else {
                point2 = point;
                point = point2;
            }
            double hypot = Math.hypot(point.x - point2.x, point.y - point2.y);
            double hypot2 = Math.hypot(point4.x - point3.x, point4.y - point3.y);
            str = "   ";
            str2 = "\n";
            double hypot3 = Math.hypot(point3.x - point2.x, point3.y - point2.y);
            double hypot4 = Math.hypot(point4.x - point.x, point4.y - point.y);
            double d = hypot / hypot3;
            double d2 = hypot / hypot4;
            double d3 = hypot2 / hypot3;
            double d4 = hypot2 / hypot4;
            if (d >= 1.35d && d <= 1.75d && d3 >= 1.35d && d3 <= 1.75d && d2 >= 1.35d && d2 <= 1.75d && d4 >= 1.35d && d4 <= 1.75d) {
                Bitmap createBitmap = Bitmap.createBitmap(1024, (int) Math.round(1024.0d / ((((d + d2) + d3) + d4) / 4.0d)), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(createBitmap);
                float[] fArr = {point2.x * f, point2.y * f, point.x * f, point.y * f, point4.x * f, point4.y * f, point3.x * f, point3.y * f};
                Matrix matrix2 = new Matrix();
                matrix2.setPolyToPoly(fArr, 0, new float[]{0.0f, 0.0f, createBitmap.getWidth(), 0.0f, createBitmap.getWidth(), createBitmap.getHeight(), 0.0f, createBitmap.getHeight()}, 0, 4);
                canvas.drawBitmap(bitmap2, matrix2, new Paint(2));
                bitmap2 = createBitmap;
            }
        } else {
            str = "   ";
            str2 = "\n";
            if (bitmap.getWidth() > 1500 || bitmap.getHeight() > 1500) {
                float max2 = 1500.0f / Math.max(bitmap.getWidth(), bitmap.getHeight());
                i = 1;
                bitmap2 = Bitmap.createScaledBitmap(bitmap2, Math.round(bitmap.getWidth() * max2), Math.round(bitmap.getHeight() * max2), true);
                Bitmap bitmap3 = null;
                Rect[][] rectArr = null;
                i2 = 0;
                i3 = 0;
                int i6 = 0;
                while (true) {
                    if (i2 < 3) {
                        i4 = 2;
                        break;
                    }
                    if (i2 == i) {
                        Matrix matrix3 = new Matrix();
                        matrix3.setRotate(1.0f, bitmap2.getWidth() / 2, bitmap2.getHeight() / 2);
                        matrix = matrix3;
                    } else if (i2 != 2) {
                        matrix = null;
                    } else {
                        Matrix matrix4 = new Matrix();
                        matrix4.setRotate(-1.0f, bitmap2.getWidth() / 2, bitmap2.getHeight() / 2);
                        matrix = matrix4;
                    }
                    Bitmap createBitmap2 = matrix != null ? Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true) : bitmap2;
                    bitmap3 = Bitmap.createBitmap(createBitmap2.getWidth(), createBitmap2.getHeight(), Bitmap.Config.ALPHA_8);
                    rectArr = binarizeAndFindCharacters(createBitmap2, bitmap3);
                    if (rectArr == null) {
                        return null;
                    }
                    int length = rectArr.length;
                    int i7 = 0;
                    while (i7 < length) {
                        Rect[] rectArr2 = rectArr[i7];
                        i3 = Math.max(rectArr2.length, i3);
                        if (rectArr2.length > 0) {
                            i5 = 1;
                            i6++;
                        } else {
                            i5 = 1;
                        }
                        i7 += i5;
                    }
                    i4 = 2;
                    if (i6 >= 2 && i3 >= 30) {
                        break;
                    }
                    i2++;
                    i = 1;
                }
                if (i3 >= 30 || i6 < i4) {
                    return null;
                }
                Bitmap createBitmap3 = Bitmap.createBitmap(rectArr[0].length * 10, rectArr.length * 15, Bitmap.Config.ALPHA_8);
                Canvas canvas2 = new Canvas(createBitmap3);
                Paint paint = new Paint(2);
                Rect rect = new Rect(0, 0, 10, 15);
                int length2 = rectArr.length;
                int i8 = 0;
                for (int i9 = 0; i9 < length2; i9++) {
                    Rect[] rectArr3 = rectArr[i9];
                    int length3 = rectArr3.length;
                    int i10 = 0;
                    int i11 = 0;
                    while (i10 < length3) {
                        Rect rect2 = rectArr3[i10];
                        int i12 = i11 * 10;
                        int i13 = length2;
                        int i14 = i8 * 15;
                        rect.set(i12, i14, i12 + 10, i14 + 15);
                        canvas2.drawBitmap(bitmap3, rect2, rect, paint);
                        i11++;
                        i10++;
                        length2 = i13;
                        rectArr3 = rectArr3;
                    }
                    i8++;
                }
                String performRecognition = performRecognition(createBitmap3, rectArr.length, rectArr[0].length, ApplicationLoader.applicationContext.getAssets());
                if (performRecognition == null) {
                    return null;
                }
                String str3 = str2;
                String[] split = TextUtils.split(performRecognition, str3);
                Result result = new Result();
                if (split.length < 2 || split[0].length() < 30 || split[1].length() != split[0].length()) {
                    return null;
                }
                result.rawMRZ = TextUtils.join(str3, split);
                HashMap<String, String> countriesMap = getCountriesMap();
                char charAt = split[0].charAt(0);
                if (charAt == 'P') {
                    result.type = 1;
                    if (split[0].length() == 44) {
                        result.issuingCountry = split[0].substring(2, 5);
                        int indexOf = split[0].indexOf("<<", 6);
                        if (indexOf != -1) {
                            result.lastName = split[0].substring(5, indexOf).replace('<', ' ').replace('0', 'O').trim();
                            String trim = split[0].substring(indexOf + 2).replace('<', ' ').replace('0', 'O').trim();
                            result.firstName = trim;
                            String str4 = str;
                            if (trim.contains(str4)) {
                                String str5 = result.firstName;
                                result.firstName = str5.substring(0, str5.indexOf(str4));
                            }
                        }
                        String trim2 = split[1].substring(0, 9).replace('<', ' ').replace('O', '0').trim();
                        if (checksum(trim2) == getNumber(split[1].charAt(9))) {
                            result.number = trim2;
                        }
                        result.nationality = split[1].substring(10, 13);
                        String replace = split[1].substring(13, 19).replace('O', '0').replace('I', '1');
                        if (checksum(replace) == getNumber(split[1].charAt(19))) {
                            parseBirthDate(replace, result);
                        }
                        result.gender = parseGender(split[1].charAt(20));
                        String replace2 = split[1].substring(21, 27).replace('O', '0').replace('I', '1');
                        if (checksum(replace2) == getNumber(split[1].charAt(27)) || split[1].charAt(27) == '<') {
                            parseExpiryDate(replace2, result);
                        }
                        if ("RUS".equals(result.issuingCountry) && split[0].charAt(1) == 'N') {
                            result.type = 3;
                            String[] split2 = result.firstName.split(" ");
                            result.firstName = cyrillicToLatin(russianPassportTranslit(split2[0]));
                            if (split2.length > 1) {
                                result.middleName = cyrillicToLatin(russianPassportTranslit(split2[1]));
                            }
                            result.lastName = cyrillicToLatin(russianPassportTranslit(result.lastName));
                            if (result.number != null) {
                                result.number = result.number.substring(0, 3) + split[1].charAt(28) + result.number.substring(3);
                            }
                        } else {
                            result.firstName = result.firstName.replace('8', 'B');
                            result.lastName = result.lastName.replace('8', 'B');
                        }
                        result.lastName = capitalize(result.lastName);
                        result.firstName = capitalize(result.firstName);
                        result.middleName = capitalize(result.middleName);
                    }
                } else {
                    if (charAt != 'I' && charAt != 'A' && charAt != 'C') {
                        return null;
                    }
                    result.type = 2;
                    if (split.length == 3 && split[0].length() == 30 && split[2].length() == 30) {
                        result.issuingCountry = split[0].substring(2, 5);
                        String trim3 = split[0].substring(5, 14).replace('<', ' ').replace('O', '0').trim();
                        if (checksum(trim3) == split[0].charAt(14) - '0') {
                            result.number = trim3;
                        }
                        String replace3 = split[1].substring(0, 6).replace('O', '0').replace('I', '1');
                        if (checksum(replace3) == getNumber(split[1].charAt(6))) {
                            parseBirthDate(replace3, result);
                        }
                        result.gender = parseGender(split[1].charAt(7));
                        String replace4 = split[1].substring(8, 14).replace('O', '0').replace('I', '1');
                        if (checksum(replace4) == getNumber(split[1].charAt(14)) || split[1].charAt(14) == '<') {
                            parseExpiryDate(replace4, result);
                        }
                        result.nationality = split[1].substring(15, 18);
                        int indexOf2 = split[2].indexOf("<<");
                        if (indexOf2 != -1) {
                            result.lastName = split[2].substring(0, indexOf2).replace('<', ' ').trim();
                            result.firstName = split[2].substring(indexOf2 + 2).replace('<', ' ').trim();
                        }
                    } else if (split.length == 2 && split[0].length() == 36) {
                        String substring = split[0].substring(2, 5);
                        result.issuingCountry = substring;
                        if ("FRA".equals(substring) && charAt == 'I' && split[0].charAt(1) == 'D') {
                            result.nationality = "FRA";
                            result.lastName = split[0].substring(5, 30).replace('<', ' ').trim();
                            result.firstName = split[1].substring(13, 27).replace("<<", ", ").replace('<', ' ').trim();
                            String replace5 = split[1].substring(0, 12).replace('O', '0');
                            if (checksum(replace5) == getNumber(split[1].charAt(12))) {
                                result.number = replace5;
                            }
                            String replace6 = split[1].substring(27, 33).replace('O', '0').replace('I', '1');
                            if (checksum(replace6) == getNumber(split[1].charAt(33))) {
                                parseBirthDate(replace6, result);
                            }
                            result.gender = parseGender(split[1].charAt(34));
                            result.doesNotExpire = true;
                        } else {
                            int indexOf3 = split[0].indexOf("<<");
                            if (indexOf3 != -1) {
                                result.lastName = split[0].substring(5, indexOf3).replace('<', ' ').trim();
                                result.firstName = split[0].substring(indexOf3 + 2).replace('<', ' ').trim();
                            }
                            String trim4 = split[1].substring(0, 9).replace('<', ' ').replace('O', '0').trim();
                            if (checksum(trim4) == getNumber(split[1].charAt(9))) {
                                result.number = trim4;
                            }
                            result.nationality = split[1].substring(10, 13);
                            String replace7 = split[1].substring(13, 19).replace('O', '0').replace('I', '1');
                            if (checksum(replace7) == getNumber(split[1].charAt(19))) {
                                parseBirthDate(replace7, result);
                            }
                            result.gender = parseGender(split[1].charAt(20));
                            String replace8 = split[1].substring(21, 27).replace('O', '0').replace('I', '1');
                            if (checksum(replace8) == getNumber(split[1].charAt(27)) || split[1].charAt(27) == '<') {
                                parseExpiryDate(replace8, result);
                            }
                        }
                    }
                    result.firstName = capitalize(result.firstName.replace('0', 'O').replace('8', 'B'));
                    result.lastName = capitalize(result.lastName.replace('0', 'O').replace('8', 'B'));
                }
                if (TextUtils.isEmpty(result.firstName) && TextUtils.isEmpty(result.lastName)) {
                    return null;
                }
                result.issuingCountry = countriesMap.get(result.issuingCountry);
                result.nationality = countriesMap.get(result.nationality);
                return result;
            }
        }
        i = 1;
        Bitmap bitmap32 = null;
        Rect[][] rectArr4 = null;
        i2 = 0;
        i3 = 0;
        int i62 = 0;
        while (true) {
            if (i2 < 3) {
            }
            i2++;
            i = 1;
        }
        if (i3 >= 30) {
        }
        return null;
    }

    private static String russianPassportTranslit(String str) {
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            int indexOf = "ABVGDE2JZIQKLMNOPRSTUFHC34WXY9678".indexOf(charArray[i]);
            if (indexOf != -1) {
                charArray[i] = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ".charAt(indexOf);
            }
        }
        return new String(charArray);
    }

    private static native void setYuvBitmapPixels(Bitmap bitmap, byte[] bArr);
}
