package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_account_setAuthorizationTTL extends TLObject {
    public int authorization_ttl_days;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1081501024);
        abstractSerializedData.writeInt32(this.authorization_ttl_days);
    }
}
