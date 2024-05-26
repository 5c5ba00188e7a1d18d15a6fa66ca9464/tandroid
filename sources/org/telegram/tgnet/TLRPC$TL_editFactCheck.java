package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_editFactCheck extends TLObject {
    public int msg_id;
    public TLRPC$InputPeer peer;
    public TLRPC$TL_textWithEntities text;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(92925557);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt32(this.msg_id);
        this.text.serializeToStream(abstractSerializedData);
    }
}
