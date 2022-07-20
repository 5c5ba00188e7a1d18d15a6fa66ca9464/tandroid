package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_topPeerCategoryChannels extends TLRPC$TopPeerCategory {
    public static int constructor = 371037736;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
