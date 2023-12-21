package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputWallPaperSlug extends TLRPC$InputWallPaper {
    public String slug;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.slug = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1913199744);
        abstractSerializedData.writeString(this.slug);
    }
}
