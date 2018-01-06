package malm.tech.helpers.discovery

import malm.tech.actors.JmDNSActor
import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test._
import akka.actor._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class DiscoverySpec extends PlaySpec with OneAppPerTest {

  "Discovery actor" should {

    "Discover via JmDNS" in  {
    }

  }

  "HomeController" should {

    "render the index page" in {
      val home = route(app, FakeRequest(GET, "/")).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Your new application is ready.")
    }

  }

  "CountController" should {

    "return an increasing count" in {
      contentAsString(route(app, FakeRequest(GET, "/count")).get) mustBe "0"
      contentAsString(route(app, FakeRequest(GET, "/count")).get) mustBe "1"
      contentAsString(route(app, FakeRequest(GET, "/count")).get) mustBe "2"
    }

  }

}

class TestActor extends Actor {
  override def receive = {
    case Any
  }
}

