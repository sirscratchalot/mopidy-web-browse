package malm.tech.mopidy

import malm.tech.actors.MopidyWebUIScannerActor.ScanResult

object WebScraper {
  def removeDtdAndGetUIs(body:String): List[ScanResult] ={
    val xml = scala.xml.XML.loadString(body.replaceAll("<DOCTYPE.*>|<link.*>|<meta.*>","")) //Removes elements not Xhtml
    return getMopidyUis(xml)

  }
  //More trouble than it's worth to use actual XML parsers due to DOCTYPE bug.
  private def getMopidyUis(htmlResult:scala.xml.NodeSeq): List[ScanResult] ={
      htmlResult.map(p=> p \\ "a").flatMap(ns=>ns.filter(n=> n.text != "www.mopidy.com" ))
       .map(p=> ScanResult(p.text,p \@ "href")).toList
  }
}
