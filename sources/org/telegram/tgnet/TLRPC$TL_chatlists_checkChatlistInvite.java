package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_chatlists_checkChatlistInvite extends TLObject {
    public static int constructor = 1103171583;
    public String slug;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$chatlist_ChatlistInvite.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.slug);
    }
}
