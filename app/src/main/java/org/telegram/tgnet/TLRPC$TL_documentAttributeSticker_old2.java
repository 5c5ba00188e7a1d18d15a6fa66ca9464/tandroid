package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_documentAttributeSticker_old2 extends TLRPC$TL_documentAttributeSticker {
    public static int constructor = -1723033470;

    @Override // org.telegram.tgnet.TLRPC$TL_documentAttributeSticker, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.alt = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_documentAttributeSticker, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.alt);
    }
}
