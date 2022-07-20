package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_updateAttachMenuBots extends TLRPC$Update {
    public static int constructor = 397910539;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
