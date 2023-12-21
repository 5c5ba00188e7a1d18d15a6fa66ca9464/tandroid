package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputMediaGame extends TLRPC$InputMedia {
    public TLRPC$InputGame id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.id = TLRPC$InputGame.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-750828557);
        this.id.serializeToStream(abstractSerializedData);
    }
}
