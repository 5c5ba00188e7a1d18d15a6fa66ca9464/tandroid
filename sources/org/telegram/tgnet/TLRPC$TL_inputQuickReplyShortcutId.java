package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputQuickReplyShortcutId extends TLRPC$InputQuickReplyShortcut {
    public int shortcut_id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.shortcut_id = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(18418929);
        abstractSerializedData.writeInt32(this.shortcut_id);
    }
}
