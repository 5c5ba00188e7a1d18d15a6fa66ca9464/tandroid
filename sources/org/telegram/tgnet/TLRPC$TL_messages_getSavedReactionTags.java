package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_getSavedReactionTags extends TLObject {
    public int flags;
    public long hash;
    public TLRPC$InputPeer peer;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_SavedReactionTags.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(909631579);
        abstractSerializedData.writeInt32(this.flags);
        if ((this.flags & 1) != 0) {
            this.peer.serializeToStream(abstractSerializedData);
        }
        abstractSerializedData.writeInt64(this.hash);
    }
}
