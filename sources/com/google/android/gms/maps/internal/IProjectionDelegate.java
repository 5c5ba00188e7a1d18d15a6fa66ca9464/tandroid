package com.google.android.gms.maps.internal;

import android.os.IInterface;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.maps.model.LatLng;

/* loaded from: classes.dex */
public interface IProjectionDelegate extends IInterface {
    IObjectWrapper toScreenLocation(LatLng latLng);
}
