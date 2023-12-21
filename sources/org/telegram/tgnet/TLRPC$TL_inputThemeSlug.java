package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputThemeSlug extends TLRPC$InputTheme {
    public String slug;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.slug = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-175567375);
        abstractSerializedData.writeString(this.slug);
    }
}
