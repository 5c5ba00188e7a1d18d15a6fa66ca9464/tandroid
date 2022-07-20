package com.huawei.hms.framework.common;

import android.text.TextUtils;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLProtocolException;
/* loaded from: classes.dex */
public class ExceptionCode {
    public static final int CANCEL = 10000100;
    private static final String CONNECT = "connect";
    public static final int CONNECTION_ABORT = 10000402;
    public static final int CONNECTION_REFUSED = 10000404;
    public static final int CONNECTION_RESET = 10000401;
    public static final int CONNECT_FAILED = 10000403;
    public static final int CRASH_EXCEPTION = 10000000;
    public static final int INTERRUPT_CONNECT_CLOSE = 10000405;
    public static final int INTERRUPT_EXCEPTION = 10000804;
    public static final int NETWORK_CHANGED = 10000201;
    public static final int NETWORK_IO_EXCEPTION = 10000802;
    public static final int NETWORK_TIMEOUT = 10000101;
    public static final int NETWORK_UNREACHABLE = 10000200;
    public static final int NETWORK_UNSUPPORTED = 10000102;
    public static final int PROTOCOL_ERROR = 10000801;
    private static final String READ = "read";
    public static final int READ_ERROR = 10000601;
    public static final int ROUTE_FAILED = 10000301;
    public static final int SHUTDOWN_EXCEPTION = 10000202;
    public static final int SOCKET_CLOSE = 10000406;
    public static final int SOCKET_CONNECT_TIMEOUT = 10000400;
    public static final int SOCKET_READ_TIMEOUT = 10000600;
    public static final int SOCKET_TIMEOUT = 10000803;
    public static final int SOCKET_WRITE_TIMEOUT = 10000700;
    public static final int SSL_HANDSHAKE_EXCEPTION = 10000501;
    public static final int SSL_PEERUNVERIFIED_EXCEPTION = 10000502;
    public static final int SSL_PROTOCOL_EXCEPTION = 10000500;
    public static final int UNABLE_TO_RESOLVE_HOST = 10000300;
    public static final int UNEXPECTED_EOF = 10000800;
    private static final String WRITE = "write";

    public static int getErrorCodeFromException(Exception exc) {
        if (exc == null) {
            return 10000802;
        }
        if (!(exc instanceof IOException)) {
            return 10000000;
        }
        String message = exc.getMessage();
        if (message == null) {
            return 10000802;
        }
        String lowerCase = StringUtils.toLowerCase(message);
        int errorCodeFromMsg = getErrorCodeFromMsg(lowerCase);
        if (errorCodeFromMsg != 10000802) {
            return errorCodeFromMsg;
        }
        if (exc instanceof SocketTimeoutException) {
            return getErrorCodeSocketTimeout(exc);
        }
        if (exc instanceof ConnectException) {
            return 10000403;
        }
        if (exc instanceof NoRouteToHostException) {
            return 10000301;
        }
        if (exc instanceof SSLProtocolException) {
            return 10000500;
        }
        if (exc instanceof SSLHandshakeException) {
            return 10000501;
        }
        if (exc instanceof SSLPeerUnverifiedException) {
            return 10000502;
        }
        if (exc instanceof UnknownHostException) {
            return 10000300;
        }
        if (exc instanceof InterruptedIOException) {
            return lowerCase.contains("connection has been shut down") ? 10000405 : 10000804;
        } else if (!(exc instanceof ProtocolException)) {
            return errorCodeFromMsg;
        } else {
            return 10000801;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0039, code lost:
        if (r8.equals("read") == false) goto L4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static int getErrorCodeSocketTimeout(Exception exc) {
        char c = 0;
        String checkExceptionContainsKey = checkExceptionContainsKey(exc, "connect", "read", "write");
        checkExceptionContainsKey.hashCode();
        switch (checkExceptionContainsKey.hashCode()) {
            case 3496342:
                break;
            case 113399775:
                if (checkExceptionContainsKey.equals("write")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 951351530:
                if (checkExceptionContainsKey.equals("connect")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return 10000600;
            case 1:
                return 10000700;
            case 2:
                return 10000400;
            default:
                return 10000803;
        }
    }

    private static int getErrorCodeFromMsg(String str) {
        if (str.contains("unexpected end of stream")) {
            return 10000800;
        }
        if (str.contains("unable to resolve host")) {
            return 10000300;
        }
        if (str.contains("read error")) {
            return 10000601;
        }
        if (str.contains("connection reset")) {
            return 10000401;
        }
        if (str.contains("software caused connection abort")) {
            return 10000402;
        }
        if (str.contains("failed to connect to")) {
            return 10000403;
        }
        if (str.contains("connection refused")) {
            return 10000404;
        }
        if (str.contains("connection timed out")) {
            return 10000400;
        }
        if (str.contains("no route to host")) {
            return 10000301;
        }
        if (str.contains("network is unreachable")) {
            return 10000200;
        }
        return str.contains("socket closed") ? 10000406 : 10000802;
    }

    private static String checkExceptionContainsKey(Exception exc, String... strArr) {
        return checkStrContainsKey(StringUtils.toLowerCase(exc.getMessage()), strArr);
    }

    private static String checkStrContainsKey(String str, String... strArr) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        for (String str2 : strArr) {
            if (str.contains(str2)) {
                return str2;
            }
        }
        return "";
    }
}
