package com.microsoft.appcenter.ingestion.models.json;

import com.microsoft.appcenter.ingestion.models.Model;
import java.util.List;
/* loaded from: classes.dex */
public interface ModelFactory<M extends Model> {
    /* renamed from: create */
    M mo246create();

    List<M> createList(int i);
}
