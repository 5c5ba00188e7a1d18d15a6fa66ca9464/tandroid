package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_updateBroadcastRevenueTransactions extends TLRPC$Update {
    public TLRPC$TL_broadcastRevenueBalances balances;
    public TLRPC$Peer peer;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.peer = TLRPC$Peer.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.balances = TLRPC$TL_broadcastRevenueBalances.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-539401739);
        this.peer.serializeToStream(abstractSerializedData);
        this.balances.serializeToStream(abstractSerializedData);
    }
}
