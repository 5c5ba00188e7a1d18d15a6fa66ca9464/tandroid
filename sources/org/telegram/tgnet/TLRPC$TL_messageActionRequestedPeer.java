package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageActionRequestedPeer extends TLRPC$MessageAction {
    public int button_id;
    public TLRPC$Peer peer;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.button_id = abstractSerializedData.readInt32(z);
        this.peer = TLRPC$Peer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-25742243);
        abstractSerializedData.writeInt32(this.button_id);
        this.peer.serializeToStream(abstractSerializedData);
    }
}
