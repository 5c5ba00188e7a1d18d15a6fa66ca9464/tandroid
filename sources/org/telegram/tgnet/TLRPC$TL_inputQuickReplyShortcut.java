package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputQuickReplyShortcut extends TLRPC$InputQuickReplyShortcut {
    public String shortcut;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.shortcut = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(609840449);
        abstractSerializedData.writeString(this.shortcut);
    }
}
