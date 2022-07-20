package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_keyboardButtonWebView extends TLRPC$KeyboardButton {
    public static int constructor = 326529584;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = abstractSerializedData.readString(z);
        this.url = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.text);
        abstractSerializedData.writeString(this.url);
    }
}
