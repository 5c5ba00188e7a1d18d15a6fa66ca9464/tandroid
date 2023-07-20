package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputReplyToStory extends TLRPC$InputReplyTo {
    public static int constructor = 363917955;
    public int story_id;
    public TLRPC$InputUser user_id;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.user_id = TLRPC$InputUser.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.story_id = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        this.user_id.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt32(this.story_id);
    }
}
