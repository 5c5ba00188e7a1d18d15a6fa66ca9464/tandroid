package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_textEmail extends TLRPC$RichText {
    public TLRPC$RichText text;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = TLRPC$RichText.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.email = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-564523562);
        this.text.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.email);
    }
}
