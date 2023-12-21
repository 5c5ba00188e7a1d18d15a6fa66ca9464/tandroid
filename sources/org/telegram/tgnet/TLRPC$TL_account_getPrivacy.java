package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_getPrivacy extends TLObject {
    public TLRPC$InputPrivacyKey key;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_account_privacyRules.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-623130288);
        this.key.serializeToStream(abstractSerializedData);
    }
}
