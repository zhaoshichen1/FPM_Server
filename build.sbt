name := """server"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "javax.ws.rs" % "jsr311-api" % "1.1.1" % "provided",
  "org.glassfish" % "javax.ejb" % "3.0.1" % "provided",
  "org.eclipse.persistence" % "javax.persistence" % "2.0.0" % "provided"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
