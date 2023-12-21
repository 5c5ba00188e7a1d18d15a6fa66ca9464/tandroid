package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_channelAdminLogEventActionChangePeerColor extends TLRPC$ChannelAdminLogEventAction {
    public TLRPC$TL_peerColor new_value;
    public TLRPC$TL_peerColor prev_value;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.prev_value = TLRPC$TL_peerColor.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.new_value = TLRPC$TL_peerColor.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1469507456);
        this.prev_value.serializeToStream(abstractSerializedData);
        this.new_value.serializeToStream(abstractSerializedData);
    }
}
