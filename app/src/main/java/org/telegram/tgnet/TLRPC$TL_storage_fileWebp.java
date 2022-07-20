package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_storage_fileWebp extends TLRPC$storage_FileType {
    public static int constructor = 276907596;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
