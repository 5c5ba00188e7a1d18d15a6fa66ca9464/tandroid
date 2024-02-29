package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_updateBusinessWorkHours extends TLObject {
    public TLRPC$TL_businessWorkHours business_work_hours;
    public int flags;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1258348646);
        abstractSerializedData.writeInt32(this.flags);
        if ((this.flags & 1) != 0) {
            this.business_work_hours.serializeToStream(abstractSerializedData);
        }
    }
}
