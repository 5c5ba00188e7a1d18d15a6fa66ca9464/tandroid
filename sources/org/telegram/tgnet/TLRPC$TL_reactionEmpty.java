package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_reactionEmpty extends TLRPC$Reaction {
    public static int constructor = 2046153753;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
