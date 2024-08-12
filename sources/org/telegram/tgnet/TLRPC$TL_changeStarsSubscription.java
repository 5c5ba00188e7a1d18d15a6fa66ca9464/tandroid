package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_changeStarsSubscription extends TLObject {
    public Boolean canceled;
    public int flags;
    public TLRPC$InputPeer peer;
    public String subscription_id;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-948500360);
        int i = this.canceled != null ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        abstractSerializedData.writeInt32(i);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.subscription_id);
        if ((this.flags & 1) != 0) {
            abstractSerializedData.writeBool(this.canceled.booleanValue());
        }
    }
}
