package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_chatInvitePublicJoinRequests extends TLRPC$TL_chatInviteExported {
    public static int constructor = -317687113;

    @Override // org.telegram.tgnet.TLRPC$TL_chatInviteExported, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
    }

    @Override // org.telegram.tgnet.TLRPC$TL_chatInviteExported, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
