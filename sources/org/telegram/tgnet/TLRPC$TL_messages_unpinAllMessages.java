package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_unpinAllMessages extends TLObject {
    public TLRPC$InputPeer peer;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_messages_affectedHistory.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-265962357);
        this.peer.serializeToStream(abstractSerializedData);
    }
}
