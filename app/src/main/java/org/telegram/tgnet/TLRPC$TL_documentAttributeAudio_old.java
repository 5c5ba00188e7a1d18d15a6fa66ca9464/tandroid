package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_documentAttributeAudio_old extends TLRPC$TL_documentAttributeAudio {
    public static int constructor = 85215461;

    @Override // org.telegram.tgnet.TLRPC$TL_documentAttributeAudio, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.duration = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_documentAttributeAudio, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32(this.duration);
    }
}
