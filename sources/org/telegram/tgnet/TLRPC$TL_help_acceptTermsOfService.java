package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_help_acceptTermsOfService extends TLObject {
    public TLRPC$TL_dataJSON id;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-294455398);
        this.id.serializeToStream(abstractSerializedData);
    }
}
