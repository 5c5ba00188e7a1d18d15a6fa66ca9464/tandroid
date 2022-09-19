package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_emailVerifyPurposePassport extends TLRPC$EmailVerifyPurpose {
    public static int constructor = -1141565819;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
