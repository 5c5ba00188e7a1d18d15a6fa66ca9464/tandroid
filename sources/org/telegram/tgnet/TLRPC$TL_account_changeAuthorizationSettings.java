package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_changeAuthorizationSettings extends TLObject {
    public boolean call_requests_disabled;
    public boolean confirmed;
    public boolean encrypted_requests_disabled;
    public int flags;
    public long hash;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1089766498);
        int i = this.confirmed ? this.flags | 8 : this.flags & (-9);
        this.flags = i;
        abstractSerializedData.writeInt32(i);
        abstractSerializedData.writeInt64(this.hash);
        if ((this.flags & 1) != 0) {
            abstractSerializedData.writeBool(this.encrypted_requests_disabled);
        }
        if ((this.flags & 2) != 0) {
            abstractSerializedData.writeBool(this.call_requests_disabled);
        }
    }
}
