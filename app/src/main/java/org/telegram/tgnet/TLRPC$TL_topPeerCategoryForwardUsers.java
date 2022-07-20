package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_topPeerCategoryForwardUsers extends TLRPC$TopPeerCategory {
    public static int constructor = -1472172887;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
