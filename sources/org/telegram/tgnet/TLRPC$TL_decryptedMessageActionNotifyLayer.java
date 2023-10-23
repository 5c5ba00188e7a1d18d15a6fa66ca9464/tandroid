package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_decryptedMessageActionNotifyLayer extends TLRPC$DecryptedMessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.layer = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-217806717);
        abstractSerializedData.writeInt32(this.layer);
    }
}
