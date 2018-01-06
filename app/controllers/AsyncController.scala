package controllers

import akka.actor.ActorSystem
import javax.inject._

import play.api._

import play.api.libs.streams.ActorFlow
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.concurrent.duration._

/**
 * This controller creates an `Action` that demonstrates how to write
 * simple asynchronous code in a controller. It uses a timer to
 * asynchronously delay sending a response for 1 second.
 *
 * @param actorSystem We need the `ActorSystem`'s `Scheduler` to
 * run code after a delay.
 * @param exec We need an `ExecutionContext` to execute our
 * asynchronous code.
 */
@Singleton
class AsyncController @Inject() (actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends Controller {

  /**
    *
   */
  def setupWebsocket = WebSocket.accept[String,String] {
    bequest =>
      ActorFlow.actorRef[String,String] {
       out => ""

      }
  }

  private def getFutureMessage(delayTime: FiniteDuration): Future[String] = {
    val promise: Promise[String] = Promise[String]()
    actorSystem.scheduler.scheduleOnce(delayTime) { promise.success("Hi!") }
    promise.future
  }
  private def iaAmATestMethod(implicit paramOne:String, f:Int => String) = {
    f(paramOne)
  }
  implicit val testImplicit = "I AM IMPLIED!"

}
