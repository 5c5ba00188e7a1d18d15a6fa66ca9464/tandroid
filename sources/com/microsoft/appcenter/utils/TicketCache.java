package com.microsoft.appcenter.utils;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class TicketCache {
    private static final Map sTickets = new HashMap();

    public static String getTicket(String str) {
        return (String) sTickets.get(str);
    }
}
