package com.google.firebase.messaging.reporting;

import com.google.android.gms.internal.firebase_messaging.zze;

/* loaded from: classes.dex */
public final class MessagingClientEventExtension {
    private static final MessagingClientEventExtension DEFAULT_INSTANCE = new Builder().build();
    private final MessagingClientEvent messaging_client_event_;

    /* loaded from: classes.dex */
    public static final class Builder {
        private MessagingClientEvent messaging_client_event_ = null;

        Builder() {
        }

        public MessagingClientEventExtension build() {
            return new MessagingClientEventExtension(this.messaging_client_event_);
        }

        public Builder setMessagingClientEvent(MessagingClientEvent messagingClientEvent) {
            this.messaging_client_event_ = messagingClientEvent;
            return this;
        }
    }

    MessagingClientEventExtension(MessagingClientEvent messagingClientEvent) {
        this.messaging_client_event_ = messagingClientEvent;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public MessagingClientEvent getMessagingClientEventInternal() {
        return this.messaging_client_event_;
    }

    public byte[] toByteArray() {
        return zze.zza(this);
    }
}
