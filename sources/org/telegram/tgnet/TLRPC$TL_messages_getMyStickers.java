package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_getMyStickers extends TLObject {
    public int limit;
    public long offset_id;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_messages_myStickers.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-793386500);
        abstractSerializedData.writeInt64(this.offset_id);
        abstractSerializedData.writeInt32(this.limit);
    }
}
