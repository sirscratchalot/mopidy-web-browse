package malm.tech.helpers

import javax.jmdns.ServiceInfo

import malm.tech.actors.Messages.{NewService, RemovedService, ResolvedService, ResolvedWebUi}
import play.api.libs.json.{Json, Writes}

object JsonWriters {
  implicit def InfoWriter = new Writes[ServiceInfo]{
    def writes(info:ServiceInfo) = {
      Json.obj( "name"->info.getName,
        "type" -> info.getType,
        "port" -> info.getPort(),
        "protocol" -> info.getProtocol(),
        "urls" -> info.getURLs(),
        "subtype" -> info.getSubtype,
        "ip"->info.getHostAddresses,
        "key"->info.getKey
      )
    }
  }
  implicit def RemoveWriter = new Writes[RemovedService] {
    def writes(removedService: RemovedService) = {
      Json.obj(
        "event"->"Removed",
        "info"->removedService.info
      )
    }
  }
  implicit def NewWriter = new Writes[NewService] {
    def writes(newService: NewService) = {
      Json.obj(
        "event"->"Detected",
        "info"->newService.info
      )
    }
  }
  implicit def ResolvedWriter = new Writes[ResolvedService] {
    def writes(resolvedService: ResolvedService) = {
      Json.obj(
        "event"->"Resolved",
        "info"->resolvedService.info
      )
    }}
  implicit def WebUIScanResolvedWriter = new Writes[ResolvedWebUi] {
    def writes(resolvedWebUi: ResolvedWebUi) = {
      Json.obj(
        "event"->"UIResolved",
        "key"->resolvedWebUi.key,
        "name"->resolvedWebUi.name,
        "path" -> resolvedWebUi.path
      )
    }}

}
