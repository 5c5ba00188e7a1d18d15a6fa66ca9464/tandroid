package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_updateContactsReset extends TLRPC$Update {
    public static int constructor = 1887741886;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
