package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_chatEmpty extends TLRPC$Chat {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.id = abstractSerializedData.readInt64(z);
        this.title = "DELETED";
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(693512293);
        abstractSerializedData.writeInt64(this.id);
    }
}
