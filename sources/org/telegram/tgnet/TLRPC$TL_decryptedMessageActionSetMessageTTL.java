package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_decryptedMessageActionSetMessageTTL extends TLRPC$DecryptedMessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.ttl_seconds = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1586283796);
        abstractSerializedData.writeInt32(this.ttl_seconds);
    }
}
