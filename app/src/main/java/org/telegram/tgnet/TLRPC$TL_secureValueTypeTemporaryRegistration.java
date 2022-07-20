package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_secureValueTypeTemporaryRegistration extends TLRPC$SecureValueType {
    public static int constructor = -368907213;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
