package com.microsoft.appcenter.ingestion.models.json;

import com.microsoft.appcenter.ingestion.models.Model;
import java.util.List;

/* loaded from: classes.dex */
public interface ModelFactory {
    Model create();

    List createList(int i);
}
