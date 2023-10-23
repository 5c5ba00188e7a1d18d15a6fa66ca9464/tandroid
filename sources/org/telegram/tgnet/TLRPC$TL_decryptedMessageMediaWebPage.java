package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_decryptedMessageMediaWebPage extends TLRPC$DecryptedMessageMedia {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.url = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-452652584);
        abstractSerializedData.writeString(this.url);
    }
}
