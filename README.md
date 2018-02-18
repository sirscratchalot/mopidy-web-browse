# Mopidy Server Browser

Do you have a lot of Raspberry Pis with Mopidy on them? I do.   
Keeeping track of URLs for multiple playback locations can be a bit annoying.
Mopidy server browser discovers Mopidy-servers with a HTTP GUI on your network and allows you to 
use the web interfaces without leaving the page.
 

Mopidy Server Browser detects any Mopidy HTTP server and scans it for set up Web GUIs.
You can then browse the Web-Guis on the page.  
Running this on my network means no longer having to keep track of URLs, and more importantly neither does anyone else.

<DEMO GIF HERE>

## Running Mopidy-web-browse:

Needs JRE 1.8 minimum.
Download mopidy-web-browse.1.0.zip from releases.
  ```
  unzip mopidy-web-browse-1.0.zip
  mopidy-web-browse-1.0-SNAPSHOT/bin/mopidy-web-browse -Dplay.crypto.secret=anythingReally-sinceNotSensitive -Dhttp.port=8080
 ```
 This will start the server on port 8080.
 Go to 'http://localhost:9090' to browse your mopidy servers. 

 On windows, unzip and then start from CMD:  
  'mopidy-web-browse-1.0-SNAPSHOT/bin/mopidy-web-browse.bat -Dplay.crypto.secret=anythingReally-sinceNotSensitive -Dhttp.port=8080'

If for any reason the app fails to start, you may need to remove the current PID file before trying again:  
`rm mopidy-web-browse/RUNNING_PID`

## Tech stack:

- ZeroConf / Bonjour is used to detect services via the JmDNS library [Link](https://github.com/jmdns/jmdns)
- The server part of Mopidy Web Browse is implemented in [Play Framework](https://www.playframework.com) 2.5 using [Scala](https://scala-lang.org).
- Detected services are streamed to the front-end via WebSockets implemented via [akka](https://akka.io/) flows and actors . 
- The frontend is implemented as a [ReactJS](https://reactjs.org) single page app, a minified version is included with the server.
- The source for the front-end is available in a separate repository: <GUI REPO LINK>


