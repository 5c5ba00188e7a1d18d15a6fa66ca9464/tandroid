package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_webPageNotModified_layer110 extends TLRPC$TL_webPageNotModified {
    public static int constructor = -2054908813;

    @Override // org.telegram.tgnet.TLRPC$TL_webPageNotModified, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
