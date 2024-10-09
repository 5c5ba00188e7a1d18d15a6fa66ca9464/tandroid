package com.microsoft.appcenter.ingestion.models;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

/* loaded from: classes.dex */
public interface Log extends Model {
    void addTransmissionTarget(String str);

    Device getDevice();

    UUID getSid();

    Object getTag();

    Date getTimestamp();

    Set getTransmissionTargetTokens();

    String getType();

    String getUserId();

    void setDevice(Device device);

    void setDistributionGroupId(String str);

    void setSid(UUID uuid);

    void setTimestamp(Date date);
}
