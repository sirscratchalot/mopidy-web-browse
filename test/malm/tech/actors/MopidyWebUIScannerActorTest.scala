package malm.tech.actors

import javax.jmdns.{JmDNS, ServiceInfo}

import akka.actor.{Actor, ActorRef, Props}
import malm.tech.actors.Messages.{ScanWebUI, ScanWebUIResult}
import malm.tech.actors.MopidyWebUIScannerActor.ScanResult
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import org.scalatest.mock.MockitoSugar
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.global

class MopidyWebUIScannerActorTest extends PlaySpec with OneAppPerTest with MockitoSugar {

  implicit val exec = global
    val mockInfo = mock[ServiceInfo]
  "Actor" should "respond to call with parsed results".in {
    val wsmock = mock[WSClient]
    when(mockInfo.getURLs()).thenReturn(Array("http://localhost:123"))
    val mockResponse = mock[WSResponse]
    val mockRequest = mock[WSRequest]
    when(mockResponse.xml) thenReturn html
    when(wsmock.url(any[String])).thenReturn(mockRequest)
      when(mockRequest.get).
      thenReturn(Future.successful(mockResponse))
    val actorToTest = app.actorSystem.actorOf(MopidyWebUIScannerActor.props(wsmock))
    val testActor = app.actorSystem.actorOf(Props(new TestActor(actorToTest)))
    testActor ! "call"
    Thread.sleep(500)
    testActor ! "fail?"
    Thread.sleep(500)
  }
  class TestActor(target:ActorRef) extends Actor {
    var triggered = false;
    override def receive: Receive = {
      case "call" =>
        target ! ScanWebUI(mockInfo)
      case x:ScanWebUIResult =>
        println("Parsed scan: "+x)
        x.links must be
          List[ScanResult](ScanResult("musicbox_webclient","/musicbox_webclient/")
            ,ScanResult("musicbix_webclient","/musicbix_webclient/"))
        triggered = true;
      case "fail?" => assert(triggered) //Should never happen
      case x:Any => println("Got replyyyyy "+ x)
  }
  }
  val html =
    <html>
      <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>Mopidy</title>
        <link rel="stylesheet" type="text/css" href="mopidy.css"></link>
      </head>
      <body>
        <div class="box focus">
          <h1>Mopidy</h1>
          <p>This web server is a part of the Mopidy music server. To learn more
            about Mopidy, please visit
            <a href="http://www.mopidy.com/">www.mopidy.com</a>.</p>
        </div>
        <div class="box">
          <h2>Web clients</h2>
          <ul>

            <li><a href="/musicbox_webclient/">musicbox_webclient</a></li>
            <li><a href="/musicbix_webclient/">musicbix_webclient</a></li>

          </ul>
          <p>Web clients which are installed as Mopidy extensions will
            automatically appear here.</p>
        </div>
      </body>
    </html>
}
