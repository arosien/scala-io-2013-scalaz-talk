import net.rosien.landslide._

name := "scala-io-2013-scalaz-talk"

organization := "net.rosien"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.0.4",
  "org.specs2" %% "specs2"      % "1.12.3" % "test"
)

site.settings

LandslideSupport.settings ++ Seq(site.addMappingsToSiteDir(mappings in LandslideSupport.Landslide, ""))

LandslideSupport.Destination := "index.html"

ghpages.settings

git.remoteRepo := "git@github.com:arosien/scala-io-scalaz-scalaz-talk.git"

initialCommands := """
import scalaz._
import Scalaz._
import net.rosien.scalaz._
"""

