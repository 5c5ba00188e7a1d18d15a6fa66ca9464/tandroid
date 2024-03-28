package j$.util.stream;
/* loaded from: classes2.dex */
abstract /* synthetic */ class A3 {
    static final /* synthetic */ int[] a;

    static {
        int[] iArr = new int[f4.values().length];
        a = iArr;
        try {
            iArr[f4.REFERENCE.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            a[f4.INT_VALUE.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            a[f4.LONG_VALUE.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            a[f4.DOUBLE_VALUE.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
    }
}
