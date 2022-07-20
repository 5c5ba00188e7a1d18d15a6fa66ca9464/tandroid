package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_storage_filePdf extends TLRPC$storage_FileType {
    public static int constructor = -1373745011;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
