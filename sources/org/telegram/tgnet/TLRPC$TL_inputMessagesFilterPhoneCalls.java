package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputMessagesFilterPhoneCalls extends TLRPC$MessagesFilter {
    public static int constructor = -2134272152;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        int readInt32 = abstractSerializedData.readInt32(z);
        this.flags = readInt32;
        this.missed = (readInt32 & 1) != 0;
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        int i = this.missed ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        abstractSerializedData.writeInt32(i);
    }
}
