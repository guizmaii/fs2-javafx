import sbtrelease.ReleasePlugin.autoImport.ReleaseTransformations._

lazy val `fs2-javafx` = project
  .in(file("."))
  .settings(
    organization := "net.kurobako",
    name := "fs2-javafx",
    scalaVersion := "2.13.5",
    scalacOptions ++= Seq(
      "-P:bm4:no-map-id:y"
    ),
    scalacOptions ~= filterConsoleScalacOptions,
    Test / fork := true,
    Test / testForkedParallel := false,
    scalacOptions ++= List("-release", "11"),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.11.3" cross CrossVersion.full),
    libraryDependencies ++= Seq(
      "co.fs2"        %% "fs2-core"         % "2.5.3",
      "org.openjfx"    % s"javafx-base"     % "15.0.1" % Provided classifier osName,
      "org.openjfx"    % s"javafx-controls" % "15.0.1" % Provided classifier osName,
      "org.openjfx"    % s"javafx-fxml"     % "15.0.1" % Provided classifier osName,
      "org.openjfx"    % s"javafx-graphics" % "15.0.1" % Provided classifier osName,
      "org.openjfx"    % s"javafx-media"    % "15.0.1" % Provided classifier osName,
      "org.openjfx"    % s"javafx-swing"    % "15.0.1" % Provided classifier osName,
      "org.openjfx"    % s"javafx-web"      % "15.0.1" % Provided classifier osName,
      "org.scalatest" %% "scalatest"        % "3.2.5"  % Test
    ),
    developers := List(
      Developer(
        id = "tom91136",
        name = "Tom Lin",
        email = "tom91136@gmail.com",
        url("https://github.com/tom91136")
      )
    ),
    homepage := Some(url(s"https://github.com/tom91136/${name.value}")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    // publish settings
    bintrayPackage := name.value,
    bintrayReleaseOnPublish := false,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      releaseStepCommandAndRemaining("^ compile"), // still no tests =(
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("^ publish"),
      releaseStepTask(bintrayRelease),
      setNextVersion,
      commitNextVersion,
      pushChanges
    )
  )

lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"
  case _                            => throw new Exception("Unknown platform!")
}
