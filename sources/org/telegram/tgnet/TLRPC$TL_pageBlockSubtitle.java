package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_pageBlockSubtitle extends TLRPC$PageBlock {
    public TLRPC$RichText text;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.text = TLRPC$RichText.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1879401953);
        this.text.serializeToStream(abstractSerializedData);
    }
}
