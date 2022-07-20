package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_chatParticipantsForbidden_old extends TLRPC$TL_chatParticipantsForbidden {
    public static int constructor = 265468810;

    @Override // org.telegram.tgnet.TLRPC$TL_chatParticipantsForbidden, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.chat_id = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_chatParticipantsForbidden, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32((int) this.chat_id);
    }
}
