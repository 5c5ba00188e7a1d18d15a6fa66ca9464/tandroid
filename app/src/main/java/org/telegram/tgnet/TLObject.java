package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLObject {
    private static final ThreadLocal<NativeByteBuffer> sizeCalculator = new AnonymousClass1();
    public boolean disableFree = false;
    public int networkType;

    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return null;
    }

    public void freeResources() {
    }

    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
    }

    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
    }

    /* renamed from: org.telegram.tgnet.TLObject$1 */
    /* loaded from: classes.dex */
    class AnonymousClass1 extends ThreadLocal<NativeByteBuffer> {
        AnonymousClass1() {
        }

        @Override // java.lang.ThreadLocal
        public NativeByteBuffer initialValue() {
            return new NativeByteBuffer(true);
        }
    }

    public int getObjectSize() {
        ThreadLocal<NativeByteBuffer> threadLocal = sizeCalculator;
        NativeByteBuffer nativeByteBuffer = threadLocal.get();
        nativeByteBuffer.rewind();
        serializeToStream(threadLocal.get());
        return nativeByteBuffer.length();
    }
}
