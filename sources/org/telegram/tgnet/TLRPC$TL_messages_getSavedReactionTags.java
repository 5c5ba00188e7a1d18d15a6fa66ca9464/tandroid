package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_getSavedReactionTags extends TLObject {
    public long hash;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_SavedReactionTags.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1981668047);
        abstractSerializedData.writeInt64(this.hash);
    }
}
