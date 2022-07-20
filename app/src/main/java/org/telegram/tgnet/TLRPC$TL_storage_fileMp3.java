package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_storage_fileMp3 extends TLRPC$storage_FileType {
    public static int constructor = 1384777335;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
