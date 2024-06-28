package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_payments_getStarsRevenueAdsAccountUrl extends TLObject {
    public TLRPC$InputPeer peer;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_payments_starsRevenueAdsAccountUrl.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-774377531);
        this.peer.serializeToStream(abstractSerializedData);
    }
}
