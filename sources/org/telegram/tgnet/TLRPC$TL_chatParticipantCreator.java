package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_chatParticipantCreator extends TLRPC$ChatParticipant {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.user_id = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-462696732);
        abstractSerializedData.writeInt64(this.user_id);
    }
}
