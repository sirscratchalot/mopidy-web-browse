package malm.tech.helpers.discovery;

import malm.tech.helpers.javahelpers.CollectionTools;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.stream.Collectors;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;

public class ServiceDiscovery {
    public void scanIPv4(String interfaceName,String... serviceTypes) throws IOException{
        JmDNS jmDNS = JmDNS.create(interfaceName);

        //List<InetAddress> inetInterfaces = CollectionTools.enumAsIter(nets).filter(inf -> {
        //    try {
        //        return inf.isUp();
        //    } catch (SocketException e) {
        //        return false;
        //    }
        //}).flatMap(x -> {
        //    return CollectionTools.enumAsIter(x.getInetAddresses())
        //            .filter(in -> {
        //                return in instanceof Inet4Address;
        //            });
        //}).collect(Collectors.toList());

        //List<JmDNS> collect = inetInterfaces.stream().map(ip -> {
        //    try {
        //        return Optional.<JmDNS>of(JmDNS.create(ip));
        //    } catch (IOException e) {
        //        return Optional.<JmDNS>empty();
        //    }
        //}).filter(opt -> opt.isPresent()).map(opt -> opt.get()).collect(Collectors.toList());


    }
}
