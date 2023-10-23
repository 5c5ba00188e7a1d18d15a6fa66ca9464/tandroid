package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_emailVerifyPurposeLoginChange extends TLRPC$EmailVerifyPurpose {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1383932651);
    }
}
