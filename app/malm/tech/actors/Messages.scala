package malm.tech.actors

import java.net.InetAddress
import javax.jmdns.ServiceInfo

object Messages {
  case class NewService(name:String, serviceType : String, info : ServiceInfo )
  case class RemovedService(name:String, serviceType : String, info : ServiceInfo )
  case class ResolvedService(name:String, serviceType : String, info : ServiceInfo )

}
class Messages {

}
