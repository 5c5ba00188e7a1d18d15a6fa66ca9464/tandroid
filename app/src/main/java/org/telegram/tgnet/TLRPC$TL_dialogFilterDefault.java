package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_dialogFilterDefault extends TLRPC$DialogFilter {
    public static int constructor = 909284270;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
