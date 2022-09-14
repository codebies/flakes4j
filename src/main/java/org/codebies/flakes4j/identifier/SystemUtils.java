package org.codebies.flakes4j.identifier;

import io.vavr.control.Try;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

final class SystemUtils {

    private final static Logger logger = Logger.getLogger(SystemUtils.class.getName());
    private static Integer machineId;
    private static boolean machineIdIdentified = false;

    private static Integer getSafeMachineId() {
        return Try.of(SystemUtils::extractMachineId)
                .onFailure(exp -> logger.log(Level.SEVERE, "Exception - {0} ",exp))
                .getOrNull();
    }

    private static int extractMachineId() {
        InetAddress netAddress = getNetAddress();

        if (Objects.isNull(netAddress))
            throw new IDExtractionFailureException();
        logger.log(Level.INFO,"Local IP Address :: {0}", netAddress.getHostAddress());
        return extractId(netAddress.getAddress());
    }

    private static int extractId(byte[] address) {
        return (Byte.toUnsignedInt(address[2]) << 8) + Byte.toUnsignedInt(address[3]);
    }

    private static InetAddress getNetAddress() {
        Enumeration<NetworkInterface> networkInterfaces = getNetworkInterfaceEnumeration();
        if (Objects.isNull(networkInterfaces)) return null;

        while (networkInterfaces.hasMoreElements()) {
            InetAddress inetAddress = getInetAddress(networkInterfaces.nextElement());
            if (inetAddress != null)
                return inetAddress;
        }
        return null;
    }

    private static InetAddress getInetAddress(NetworkInterface networkInterface) {
        if(isInterfaceUp(networkInterface) && !isInterfaceLoopback(networkInterface))
            return filterInetAddresses(networkInterface);
        else
            return null;
    }

    private static InetAddress filterInetAddresses(NetworkInterface networkInterface) {
        Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
        while (inetAddresses.hasMoreElements()) {
            InetAddress inetAddress = inetAddresses.nextElement();
            if (isIP4NonLoopBackAddress(inetAddress) && inetAddress.isSiteLocalAddress()) {
                return inetAddress;
            }
        }
        return null;
    }

    private static boolean isInterfaceUp(NetworkInterface networkInterface){
        return Try.of(networkInterface::isUp).getOrElse(false);
    }

    private static boolean isInterfaceLoopback(NetworkInterface networkInterface){
        return Try.of(networkInterface::isLoopback).getOrElse(false);
    }

    private static boolean isIP4NonLoopBackAddress(InetAddress inetAddress) {
        return !inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address;
    }

    private static Enumeration<NetworkInterface> getNetworkInterfaceEnumeration() {
        return Try.of(NetworkInterface::getNetworkInterfaces).getOrNull();
    }

    public static int getMachineId() {
        if(!machineIdIdentified) {
            machineId = getSafeMachineId();
            machineIdIdentified = true;
        }
        if (Objects.isNull(machineId))
            throw new IDExtractionFailureException();
        return machineId;
    }

    private static class IDExtractionFailureException extends RuntimeException {
    }

}
