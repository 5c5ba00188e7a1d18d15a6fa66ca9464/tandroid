package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_auth_sentCodeTypeApp extends TLRPC$auth_SentCodeType {
    public static int constructor = 1035688326;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.length = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32(this.length);
    }
}
