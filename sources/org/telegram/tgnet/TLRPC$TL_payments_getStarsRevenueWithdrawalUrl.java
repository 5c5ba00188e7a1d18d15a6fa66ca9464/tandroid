package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_payments_getStarsRevenueWithdrawalUrl extends TLObject {
    public TLRPC$InputCheckPasswordSRP password;
    public TLRPC$InputPeer peer;
    public long stars;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_payments_starsRevenueWithdrawalUrl.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(331081907);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt64(this.stars);
        this.password.serializeToStream(abstractSerializedData);
    }
}
