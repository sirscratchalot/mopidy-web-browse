package malm.tech.actors

import akka.actor._
object ReportActor {
  def props = Props[ReportActor]
  case class CaseClassy(classy:String)
}

class ReportActor() extends Actor {

   override def receive: Receive = {
    case "Hello" => sender() ! "Hi yourself!"

  }
}
