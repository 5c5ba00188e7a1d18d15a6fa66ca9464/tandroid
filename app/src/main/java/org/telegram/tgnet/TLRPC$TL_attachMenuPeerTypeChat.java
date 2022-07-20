package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_attachMenuPeerTypeChat extends TLRPC$AttachMenuPeerType {
    public static int constructor = 84480319;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
