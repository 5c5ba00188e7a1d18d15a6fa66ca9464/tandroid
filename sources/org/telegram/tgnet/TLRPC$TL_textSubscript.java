package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_textSubscript extends TLRPC$RichText {
    public TLRPC$RichText text;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = TLRPC$RichText.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-311786236);
        this.text.serializeToStream(abstractSerializedData);
    }
}
