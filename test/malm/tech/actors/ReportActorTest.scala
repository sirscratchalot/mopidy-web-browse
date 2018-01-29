package malm.tech.actors

import javax.jmdns.ServiceInfo

import akka.actor.{Actor, ActorRef, Props}
import org.scalatest.FunSuite
import org.scalatestplus.play.{OneAppPerTest, PlaySpec}
import play.api.libs.json.{JsValue, Json}
import malm.tech.actors.Messages._
import org.scalatest.mock.MockitoSugar
import play.api.Application

import scala.concurrent.ExecutionContext.global
import scala.collection.mutable.ListBuffer

class ReportActorTest extends PlaySpec with OneAppPerTest with MockitoSugar{
  implicit val exec = global

  val serviceInfo = ServiceInfo.create("_mopidy-http._tcp.local.", "FakeOne", "", 9999, "I am not a real service.")
  "Registering an actor" should {
    "Pick up new JmDNS registrations" in {
      val fakeUiScanner = app.actorSystem.actorOf(Props(new fakeUiScanner))
      val testActor = getTestActor(4)
      val props = Props(new ReportActor(testActor(0)))
      val reportActor = app.actorSystem.actorOf(props)
      app.actorSystem.actorOf(Props(JmDNSActor(reportActor,ReportActor.mopidyServices)))
      testActor.foreach(a => {
        reportActor ! Messages.AddListeningActor(a)
        Thread.sleep(500)
        reportActor ! NewService(serviceInfo.getName,serviceInfo.getType,serviceInfo)
        Thread.sleep(500)
        reportActor ! ResolvedService(serviceInfo.getName,serviceInfo.getType,serviceInfo)
        Thread.sleep(500)
        reportActor ! RemovedService(serviceInfo.getName,serviceInfo.getType,serviceInfo)
        Thread.sleep(500)
        a ! "Triggered?"
        Thread.sleep(500)

      })
      fakeUiScanner ! "Triggered?"
    }

    Thread.sleep(500)
  }


  def getTestActor(number:Int)(implicit app:Application) = {
    val actors = ListBuffer[ActorRef]()
    for(a <- 1 to number)
      actors += app.actorSystem.actorOf(
        Props(new TestActor()))
    actors.toList
  }

  class TestActor extends Actor{
    var triggered = false
    var triggeredResolve = false
    var triggeredRemove = false
    import malm.tech.actors.Messages._
    override def receive: Receive =  {
      case  x:NewService  =>
        triggered=true
      case  x:ResolvedService  =>
        triggeredResolve=true
      case  x:RemovedService  =>
        triggeredRemove=true
      case "Triggered?" =>
        (triggered &&
          triggeredRemove &&
          triggeredResolve) must be(true)
    }
  }
  class fakeUiScanner extends Actor {
    var triggered = false
    override def receive: Receive = {
      case "Triggered?" =>
        triggered must be(true)
      case x:Any => { println("Got it!")
        triggered = true
      }

    }
  }
}
