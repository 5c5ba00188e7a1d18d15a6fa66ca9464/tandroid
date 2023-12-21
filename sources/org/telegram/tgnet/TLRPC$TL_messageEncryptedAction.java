package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messageEncryptedAction extends TLRPC$MessageAction {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.encryptedAction = TLRPC$DecryptedMessageAction.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1431655927);
        this.encryptedAction.serializeToStream(abstractSerializedData);
    }
}
