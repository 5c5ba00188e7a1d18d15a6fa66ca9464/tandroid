package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_contacts_resolvePhone extends TLObject {
    public String phone;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_contacts_resolvedPeer.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1963375804);
        abstractSerializedData.writeString(this.phone);
    }
}
