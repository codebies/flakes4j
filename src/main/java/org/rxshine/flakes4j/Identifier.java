package org.rxshine.flakes4j;

import java.net.*;
import java.util.Enumeration;
import java.util.Objects;
import java.util.function.Supplier;

final class Identifier {

    private final static Integer machineId;

    static{
        machineId = getSafeMachineId();
    }

    private static Integer getSafeMachineId(){
        try{
            return extractMachineId();
        }catch (IDExtractionFailureException ignored){

        }
        return null;
    }

    private static int extractMachineId(){
        InetAddress netAddress = getNetAddress();
        if(netAddress==null)
            throw new IDExtractionFailureException();
        return extractId(netAddress.getAddress());
    }

    private static int extractId(byte[] address) {
        return (Byte.toUnsignedInt(address[2]) << 8) + Byte.toUnsignedInt(address[3]);
    }

    private static InetAddress getNetAddress() {
        Enumeration<NetworkInterface> networkInterfaces = getNetworkInterfaceEnumeration();
        if(Objects.isNull(networkInterfaces)) return null;

        while (networkInterfaces.hasMoreElements()) {
            InetAddress inetAddress = getInetAddress(networkInterfaces.nextElement());
            if (inetAddress != null)
                return inetAddress;
        }
        return null;
    }

    private static InetAddress getInetAddress(NetworkInterface networkInterface) {
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
            InetAddress inetAddress = inetAddresses.nextElement();
            if (isIP4NonLoopBackAddress(inetAddress))
                return inetAddress;
        }
        return null;
    }

    private static boolean isIP4NonLoopBackAddress(InetAddress inetAddress) {
        if(!inetAddress.isLoopbackAddress() && inetAddress.getAddress().length==4){
            return true; // TODO: ADD LOCAL ADDRESS VALIDATION
        }
        return false;
    }

    private static Enumeration<NetworkInterface> getNetworkInterfaceEnumeration() {
        try {
            return NetworkInterface.getNetworkInterfaces();
        } catch (SocketException ignored) {}
        return null;
    }

    public static int getMachineId() {
        if(machineId==null)
            throw new IDExtractionFailureException();
        return machineId;
    }

    private static class IDExtractionFailureException extends RuntimeException{ }

}
