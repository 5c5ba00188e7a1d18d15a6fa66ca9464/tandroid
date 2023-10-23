package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_privacyValueAllowCloseFriends extends TLRPC$PrivacyRule {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-135735141);
    }
}
