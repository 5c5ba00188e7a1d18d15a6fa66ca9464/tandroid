package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_translateResultText extends TLRPC$messages_TranslatedText {
    public static int constructor = -1575684144;
    public String text;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.text);
    }
}
