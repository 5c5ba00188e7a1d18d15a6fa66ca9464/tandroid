package com.google.zxing.qrcode.decoder;

import com.google.zxing.common.BitMatrix;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public abstract class DataMask extends Enum<DataMask> {
    private static final /* synthetic */ DataMask[] $VALUES;
    public static final DataMask DATA_MASK_000;
    public static final DataMask DATA_MASK_001;
    public static final DataMask DATA_MASK_010;
    public static final DataMask DATA_MASK_011;
    public static final DataMask DATA_MASK_100;
    public static final DataMask DATA_MASK_101;
    public static final DataMask DATA_MASK_110;
    public static final DataMask DATA_MASK_111;

    abstract boolean isMasked(int i, int i2);

    private DataMask(String str, int i) {
        super(str, i);
    }

    /* synthetic */ DataMask(String str, int i, AnonymousClass1 anonymousClass1) {
        this(str, i);
    }

    public static DataMask valueOf(String str) {
        return (DataMask) Enum.valueOf(DataMask.class, str);
    }

    public static DataMask[] values() {
        return (DataMask[]) $VALUES.clone();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.zxing.qrcode.decoder.DataMask$1 */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 extends DataMask {
        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return ((i + i2) & 1) == 0;
        }

        AnonymousClass1(String str, int i) {
            super(str, i, null);
        }
    }

    static {
        AnonymousClass1 anonymousClass1 = new AnonymousClass1("DATA_MASK_000", 0);
        DATA_MASK_000 = anonymousClass1;
        AnonymousClass2 anonymousClass2 = new AnonymousClass2("DATA_MASK_001", 1);
        DATA_MASK_001 = anonymousClass2;
        AnonymousClass3 anonymousClass3 = new AnonymousClass3("DATA_MASK_010", 2);
        DATA_MASK_010 = anonymousClass3;
        AnonymousClass4 anonymousClass4 = new AnonymousClass4("DATA_MASK_011", 3);
        DATA_MASK_011 = anonymousClass4;
        AnonymousClass5 anonymousClass5 = new AnonymousClass5("DATA_MASK_100", 4);
        DATA_MASK_100 = anonymousClass5;
        AnonymousClass6 anonymousClass6 = new AnonymousClass6("DATA_MASK_101", 5);
        DATA_MASK_101 = anonymousClass6;
        AnonymousClass7 anonymousClass7 = new AnonymousClass7("DATA_MASK_110", 6);
        DATA_MASK_110 = anonymousClass7;
        AnonymousClass8 anonymousClass8 = new AnonymousClass8("DATA_MASK_111", 7);
        DATA_MASK_111 = anonymousClass8;
        $VALUES = new DataMask[]{anonymousClass1, anonymousClass2, anonymousClass3, anonymousClass4, anonymousClass5, anonymousClass6, anonymousClass7, anonymousClass8};
    }

    /* renamed from: com.google.zxing.qrcode.decoder.DataMask$2 */
    /* loaded from: classes.dex */
    final class AnonymousClass2 extends DataMask {
        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return (i & 1) == 0;
        }

        AnonymousClass2(String str, int i) {
            super(str, i, null);
        }
    }

    /* renamed from: com.google.zxing.qrcode.decoder.DataMask$3 */
    /* loaded from: classes.dex */
    final class AnonymousClass3 extends DataMask {
        AnonymousClass3(String str, int i) {
            super(str, i, null);
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return i2 % 3 == 0;
        }
    }

    /* renamed from: com.google.zxing.qrcode.decoder.DataMask$4 */
    /* loaded from: classes.dex */
    final class AnonymousClass4 extends DataMask {
        AnonymousClass4(String str, int i) {
            super(str, i, null);
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return (i + i2) % 3 == 0;
        }
    }

    /* renamed from: com.google.zxing.qrcode.decoder.DataMask$5 */
    /* loaded from: classes.dex */
    final class AnonymousClass5 extends DataMask {
        AnonymousClass5(String str, int i) {
            super(str, i, null);
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return (((i / 2) + (i2 / 3)) & 1) == 0;
        }
    }

    /* renamed from: com.google.zxing.qrcode.decoder.DataMask$6 */
    /* loaded from: classes.dex */
    final class AnonymousClass6 extends DataMask {
        AnonymousClass6(String str, int i) {
            super(str, i, null);
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return (i * i2) % 6 == 0;
        }
    }

    /* renamed from: com.google.zxing.qrcode.decoder.DataMask$7 */
    /* loaded from: classes.dex */
    final class AnonymousClass7 extends DataMask {
        AnonymousClass7(String str, int i) {
            super(str, i, null);
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return (i * i2) % 6 < 3;
        }
    }

    /* renamed from: com.google.zxing.qrcode.decoder.DataMask$8 */
    /* loaded from: classes.dex */
    final class AnonymousClass8 extends DataMask {
        AnonymousClass8(String str, int i) {
            super(str, i, null);
        }

        @Override // com.google.zxing.qrcode.decoder.DataMask
        boolean isMasked(int i, int i2) {
            return (((i + i2) + ((i * i2) % 3)) & 1) == 0;
        }
    }

    public final void unmaskBitMatrix(BitMatrix bitMatrix, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            for (int i3 = 0; i3 < i; i3++) {
                if (isMasked(i2, i3)) {
                    bitMatrix.flip(i3, i2);
                }
            }
        }
    }
}
