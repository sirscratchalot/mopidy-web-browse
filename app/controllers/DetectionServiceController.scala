package controllers

import akka.actor.{ActorSystem, Props}
import javax.inject._

import akka.stream.Materializer
import malm.tech.actors.WebsocketActor
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import services.ActorSetup

import scala.concurrent.{ExecutionContext, Future, Promise}

@Singleton
class DetectionServiceController @Inject()(jmdnsHandler:ActorSetup)
(implicit exec: ExecutionContext,actorSystem: ActorSystem,mat:Materializer) extends Controller {

   /**
    *
    */
  def setupWebsocket = WebSocket.accept[JsValue,JsValue] { //No validation required for service detection.
    request =>
    val ref = ActorFlow.actorRef[JsValue,JsValue] {
      out => Props(new WebsocketActor(out,jmdnsHandler.reportActorRef))
    }
      ref
  }

}
