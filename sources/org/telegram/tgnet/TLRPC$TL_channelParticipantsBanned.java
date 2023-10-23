package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channelParticipantsBanned extends TLRPC$ChannelParticipantsFilter {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.q = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(338142689);
        abstractSerializedData.writeString(this.q);
    }
}
