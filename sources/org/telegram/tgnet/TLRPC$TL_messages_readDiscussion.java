package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_readDiscussion extends TLObject {
    public int msg_id;
    public TLRPC$InputPeer peer;
    public int read_max_id;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-147740172);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt32(this.msg_id);
        abstractSerializedData.writeInt32(this.read_max_id);
    }
}
