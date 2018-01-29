package controllers

import java.net.URI
import java.lang.Thread
import javax.jmdns.{JmDNS, ServiceInfo}

import io.backchat.hookup.{DefaultHookupClient, HookupClientConfig, JsonMessage, Success}
import org.scalatestplus.play.{OneAppPerTest, OneServerPerTest, PlaySpec}
import play.api.libs.json.{JsValue, Json}
import malm.tech.helpers.JsonWriters._

import scala.collection.mutable.ListBuffer

class DetectionServiceControllerTest extends PlaySpec with OneServerPerTest {

  val serviceInfo = ServiceInfo.create("_mopidy-http._tcp.local.","FakeOne","",9999,"I am not a real service.")
  override lazy val port = 9000

  "Websocket" should {
    val jmdnsloose = JmDNS.create("127.0.0.1")
    var list =  List[JsValue]()
    "Receive examples" in {
      val hookupClient = new DefaultHookupClient(HookupClientConfig(URI.create("ws://localhost:9000/flow"))) {
        def receive =  {
          case x:JsonMessage => println(x.content.toString)
        }
        connect() onSuccess {
          case Success => send("{}")
        }
      }
      jmdnsloose.registerService(serviceInfo)
      jmdnsloose.unregisterService(serviceInfo)
      Thread.sleep(20000)
      jmdnsloose.close()
    }

  }
}
