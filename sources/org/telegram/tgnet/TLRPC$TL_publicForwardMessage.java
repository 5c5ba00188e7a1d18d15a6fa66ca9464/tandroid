package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_publicForwardMessage extends TLRPC$PublicForward {
    public TLRPC$Message message;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.message = TLRPC$Message.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(32685898);
        this.message.serializeToStream(abstractSerializedData);
    }
}
