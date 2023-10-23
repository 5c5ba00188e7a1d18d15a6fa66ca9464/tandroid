package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_reactionEmpty extends TLRPC$Reaction {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(2046153753);
    }
}
