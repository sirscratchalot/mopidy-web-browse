package malm.tech.actors
import akka.actor._
import javax.jmdns.{JmDNS, ServiceEvent, ServiceListener, ServiceTypeListener}



/**
  * JmDNSActor should be a single actor for server.
  * reportActor will have the unenviable task of keeping a tally of services.
  */
object JmDNSActor {
  def apply(reportActor: ActorRef,services:Array[String]): JmDNSActor ={
    val jmDNS = JmDNS.create("0.0.0.0")
    new JmDNSActor(reportActor,jmDNS,services)
  }


}
class JmDNSActor  (reportActor:ActorRef,jmDNS: JmDNS,services:Array[String]) extends Actor {
  import Messages._
   jmDNS.addServiceTypeListener(
    new ServiceTypeListener {
      override def subTypeForServiceTypeAdded(event: ServiceEvent): Unit = {
        reportActor ! NewServiceSubType(event.getType,event.getInfo.getSubtype)
      }
      override def serviceTypeAdded(event: ServiceEvent): Unit = {
        reportActor ! NewServiceType(event.getType)
      }
    }
  )
  services.toStream.foreach(service => jmDNS.addServiceListener(service,
    new ServiceListener {
    override def serviceAdded(event: ServiceEvent): Unit = {
     reportActor ! NewService(event.getName,event.getType,event.getInfo)
    }

    override def serviceResolved(event: ServiceEvent): Unit = {
      reportActor ! ResolvedService(event.getName,event.getType,event.getInfo)
    }

    override def serviceRemoved(event: ServiceEvent): Unit = {
      reportActor ! RemovedService(event.getName,event.getType,event.getInfo)
    }
  }))
  override def receive = {
    case x:Any=> println(s"Not much of a talker, got: $x")
  }
  override def postStop() = {
    jmDNS.close()
  }
}
