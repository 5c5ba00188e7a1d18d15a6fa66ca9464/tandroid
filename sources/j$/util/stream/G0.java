package j$.util.stream;
/* loaded from: classes2.dex */
abstract /* synthetic */ class G0 {
    static final /* synthetic */ int[] a;

    static {
        int[] iArr = new int[T2.values().length];
        a = iArr;
        try {
            iArr[T2.REFERENCE.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            a[T2.INT_VALUE.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            a[T2.LONG_VALUE.ordinal()] = 3;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            a[T2.DOUBLE_VALUE.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
    }
}
