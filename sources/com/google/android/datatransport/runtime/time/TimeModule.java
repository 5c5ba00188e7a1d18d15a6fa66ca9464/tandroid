package com.google.android.datatransport.runtime.time;

/* loaded from: classes.dex */
public abstract class TimeModule {
    static Clock eventClock() {
        return new WallTimeClock();
    }

    static Clock uptimeClock() {
        return new UptimeClock();
    }
}
