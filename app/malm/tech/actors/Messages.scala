package malm.tech.actors

import java.net.InetAddress
import javax.jmdns.ServiceInfo

import akka.actor.ActorRef
import malm.tech.actors.MopidyWebUIScannerActor.ScanResult

object Messages {
  //Messages for JmDNSActor and Listening actors
  case class NewService(name:String, serviceType : String, info : ServiceInfo )
  case class NewServiceType(serviceType:String)
  case class NewServiceSubType(serviceType:String,serviceSubType: String)
  case class RemovedService(name:String, serviceType : String, info : ServiceInfo )
  case class ResolvedService(name:String, serviceType : String, info : ServiceInfo )
  case class ResolvedWebUi(key:String, name : String, path : String )

  //Messages for ReportActor
  case class BulkServices(bulkServices: Set[ServiceInfo])
  case class BulkWebUi(bulkUi: Map[String,List[MopidyWebUIScannerActor.ScanResult]])

  case class ListServices()
  case class ListWebUis()

  case class AddListeningActor(actorRef: ActorRef)
  case class ConfirmRemoved(actorRef: ActorRef,numberOfListeners:Int)
  case class ConfirmAdded(actorRef: ActorRef,numberOfListeners:Int)
  case class RemoveListeningActor(actorRef: ActorRef)

  //Messages for MopidyUIScanner
  case class ScanWebUI(info:ServiceInfo)
  case class ScanWebUIResult(info:ServiceInfo,links:List[ScanResult])
}

