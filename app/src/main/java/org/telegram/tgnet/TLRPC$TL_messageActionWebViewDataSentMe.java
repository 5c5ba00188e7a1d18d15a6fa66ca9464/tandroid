package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageActionWebViewDataSentMe extends TLRPC$MessageAction {
    public static int constructor = 1205698681;
    public String data;
    public String text;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = abstractSerializedData.readString(z);
        this.data = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.text);
        abstractSerializedData.writeString(this.data);
    }
}
