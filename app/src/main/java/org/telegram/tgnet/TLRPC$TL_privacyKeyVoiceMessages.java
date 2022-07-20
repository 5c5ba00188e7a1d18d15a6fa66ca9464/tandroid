package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_privacyKeyVoiceMessages extends TLRPC$PrivacyKey {
    public static int constructor = 110621716;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
