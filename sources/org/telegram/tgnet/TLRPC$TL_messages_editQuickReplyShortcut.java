package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_editQuickReplyShortcut extends TLObject {
    public String shortcut;
    public int shortcut_id;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1543519471);
        abstractSerializedData.writeInt32(this.shortcut_id);
        abstractSerializedData.writeString(this.shortcut);
    }
}
