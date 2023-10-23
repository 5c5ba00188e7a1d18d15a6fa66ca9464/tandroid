package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_channelAdminLogEventActionChangePhoto extends TLRPC$ChannelAdminLogEventAction {
    public TLRPC$Photo new_photo;
    public TLRPC$Photo prev_photo;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.prev_photo = TLRPC$Photo.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.new_photo = TLRPC$Photo.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1129042607);
        this.prev_photo.serializeToStream(abstractSerializedData);
        this.new_photo.serializeToStream(abstractSerializedData);
    }
}
