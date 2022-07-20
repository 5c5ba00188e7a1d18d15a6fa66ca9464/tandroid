package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_updateLoginToken extends TLRPC$Update {
    public static int constructor = 1448076945;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
