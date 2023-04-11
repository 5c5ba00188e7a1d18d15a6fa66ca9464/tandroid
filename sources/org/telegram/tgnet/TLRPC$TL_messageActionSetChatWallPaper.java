package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messageActionSetChatWallPaper extends TLRPC$MessageAction {
    public static int constructor = -1136350937;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.wallpaper = TLRPC$WallPaper.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        this.wallpaper.serializeToStream(abstractSerializedData);
    }
}
