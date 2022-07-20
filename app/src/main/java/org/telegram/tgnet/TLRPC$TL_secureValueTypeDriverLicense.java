package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_secureValueTypeDriverLicense extends TLRPC$SecureValueType {
    public static int constructor = 115615172;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
