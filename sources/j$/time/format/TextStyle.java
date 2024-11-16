package j$.time.format;

/* loaded from: classes2.dex */
public enum TextStyle {
    FULL(0),
    FULL_STANDALONE(0),
    SHORT(1),
    SHORT_STANDALONE(1),
    NARROW(1),
    NARROW_STANDALONE(1);

    private final int a;

    TextStyle(int i) {
        this.a = i;
    }

    final int a() {
        return this.a;
    }
}
