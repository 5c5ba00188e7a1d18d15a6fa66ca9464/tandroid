package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_getSavedGifs extends TLObject {
    public long hash;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_SavedGifs.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1559270965);
        abstractSerializedData.writeInt64(this.hash);
    }
}
