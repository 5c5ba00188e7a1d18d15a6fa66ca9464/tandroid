package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$AttachMenuBot extends TLObject {
    public static TLRPC$TL_attachMenuBot TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$TL_attachMenuBot tLRPC$TL_attachMenuBot;
        if (i != -928371502) {
            tLRPC$TL_attachMenuBot = i != -381896846 ? null : new TLRPC$TL_attachMenuBot_layer140();
        } else {
            tLRPC$TL_attachMenuBot = new TLRPC$TL_attachMenuBot();
        }
        if (tLRPC$TL_attachMenuBot != null || !z) {
            if (tLRPC$TL_attachMenuBot != null) {
                tLRPC$TL_attachMenuBot.readParams(abstractSerializedData, z);
            }
            return tLRPC$TL_attachMenuBot;
        }
        throw new RuntimeException(String.format("can't parse magic %x in AttachMenuBot", Integer.valueOf(i)));
    }
}
