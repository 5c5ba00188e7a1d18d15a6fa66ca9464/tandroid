package org.telegram.messenger.voip;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;

/* loaded from: classes3.dex */
public class JNIUtilities {
    public static String[] getCarrierInfo() {
        String str;
        String str2;
        int defaultDataSubscriptionId;
        TelephonyManager telephonyManager = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
        if (Build.VERSION.SDK_INT >= 24) {
            defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            telephonyManager = telephonyManager.createForSubscriptionId(defaultDataSubscriptionId);
        }
        if (TextUtils.isEmpty(telephonyManager.getNetworkOperatorName())) {
            return null;
        }
        String networkOperator = telephonyManager.getNetworkOperator();
        if (networkOperator == null || networkOperator.length() <= 3) {
            str = "";
            str2 = "";
        } else {
            str = networkOperator.substring(0, 3);
            str2 = networkOperator.substring(3);
        }
        return new String[]{telephonyManager.getNetworkOperatorName(), telephonyManager.getNetworkCountryIso().toUpperCase(), str, str2};
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0012, code lost:
    
        r0 = r0.getLinkProperties(r1);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String getCurrentNetworkInterfaceName() {
        Network activeNetwork;
        LinkProperties linkProperties;
        String interfaceName;
        ConnectivityManager connectivityManager = (ConnectivityManager) ApplicationLoader.applicationContext.getSystemService("connectivity");
        activeNetwork = connectivityManager.getActiveNetwork();
        if (activeNetwork == null || linkProperties == null) {
            return null;
        }
        interfaceName = linkProperties.getInterfaceName();
        return interfaceName;
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x001b, code lost:
    
        r0 = r0.getLinkProperties(r1);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String[] getLocalNetworkAddressesAndInterfaceName() {
        Network activeNetwork;
        LinkProperties linkProperties;
        List linkAddresses;
        String interfaceName;
        InetAddress address;
        ConnectivityManager connectivityManager = (ConnectivityManager) ApplicationLoader.applicationContext.getSystemService("connectivity");
        String str = null;
        if (Build.VERSION.SDK_INT >= 23) {
            activeNetwork = connectivityManager.getActiveNetwork();
            if (activeNetwork == null || linkProperties == null) {
                return null;
            }
            linkAddresses = linkProperties.getLinkAddresses();
            Iterator it = linkAddresses.iterator();
            String str2 = null;
            while (it.hasNext()) {
                address = JNIUtilities$$ExternalSyntheticApiModelOutline3.m(it.next()).getAddress();
                if (address instanceof Inet4Address) {
                    if (!address.isLinkLocalAddress()) {
                        str = address.getHostAddress();
                    }
                } else if ((address instanceof Inet6Address) && !address.isLinkLocalAddress() && (address.getAddress()[0] & 240) != 240) {
                    str2 = address.getHostAddress();
                }
            }
            interfaceName = linkProperties.getInterfaceName();
            return new String[]{interfaceName, str, str2};
        }
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            if (networkInterfaces == null) {
                return null;
            }
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface nextElement = networkInterfaces.nextElement();
                if (!nextElement.isLoopback() && nextElement.isUp()) {
                    Enumeration<InetAddress> inetAddresses = nextElement.getInetAddresses();
                    String str3 = null;
                    String str4 = null;
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress nextElement2 = inetAddresses.nextElement();
                        if (nextElement2 instanceof Inet4Address) {
                            if (!nextElement2.isLinkLocalAddress()) {
                                str3 = nextElement2.getHostAddress();
                            }
                        } else if ((nextElement2 instanceof Inet6Address) && !nextElement2.isLinkLocalAddress() && (nextElement2.getAddress()[0] & 240) != 240) {
                            str4 = nextElement2.getHostAddress();
                        }
                    }
                    return new String[]{nextElement.getName(), str3, str4};
                }
            }
            return null;
        } catch (Exception e) {
            FileLog.e(e);
            return null;
        }
    }

    public static int getMaxVideoResolution() {
        return 320;
    }

    public static String getSupportedVideoCodecs() {
        return "";
    }

    public static int[] getWifiInfo() {
        try {
            WifiInfo connectionInfo = ((WifiManager) ApplicationLoader.applicationContext.getSystemService("wifi")).getConnectionInfo();
            return new int[]{connectionInfo.getRssi(), connectionInfo.getLinkSpeed()};
        } catch (Exception unused) {
            return null;
        }
    }
}
