package com.google.android.exoplayer2.trackselection;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.source.TrackGroup;

/* loaded from: classes.dex */
public interface TrackSelection {
    Format getFormat(int i);

    int getIndexInTrackGroup(int i);

    TrackGroup getTrackGroup();

    int indexOf(int i);

    int indexOf(Format format);

    int length();
}
