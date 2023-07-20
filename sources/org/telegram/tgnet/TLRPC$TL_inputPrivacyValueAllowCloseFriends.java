package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputPrivacyValueAllowCloseFriends extends TLRPC$InputPrivacyRule {
    public static int constructor = 793067081;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
