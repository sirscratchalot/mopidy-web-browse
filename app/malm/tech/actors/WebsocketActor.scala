package malm.tech.actors

import javax.jmdns.ServiceInfo

import akka.actor._
import malm.tech.actors.Messages._
import malm.tech.helpers.JsonWriters._
import play.api.libs.json.Json

class WebsocketActor(out:ActorRef,in:ActorRef) extends Actor {
  in ! AddListeningActor(context.self)
  in ! ListServices //Get all cached services
  var sentKeys = List[String]()
  override def receive = {
    case BulkServices(infos:Set[ServiceInfo]) =>
      println("Bulk")
      infos.foreach(i =>  receive(
        ResolvedService(i.getName,i.getType,i))
      )
    case BulkWebUi(links:Map[String,MopidyWebUIScannerActor.ScanResult]) =>
      links.foreach(e => e._2.foreach(ln => receive(ResolvedWebUi(e._1,ln.name,ln.url)))
      )
    case x: ResolvedWebUi =>
      println(s"Resolved $x")
      out ! Json.toJson(x)
    case  x:NewService =>
      out ! Json.toJson(x)

    case x:RemovedService =>
      out ! Json.toJson(x)

    case x:ResolvedService =>
      if(!sentKeys.contains(x.info.getKey)){
        out ! Json.toJson(x)
      }
      sentKeys=sentKeys :+ x.info.getKey

    case x:Any => println(s"Unknown command..: $x")
  }

  override def postStop() = {
    in ! RemoveListeningActor(context.self)
  }
}
