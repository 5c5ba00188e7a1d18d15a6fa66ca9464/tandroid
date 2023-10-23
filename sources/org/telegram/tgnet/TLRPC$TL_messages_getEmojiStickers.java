package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_getEmojiStickers extends TLObject {
    public long hash;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_AllStickers.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-67329649);
        abstractSerializedData.writeInt64(this.hash);
    }
}
