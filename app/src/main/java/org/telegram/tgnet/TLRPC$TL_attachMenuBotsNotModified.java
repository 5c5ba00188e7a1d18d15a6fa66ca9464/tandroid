package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_attachMenuBotsNotModified extends TLRPC$AttachMenuBots {
    public static int constructor = -237467044;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
