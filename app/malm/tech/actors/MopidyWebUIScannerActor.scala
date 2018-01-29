package malm.tech.actors

import javax.jmdns.ServiceInfo

import akka.actor.{Actor, Props}

import malm.tech.actors.Messages._,malm.tech.mopidy.WebScraper

import play.api.libs.ws.{WSClient, WSResponse}
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object MopidyWebUIScannerActor {

  case class ScanResult(name:String,url:String)
  def props(wSClient: WSClient)(implicit executionContext: ExecutionContext): Props ={
     Props(new MopidyWebUIScannerActor(wSClient)(executionContext))
  }
}
class MopidyWebUIScannerActor(ws:WSClient)(implicit executionContext: ExecutionContext) extends Actor{

  override def receive:Receive={
    case ScanWebUI(info:ServiceInfo) =>
      println("Scan it magistern scan it: "+info)
      var urls = info.getURLs()
      val replyTo = sender()
      ws.url(urls(0)).get() onComplete {
        case Success(response:WSResponse) =>

          val reply = ScanWebUIResult(info, WebScraper.removeDtdAndGetUIs(response.body))
          replyTo ! reply
        case Failure(t) => println("Failed scrape"+t)
      }
    case x:Any => println(s"Unknown command $x")
  }

}
