package com.microsoft.appcenter.crashes.model;

import com.microsoft.appcenter.ingestion.models.Device;
import java.util.Date;
/* loaded from: classes.dex */
public class ErrorReport {
    private Date appErrorTime;
    private Date appStartTime;
    private Device device;
    private String id;
    private String stackTrace;
    private String threadName;

    public Device getDevice() {
        return this.device;
    }

    public void setAppErrorTime(Date date) {
        this.appErrorTime = date;
    }

    public void setAppStartTime(Date date) {
        this.appStartTime = date;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setStackTrace(String str) {
        this.stackTrace = str;
    }

    public void setThreadName(String str) {
        this.threadName = str;
    }
}
