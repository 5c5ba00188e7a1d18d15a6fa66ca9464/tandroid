package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_keyboardButtonCallback_layer117 extends TLRPC$TL_keyboardButtonCallback {
    public static int constructor = 1748655686;

    @Override // org.telegram.tgnet.TLRPC$TL_keyboardButtonCallback, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = abstractSerializedData.readString(z);
        this.data = abstractSerializedData.readByteArray(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_keyboardButtonCallback, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.text);
        abstractSerializedData.writeByteArray(this.data);
    }
}
