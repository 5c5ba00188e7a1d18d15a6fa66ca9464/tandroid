package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_deleteChat extends TLObject {
    public long chat_id;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1540419152);
        abstractSerializedData.writeInt64(this.chat_id);
    }
}
