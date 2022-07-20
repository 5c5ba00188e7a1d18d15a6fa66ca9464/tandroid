package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_userStatusRecently extends TLRPC$UserStatus {
    public static int constructor = -496024847;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
