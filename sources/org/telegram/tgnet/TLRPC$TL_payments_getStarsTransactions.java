package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_payments_getStarsTransactions extends TLObject {
    public int flags;
    public boolean inbound;
    public String offset;
    public boolean outbound;
    public TLRPC$InputPeer peer;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_payments_starsStatus.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1731904249);
        int i = this.inbound ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        int i2 = this.outbound ? i | 2 : i & (-3);
        this.flags = i2;
        abstractSerializedData.writeInt32(i2);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.offset);
    }
}
