package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_auth_codeTypeMissedCall extends TLRPC$auth_CodeType {
    public static int constructor = -702884114;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
