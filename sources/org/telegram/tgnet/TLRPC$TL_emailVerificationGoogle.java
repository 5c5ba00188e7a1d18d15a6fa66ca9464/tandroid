package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_emailVerificationGoogle extends TLRPC$EmailVerification {
    public String token;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.token = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-611279166);
        abstractSerializedData.writeString(this.token);
    }
}
