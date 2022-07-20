package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_encryptedChatDiscarded_layer122 extends TLRPC$TL_encryptedChatDiscarded {
    public static int constructor = 332848423;

    @Override // org.telegram.tgnet.TLRPC$TL_encryptedChatDiscarded, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.id = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_encryptedChatDiscarded, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32(this.id);
    }
}
