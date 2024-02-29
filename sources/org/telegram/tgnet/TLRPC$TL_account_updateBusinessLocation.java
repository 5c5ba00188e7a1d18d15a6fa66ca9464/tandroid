package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_updateBusinessLocation extends TLObject {
    public String address;
    public int flags;
    public TLRPC$InputGeoPoint geo_point;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1637149926);
        abstractSerializedData.writeInt32(this.flags);
        if ((this.flags & 2) != 0) {
            this.geo_point.serializeToStream(abstractSerializedData);
        }
        if ((this.flags & 1) != 0) {
            abstractSerializedData.writeString(this.address);
        }
    }
}
