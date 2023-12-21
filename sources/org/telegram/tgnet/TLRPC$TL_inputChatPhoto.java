package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputChatPhoto extends TLRPC$InputChatPhoto {
    public TLRPC$InputPhoto id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.id = TLRPC$InputPhoto.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1991004873);
        this.id.serializeToStream(abstractSerializedData);
    }
}
