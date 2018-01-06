package malm.tech.actors
import akka.actor._
import javax.jmdns.{JmDNS, ServiceEvent, ServiceListener, ServiceTypeListener}

import malm.tech.actors.JmDNSActor.AddChild

import scala.concurrent.ExecutionContext
object JmDNSActor {
  case class AddChild(name:String)
  def apply(reportActor: ActorRef,services:Array[String])(implicit  ec:ExecutionContext): JmDNSActor ={
    val jmDNS = JmDNS.create("0.0.0.0")
    return new JmDNSActor(reportActor,jmDNS,services)
  }


}
/**
  * Polling or flow JmDNS implementation?
  */
class JmDNSActor private (reportActor:ActorRef,jmDNS: JmDNS,services:Array[String]) extends Actor {
  import Messages._
   jmDNS.addServiceTypeListener(
    new ServiceTypeListener {
      override def subTypeForServiceTypeAdded(event: ServiceEvent): Unit = {
        print("New subtype added " +event.getName+" "+event.getType())
      }
      override def serviceTypeAdded(event: ServiceEvent): Unit = {
        print("New type added " +event.getName+" "+event.getType())
      }
    }
  )
  services.toStream.foreach(service => jmDNS.addServiceListener(service,new ServiceListener {
    override def serviceAdded(event: ServiceEvent): Unit = {
     reportActor ! newService(event.getName,event.getType,event.getInfo)
    }

    override def serviceResolved(event: ServiceEvent): Unit = {
      reportActor ! newService(event.getName,event.getType,event.getInfo)
    }

    override def serviceRemoved(event: ServiceEvent): Unit = ???
  }
  override def receive = { case AddChild(name:String)=>
   sender() ! "WHOOO ARE YOU!?! WHOOO... oh.. I guess you're " + name
  }
}
