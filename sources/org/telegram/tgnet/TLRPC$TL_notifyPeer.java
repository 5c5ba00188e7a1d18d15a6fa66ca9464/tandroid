package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_notifyPeer extends TLRPC$NotifyPeer {
    public TLRPC$Peer peer;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.peer = TLRPC$Peer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1613493288);
        this.peer.serializeToStream(abstractSerializedData);
    }
}
