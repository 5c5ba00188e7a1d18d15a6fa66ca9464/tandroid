package com.googlecode.mp4parser.authoring.builder;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes3.dex */
public class ByteBufferHelper {
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0086, code lost:
        if ((r2 instanceof java.nio.MappedByteBuffer) == false) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x008e, code lost:
        if ((r0.get(r3) instanceof java.nio.MappedByteBuffer) == false) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00a9, code lost:
        if (r0.get(r3).limit() != (r0.get(r3).capacity() - r2.capacity())) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00ab, code lost:
        r4 = r0.get(r3);
        r4.limit(r2.limit() + r4.limit());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static List<ByteBuffer> mergeAdjacentBuffers(List<ByteBuffer> samples) {
        ArrayList<ByteBuffer> nuSamples = new ArrayList<>(samples.size());
        Iterator<ByteBuffer> it = samples.iterator();
        while (it.hasNext()) {
            ByteBuffer buffer = it.next();
            int lastIndex = nuSamples.size() - 1;
            if (lastIndex >= 0 && buffer.hasArray() && nuSamples.get(lastIndex).hasArray() && buffer.array() == nuSamples.get(lastIndex).array() && nuSamples.get(lastIndex).arrayOffset() + nuSamples.get(lastIndex).limit() == buffer.arrayOffset()) {
                ByteBuffer oldBuffer = nuSamples.remove(lastIndex);
                ByteBuffer nu = ByteBuffer.wrap(buffer.array(), oldBuffer.arrayOffset(), oldBuffer.limit() + buffer.limit()).slice();
                nuSamples.add(nu);
            } else {
                buffer.reset();
                nuSamples.add(buffer);
            }
        }
        return nuSamples;
    }
}
