package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputFileStoryDocument extends TLRPC$InputFile {
    public TLRPC$InputDocument doc;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.doc = TLRPC$InputDocument.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1658620744);
        this.doc.serializeToStream(abstractSerializedData);
    }
}
