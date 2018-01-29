package services

import javax.inject._
import javax.jmdns.JmDNS

import akka.actor.{ActorSystem, Props}
import malm.tech.actors.{JmDNSActor, MopidyWebUIScannerActor, ReportActor}
import play.api.inject.ApplicationLifecycle
import play.api.libs.ws.WSClient

import scala.concurrent.{ExecutionContext, Future}

/**
  * Create the ReportActor instance.
 */
@Singleton
class ActorSetup @Inject()(actorSystem: ActorSystem, appLifecycle: ApplicationLifecycle,ec:ExecutionContext,wSClient: WSClient) {
  val mopidyWebUiScannerRef = actorSystem.actorOf(MopidyWebUIScannerActor.props(wSClient)(ec))
  val reportActorRef = actorSystem.actorOf(Props(new ReportActor(mopidyWebUiScannerRef)))
  val jmDNSActorRef = actorSystem.actorOf(Props(JmDNSActor(reportActorRef,ReportActor.mopidyServices)))
  def addPresets(): Unit ={
   //Whatever means I want to use to add hardcoded servers.
  }

  appLifecycle.addStopHook { () =>
    Future.successful(())
  }
}
