package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_emailVerificationCode extends TLRPC$EmailVerification {
    public String code;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.code = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1842457175);
        abstractSerializedData.writeString(this.code);
    }
}
