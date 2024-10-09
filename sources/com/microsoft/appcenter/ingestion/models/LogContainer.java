package com.microsoft.appcenter.ingestion.models;

import java.util.List;

/* loaded from: classes.dex */
public class LogContainer {
    private List logs;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        List list = this.logs;
        List list2 = ((LogContainer) obj).logs;
        return list != null ? list.equals(list2) : list2 == null;
    }

    public List getLogs() {
        return this.logs;
    }

    public int hashCode() {
        List list = this.logs;
        if (list != null) {
            return list.hashCode();
        }
        return 0;
    }

    public void setLogs(List list) {
        this.logs = list;
    }
}
