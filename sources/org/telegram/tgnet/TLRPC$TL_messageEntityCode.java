package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageEntityCode extends TLRPC$MessageEntity {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.offset = abstractSerializedData.readInt32(z);
        this.length = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(681706865);
        abstractSerializedData.writeInt32(this.offset);
        abstractSerializedData.writeInt32(this.length);
    }
}
