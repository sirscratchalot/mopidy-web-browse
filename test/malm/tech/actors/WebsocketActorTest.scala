package malm.tech.actors

import javax.jmdns._

import akka.actor._
import org.scalatestplus.play._
import services.ActorSetup
import play.api.libs.json.{JsValue, Json}

import scala.collection.mutable.ListBuffer

/**
  * Add your spec here.
  * You can mock out a whole application including requests, plugins etc.
  * For more information, consult the wiki.
  */
class WebsocketActorTest extends PlaySpec with OneAppPerTest {
  val serviceInfo = ServiceInfo.create("_mopidy-http._tcp.local.","FakeOne","",9999,"I am not a real service.")
  "Discovery should reach all WebSocketActors" should {
    "All should be reached" in  {
      val setup = app.injector.instanceOf(classOf[ActorSetup])
      val jmDNSBase = JmDNS.create("127.0.0.1") //Should always be detectable..

      jmDNSBase.registerService(serviceInfo)
      jmDNSBase.unregisterService(serviceInfo)

      val testActors = getTestActor(10)
      val websocketActors = testActors.map(ta => app.actorSystem.actorOf(Props(
        new WebsocketActor(ta,setup.reportActorRef))))

      Thread.sleep(20000)
      testActors.foreach(test =>
      test ! "Triggered?")
      Thread.sleep(500)
    }

  }
  def getTestActor(number:Int) = {
    val actors = ListBuffer[ActorRef]()
    for(a <- 1 to number)actors += app.actorSystem.actorOf(Props(new TestActor()))
    actors.toList
  }

  class TestActor extends Actor{
    var triggered = false
    import malm.tech.actors.Messages._
    override def receive: Receive =  {
      case  x:JsValue if (x \ "event") == "Resolved" =>
        println(Json.prettyPrint(x))
        triggered=true
      case  x:JsValue  =>
        println(Json.prettyPrint(x))
        triggered=true
      case "Triggered?" =>
        triggered must be(true)
    }
  }
}


