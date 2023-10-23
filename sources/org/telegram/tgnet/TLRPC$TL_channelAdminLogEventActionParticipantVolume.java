package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channelAdminLogEventActionParticipantVolume extends TLRPC$ChannelAdminLogEventAction {
    public TLRPC$TL_groupCallParticipant participant;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.participant = TLRPC$TL_groupCallParticipant.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1048537159);
        this.participant.serializeToStream(abstractSerializedData);
    }
}
