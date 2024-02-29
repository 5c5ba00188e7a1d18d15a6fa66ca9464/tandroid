package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_updateDeleteQuickReply extends TLRPC$Update {
    public int shortcut_id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.shortcut_id = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1407644140);
        abstractSerializedData.writeInt32(this.shortcut_id);
    }
}
