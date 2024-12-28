package org.telegram.messenger.voip;

import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.tl.TL_phone;

/* loaded from: classes3.dex */
public interface VoIPServiceState {

    public abstract /* synthetic */ class -CC {
        public static long $default$getCallDuration(VoIPServiceState voIPServiceState) {
            return 0L;
        }
    }

    void acceptIncomingCall();

    void declineIncomingCall();

    long getCallDuration();

    int getCallState();

    TL_phone.PhoneCall getPrivateCall();

    TLRPC.User getUser();

    boolean isOutgoing();

    void stopRinging();
}
