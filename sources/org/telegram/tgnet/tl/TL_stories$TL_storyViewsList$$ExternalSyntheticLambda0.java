package org.telegram.tgnet.tl;

import org.telegram.tgnet.InputSerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.Vector;
import org.telegram.tgnet.tl.TL_stories;

/* loaded from: classes3.dex */
public final /* synthetic */ class TL_stories$TL_storyViewsList$$ExternalSyntheticLambda0 implements Vector.TLDeserializer {
    @Override // org.telegram.tgnet.Vector.TLDeserializer
    public final TLObject deserialize(InputSerializedData inputSerializedData, int i, boolean z) {
        return TL_stories.StoryView.TLdeserialize(inputSerializedData, i, z);
    }
}
