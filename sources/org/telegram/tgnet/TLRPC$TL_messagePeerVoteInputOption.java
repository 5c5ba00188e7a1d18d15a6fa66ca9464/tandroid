package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messagePeerVoteInputOption extends TLRPC$MessagePeerVote {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.peer = TLRPC$Peer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.date = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1959634180);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt32(this.date);
    }
}
