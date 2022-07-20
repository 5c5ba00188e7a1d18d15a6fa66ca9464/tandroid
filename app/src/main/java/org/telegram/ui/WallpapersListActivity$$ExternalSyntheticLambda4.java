package org.telegram.ui;

import java.util.Comparator;
/* loaded from: classes3.dex */
public final /* synthetic */ class WallpapersListActivity$$ExternalSyntheticLambda4 implements Comparator {
    public final /* synthetic */ WallpapersListActivity f$0;
    public final /* synthetic */ long f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ boolean f$3;

    public /* synthetic */ WallpapersListActivity$$ExternalSyntheticLambda4(WallpapersListActivity wallpapersListActivity, long j, String str, boolean z) {
        this.f$0 = wallpapersListActivity;
        this.f$1 = j;
        this.f$2 = str;
        this.f$3 = z;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$fillWallpapersWithCustom$7;
        lambda$fillWallpapersWithCustom$7 = this.f$0.lambda$fillWallpapersWithCustom$7(this.f$1, this.f$2, this.f$3, obj, obj2);
        return lambda$fillWallpapersWithCustom$7;
    }
}
