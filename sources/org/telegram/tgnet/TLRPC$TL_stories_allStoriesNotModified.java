package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_stories_allStoriesNotModified extends TLRPC$stories_AllStories {
    public static int constructor = 1205903486;
    public String state;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.state = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeString(this.state);
    }
}
