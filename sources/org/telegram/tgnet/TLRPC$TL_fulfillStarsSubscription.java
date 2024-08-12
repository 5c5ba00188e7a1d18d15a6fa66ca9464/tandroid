package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_fulfillStarsSubscription extends TLObject {
    public TLRPC$InputPeer peer;
    public String subscription_id;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-866391117);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.subscription_id);
    }
}
