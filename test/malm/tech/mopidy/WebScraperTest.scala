package malm.tech.mopidy

import malm.tech.actors.MopidyWebUIScannerActor.ScanResult
import org.scalatest.{FlatSpec, FunSuite, Matchers}

import scala.collection.immutable.List
import scala.xml._

class WebScraperTest extends FlatSpec with Matchers {
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
    "WebScraper class" should "extract two webguis" in {
      val uis = WebScraper.removeDtdAndGetUIs(html.toString())
      uis should be (List[ScanResult](ScanResult("musicbox_webclient","/musicbox_webclient/")
      ,ScanResult("musicbix_webclient","/musicbix_webclient/")))

    }

          }
