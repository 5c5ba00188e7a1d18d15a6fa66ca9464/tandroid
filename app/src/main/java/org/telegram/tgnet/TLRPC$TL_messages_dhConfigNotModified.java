package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_dhConfigNotModified extends TLRPC$messages_DhConfig {
    public static int constructor = -1058912715;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.random = abstractSerializedData.readByteArray(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeByteArray(this.random);
    }
}
