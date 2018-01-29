package malm.tech.helpers.discovery

import java.net.Inet4Address
import javax.jmdns._

import malm.tech.actors.JmDNSActor
import org.scalatestplus.play._
import akka.actor._
import org.scalatest.Matchers

import scala.concurrent.Await

class DiscoverySpec extends PlaySpec with OneAppPerTest {

  val serviceInfo = ServiceInfo.create("_test._tcp.local","FakeOne","",9999,"I am not a real service.")
  val jmdnsloose = JmDNS.create("0.0.0.0")

  "Discovery actor" should {
    "Discover via JmDNS" in  {
      val jmDNSBase = JmDNS.create("127.0.0.1") //Should always be detectable..
      jmDNSBase.registerService(serviceInfo)
      val test = app.actorSystem.actorOf(Props(new TestActor()))
      val jmDNS = app.actorSystem.actorOf(
        Props(JmDNSActor(test,Array(serviceInfo.getType,"_mopidy-http._tcp.local.","_googlecast._tcp.local.",
          "_airport._tcp.local.","_smb._tcp.local.","_mpd._tcp.local.","_http._tcp.local."))))
      Thread.sleep(10000)
      jmDNSBase.unregisterService(serviceInfo)
      test ! "Triggered?"
      Thread.sleep(50)
    }

  }

  class TestActor extends Actor{
    var triggered = false
    var triggeredType = false
    var triggeredResolve = false
    import malm.tech.actors.Messages._
    override def receive: Receive =  {
      case x:NewServiceType => println(s"New type ${x.serviceType}")
        triggeredType = true
      case x:NewService => println(s"${x.name} " +
        s"${x.serviceType} " +
        s"${x.info.getInet4Addresses.foldLeft("")((str,ia)=>str+","+ia.getHostAddress)}")
        triggered=true
      case x:ResolvedService => println(s"Resolved ${x.name} " +
        s"${x.serviceType} " +
        s"${x.info.getInet4Addresses.foldLeft("")((str,ia)=>str+","+ia.getHostAddress)} " +
        s"${x.info.getPort}")
        triggeredResolve=true
      case "Triggered?" =>
        triggered must be(true)
        triggeredType must be(true)
        triggeredResolve must be(true)
    }
  }
}


