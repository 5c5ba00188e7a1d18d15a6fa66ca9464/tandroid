package org.telegram.tgnet;

import org.telegram.tgnet.Vector;

/* loaded from: classes3.dex */
public final /* synthetic */ class Vector$$ExternalSyntheticLambda5 implements Vector.TLDeserializer {
    @Override // org.telegram.tgnet.Vector.TLDeserializer
    public final TLObject deserialize(InputSerializedData inputSerializedData, int i, boolean z) {
        return Vector.Int.TLDeserialize(inputSerializedData, i, z);
    }
}
