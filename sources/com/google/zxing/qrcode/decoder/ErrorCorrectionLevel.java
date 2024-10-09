package com.google.zxing.qrcode.decoder;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Can't remove SSA var: r0v0 com.google.zxing.qrcode.decoder.ErrorCorrectionLevel, still in use, count: 1, list:
  (r0v0 com.google.zxing.qrcode.decoder.ErrorCorrectionLevel) from 0x002f: FILLED_NEW_ARRAY 
  (r1v1 com.google.zxing.qrcode.decoder.ErrorCorrectionLevel)
  (r0v0 com.google.zxing.qrcode.decoder.ErrorCorrectionLevel)
  (r5v1 com.google.zxing.qrcode.decoder.ErrorCorrectionLevel)
  (r4v1 com.google.zxing.qrcode.decoder.ErrorCorrectionLevel)
 A[WRAPPED] elemType: com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
	at jadx.core.utils.InsnRemover.removeSsaVar(InsnRemover.java:151)
	at jadx.core.utils.InsnRemover.unbindResult(InsnRemover.java:116)
	at jadx.core.utils.InsnRemover.lambda$unbindInsns$1(InsnRemover.java:88)
	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
	at jadx.core.utils.InsnRemover.unbindInsns(InsnRemover.java:87)
	at jadx.core.utils.InsnRemover.removeAllAndUnbind(InsnRemover.java:238)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:180)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes.dex */
public final class ErrorCorrectionLevel {
    L(1),
    M(0),
    Q(3),
    H(2);

    private static final ErrorCorrectionLevel[] FOR_BITS;
    private final int bits;

    static {
        FOR_BITS = new ErrorCorrectionLevel[]{r1, new ErrorCorrectionLevel(1), r5, new ErrorCorrectionLevel(3)};
    }

    private ErrorCorrectionLevel(int i) {
        this.bits = i;
    }

    public static ErrorCorrectionLevel forBits(int i) {
        if (i >= 0) {
            ErrorCorrectionLevel[] errorCorrectionLevelArr = FOR_BITS;
            if (i < errorCorrectionLevelArr.length) {
                return errorCorrectionLevelArr[i];
            }
        }
        throw new IllegalArgumentException();
    }

    public static ErrorCorrectionLevel valueOf(String str) {
        return (ErrorCorrectionLevel) Enum.valueOf(ErrorCorrectionLevel.class, str);
    }

    public static ErrorCorrectionLevel[] values() {
        return (ErrorCorrectionLevel[]) $VALUES.clone();
    }

    public int getBits() {
        return this.bits;
    }
}
