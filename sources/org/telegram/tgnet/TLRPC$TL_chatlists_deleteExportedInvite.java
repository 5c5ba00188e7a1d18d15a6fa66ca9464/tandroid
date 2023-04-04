package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_chatlists_deleteExportedInvite extends TLObject {
    public static int constructor = 1906072670;
    public TLRPC$TL_inputChatlistDialogFilter chatlist;
    public String slug;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        this.chatlist.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.slug);
    }
}
