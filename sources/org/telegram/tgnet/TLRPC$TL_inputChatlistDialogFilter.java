package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputChatlistDialogFilter extends TLObject {
    public static int constructor = -203367885;
    public int filter_id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.filter_id = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32(this.filter_id);
    }
}
