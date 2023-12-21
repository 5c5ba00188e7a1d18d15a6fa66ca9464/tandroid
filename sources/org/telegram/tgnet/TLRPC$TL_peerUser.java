package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_peerUser extends TLRPC$Peer {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.user_id = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1498486562);
        abstractSerializedData.writeInt64(this.user_id);
    }
}
