package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_account_resetPasswordOk extends TLRPC$account_ResetPasswordResult {
    public static int constructor = -383330754;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
