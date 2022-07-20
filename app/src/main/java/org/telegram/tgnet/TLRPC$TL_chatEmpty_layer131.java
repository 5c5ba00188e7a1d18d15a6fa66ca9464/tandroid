package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_chatEmpty_layer131 extends TLRPC$TL_chatEmpty {
    public static int constructor = -1683826688;

    @Override // org.telegram.tgnet.TLRPC$TL_chatEmpty, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.id = abstractSerializedData.readInt32(z);
        this.title = "DELETED";
    }

    @Override // org.telegram.tgnet.TLRPC$TL_chatEmpty, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32((int) this.id);
    }
}
