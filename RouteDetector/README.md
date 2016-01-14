<h1>RouteDetector desktop application</h1>
RouteDetector desktop aplication is javafx-maven project with 1.8.0 minimum required jre verison for running. It is part of RouteDetector tracking system. It connects to RouteDetector server, and retrieves locations 
of connected GPRS/GPS devices. Its purpose is to enable end user to manage vehicles, drivers and visualize locations on Google Maps.
<h2>Installation</h2>
Notice that two files requred are excluded from repository. Connection properties files ("rmi.properties" and "config.properties")
located in src/main/resources/properties/ are excluded from repository. File named "rmi.properties" contains socket port number, rmi port number, name of RMI binded object, and server address.
File named "config.properties" contains ssl cipher suite, protocol, keystore password, and truststore password.
<p/>
Compiled executable jar should be placed in same folder with "properties" and "map_resources" folders. Html file, css and 
js files are placed in "map_resources" folder. WebView opens html file which uses Google Maps Api to show the map. "properties" folder
contains several files: "additional.properties", "cacerts", "keystore" and "login.properties".
<p/>
File named "additional.properties" contains link to RouteDetector website. Files named "cacerts", "keystore" are certificates truststores,
and file "login.properties" holds previous login username and organization e-mail.
<br/>
Both folders are excluded from repository.

<h2>Usage</h2> 

Usage is descripted on RouteDetector website.

<h2>License</h2>

Project is licensed with MIT license.
