package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputPrivacyValueAllowAll extends TLRPC$InputPrivacyRule {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(407582158);
    }
}
