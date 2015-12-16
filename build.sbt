organization := "eu.shubert"

name := "lifted"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.6"

libraryDependencies ++= {
  val liftVersion = "2.6.2"
  Seq(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile",
    "org.eclipse.jetty" % "jetty-webapp" % "9.3.0.M2" % "container,test,compile",
    "org.slf4j" % "slf4j-jdk14" % "1.7.12",
    "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided"
    //"org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,compile" artifacts Artifact("javax.servlet", "jar", "jar")
  )
}

javaOptions in Jetty ++= Seq(
  "-Xdebug",
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
)

enablePlugins(JettyPlugin)