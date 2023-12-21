package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_documentAttributeFilename extends TLRPC$DocumentAttribute {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.file_name = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(358154344);
        abstractSerializedData.writeString(this.file_name);
    }
}
