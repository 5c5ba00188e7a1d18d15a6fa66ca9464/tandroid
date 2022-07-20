package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_pageBlockAudio_layer82 extends TLRPC$TL_pageBlockAudio {
    public static int constructor = 834148991;

    @Override // org.telegram.tgnet.TLRPC$TL_pageBlockAudio, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.audio_id = abstractSerializedData.readInt64(z);
        TLRPC$TL_pageCaption tLRPC$TL_pageCaption = new TLRPC$TL_pageCaption();
        this.caption = tLRPC$TL_pageCaption;
        tLRPC$TL_pageCaption.text = TLRPC$RichText.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.caption.credit = new TLRPC$TL_textEmpty();
    }

    @Override // org.telegram.tgnet.TLRPC$TL_pageBlockAudio, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt64(this.audio_id);
        this.caption.text.serializeToStream(abstractSerializedData);
    }
}
