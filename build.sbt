val ScalaVer = "2.12.7"

lazy val commonSettings = Seq(
  name    := "play-meta"
, version := "0.1.0"
, scalaVersion := ScalaVer
, libraryDependencies ++= Seq(
    "com.functortech"      %% "thera-predef" % "0.1.0-SNAPSHOT"
  , "org.typelevel"        %% "cats-effect"  % "1.0.0"
  , "com.github.pathikrit" %% "better-files" % "3.6.0"
  , "org.apache.commons"   %  "commons-text" % "1.6"
  )

, addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.8")

, scalacOptions ++= Seq(
      "-deprecation"
    , "-encoding", "UTF-8"
    , "-feature"
    , "-language:existentials"
    , "-language:higherKinds"
    , "-language:implicitConversions"
    , "-language:experimental.macros"
    , "-unchecked"
    // , "-Xfatal-warnings"
    // , "-Xlint"
    // , "-Yinline-warnings"
    , "-Ywarn-dead-code"
    , "-Xfuture"
    , "-Ypartial-unification")
)

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(
    initialCommands := "import playmeta._, Main._"
  )
