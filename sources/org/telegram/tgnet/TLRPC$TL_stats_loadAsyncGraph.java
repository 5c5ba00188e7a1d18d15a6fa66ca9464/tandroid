package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_stats_loadAsyncGraph extends TLObject {
    public int flags;
    public String token;
    public long x;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$StatsGraph.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1646092192);
        abstractSerializedData.writeInt32(this.flags);
        abstractSerializedData.writeString(this.token);
        if ((this.flags & 1) != 0) {
            abstractSerializedData.writeInt64(this.x);
        }
    }
}
