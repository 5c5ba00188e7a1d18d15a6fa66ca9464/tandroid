package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_themeDocumentNotModified_layer106 extends TLRPC$TL_theme {
    public static int constructor = 1211967244;

    @Override // org.telegram.tgnet.TLRPC$TL_theme, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
