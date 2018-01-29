package malm.tech.actors

import javax.jmdns.ServiceInfo

import akka.actor._

object ReportActor {
  val mopidyServices =Array("_mopidy-http._tcp.local.")
  def props = Props[ReportActor]
}

/**
  * To enable WebUI scanning of discovered Mopidy servers,
  * a MopidyWebUIScannerActor ActorRef should be provided.
  * @param webUiScactor
  */
class ReportActor(webUiScactor:ActorRef) extends Actor{
  import malm.tech.actors.ReportActor._,malm.tech.actors.Messages._

  var actorRefs = Set[ActorRef]()
  var resolvedServices = Set[ServiceInfo]()
  var parsedUis = Map[String,List[MopidyWebUIScannerActor.ScanResult]]()
  var serviceTypes = Set[String]()

  override def receive: Receive = {
    case AddListeningActor(ar:ActorRef) =>
      actorRefs = actorRefs + ar
      sender() ! ConfirmAdded(ar,actorRefs.size)
    case RemoveListeningActor(ar:ActorRef) =>
      actorRefs = actorRefs - ar
      sender() ! ConfirmRemoved(ar,actorRefs.size)
    case message:NewService =>
      actorRefs.foreach(ar => ar ! message)
    case message:RemovedService =>
      resolvedServices = resolvedServices - message.info
      actorRefs.foreach(ar => ar ! message)
    case message:ResolvedService =>
      resolvedServices = resolvedServices + message.info
      webUiScactor ! ScanWebUI(message.info)
      actorRefs.foreach(ar => ar ! message)
    case message:ScanWebUIResult =>
      parsedUis= parsedUis + (message.info.getKey -> message.links)
      actorRefs.foreach(ar => message.links.foreach(l => ar ! ResolvedWebUi(message.info.getKey,l.name,l.url)))
    case ListServices =>
      sender() ! BulkServices(resolvedServices)
      sender() ! BulkWebUi(parsedUis)
    case ListWebUis => sender() ! BulkServices(resolvedServices)

    case x:Any => println(s"Unknown message type $x")
  }
  val html =
    <html>
      <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>Mopidy</title>
        <link rel="stylesheet" type="text/css" href="mopidy.css"/>
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
