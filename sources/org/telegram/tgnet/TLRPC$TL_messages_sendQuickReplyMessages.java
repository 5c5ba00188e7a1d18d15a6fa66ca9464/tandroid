package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_sendQuickReplyMessages extends TLObject {
    public TLRPC$InputPeer peer;
    public int shortcut_id;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(857029332);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt32(this.shortcut_id);
    }
}
