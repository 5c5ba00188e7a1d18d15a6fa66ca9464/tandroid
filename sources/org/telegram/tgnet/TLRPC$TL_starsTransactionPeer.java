package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_starsTransactionPeer extends TLRPC$StarsTransactionPeer {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.peer = TLRPC$Peer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-670195363);
        this.peer.serializeToStream(abstractSerializedData);
    }
}
