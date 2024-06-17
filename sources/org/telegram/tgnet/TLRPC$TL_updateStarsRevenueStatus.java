package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_updateStarsRevenueStatus extends TLRPC$Update {
    public TLRPC$Peer peer;
    public TLRPC$TL_starsRevenueStatus status;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.peer = TLRPC$Peer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.status = TLRPC$TL_starsRevenueStatus.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1518030823);
        this.peer.serializeToStream(abstractSerializedData);
        this.status.serializeToStream(abstractSerializedData);
    }
}
