package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_privacyValueAllowAll extends TLRPC$PrivacyRule {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1698855810);
    }
}
