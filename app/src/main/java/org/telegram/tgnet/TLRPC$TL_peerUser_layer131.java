package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_peerUser_layer131 extends TLRPC$TL_peerUser {
    public static int constructor = -1649296275;

    @Override // org.telegram.tgnet.TLRPC$TL_peerUser, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.user_id = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_peerUser, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32((int) this.user_id);
    }
}
