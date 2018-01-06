package malm.tech.actors

import javax.jmdns.ServiceInfo

import akka.NotUsed
import akka.actor._
import akka.stream.scaladsl.Flow
import play.api.libs.json.JsValue
import Messages._
class WebsocketActor(flow:Flow[JsValue,JsValue,NotUsed]) extends Actor {

  override def receive = {
    case  NewService(name:String, serviceType:String, info:ServiceInfo) =>
      print("WHATEVER MAN! %s",{info})
      print("Case by case!: 1")
    case RemovedService(name,serviceType,info) =>
      print("THIS ONE IS GONE! %s",{info})
      print("Case by case!: 2")
    case ResolvedService(name,serviceType,info) =>
      print("Finally fouhnd! %s",{info})
      print("Case by case!: 3")


  }
}
