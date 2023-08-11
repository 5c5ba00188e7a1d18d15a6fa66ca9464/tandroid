package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_stories_activateStealthMode extends TLObject {
    public static int constructor = 1471926630;
    public int flags;
    public boolean future;
    public boolean past;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        int i = this.past ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        int i2 = this.future ? i | 2 : i & (-3);
        this.flags = i2;
        abstractSerializedData.writeInt32(i2);
    }
}
