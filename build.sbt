
// Pinched shamelessly from https://tpolecat.github.io/2014/04/11/scalac-flags.html
scalacOptions ++= Seq(
  //  "-Yno-imports", // Powerful but boring. Essentially you have to pull in everything... one day.
  "-deprecation",
  "-encoding", "UTF-8",       // yes, this is 2 args
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked"
  //  "-Xfatal-warnings",
  //  "-Xlint",
  //  "-Yno-adapted-args",
  //  "-Ywarn-dead-code",        // N.B. doesn't work well with the ??? hole
  //  "-Ywarn-numeric-widen",
  //  "-Ywarn-value-discard",
  //  "-Xfuture"
)

addCommandAlias("quickcompile", ";shared_2_12/compile;core_2_12/compile;argonaut62_2_12/compile;pactSpec_2_12/compile;plugin/compile;standalone/compile;framework_2_12/compile")
addCommandAlias("quicktest", ";shared_2_12/test;core_2_12/test;argonaut62_2_12/test;pactSpec_2_12/test;plugin/test;standalone/test;framework_2_12/test")

lazy val commonSettings = Seq(
  version := "2.2.0-SNAPSHOT",
  organization := "com.itv",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
)

lazy val scalaPactPublishSettings = Seq(
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases"  at nexus + "service/local/staging/deploy/maven2")
  },
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
  pomExtra :=
    <url>https://github.com/ITV/scala-pact</url>
      <licenses>
        <license>
          <name>ITV-OSS</name>
          <url>http://itv.com/itv-oss-licence-v1.0</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:itv/scala-pact.git</url>
        <connection>scm:git:git@github.com:itv/scala-pact.git</connection>
      </scm>
      <developers>
        <developer>
          <id>davesmith00000</id>
          <name>David Smith</name>
          <organization>ITV</organization>
          <organizationUrl>http://www.itv.com</organizationUrl>
        </developer>
      </developers>
)

val scala210: String = "2.10.6"
val scala211: String = "2.11.11"
val scala212: String = "2.12.3"

val sbt13: String = "0.13.16"
val sbt1x: String = "1.0.2"

lazy val shared =
  (project in file("scalapact-shared"))
    .settings(commonSettings: _*)
    .settings(scalaPactPublishSettings: _*)
    .settings(
      name := "scalapact-shared",
      crossScalaVersions := Seq(scala210, scala211, scala212),
      crossSbtVersions := Seq(sbt13, sbt1x),
      sbtVersion in Global := sbt1x,
      scalaCompilerBridgeSource := {
        val sv = appConfiguration.value.provider.id.version
        ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
      }
    )

//lazy val shared_2_10 = shared(scala210)
//lazy val shared_2_11 = shared(scala211)
//lazy val shared_2_12 = shared(scala212)

lazy val core =
  (project in file("scalapact-core"))
    .settings(commonSettings: _*)
    .settings(scalaPactPublishSettings: _*)
    .settings(
      name := "scalapact-core",
      libraryDependencies ++= Seq("org.scala-lang.modules" %% "scala-xml" % "1.0.6").filter(_ => scalaVersion.value != scala210),
      crossScalaVersions := Seq(scala210, scala211, scala212),
      crossSbtVersions := Seq(sbt13, sbt1x),
      sbtVersion in Global := sbt1x,
      scalaCompilerBridgeSource := {
        val sv = appConfiguration.value.provider.id.version
        ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
      }
    )
    .dependsOn(shared)
    .dependsOn(argonaut62 % "provided")
    .dependsOn(http4s0150a % "provided")

//lazy val core_2_10 = core(scala210)
//  .dependsOn(shared)
//  .dependsOn(argonaut62_2_10 % "provided")
//  .dependsOn(http4s0150a_2_10 % "provided")
//  .project
//lazy val core_2_11 = core(scala211)
//  .settings(
//    libraryDependencies ++= Seq("org.scala-lang.modules" %% "scala-xml" % "1.0.6")
//  )
//  .dependsOn(shared)
//  .dependsOn(argonaut62_2_11 % "provided")
//  .dependsOn(http4s0150a_2_11 % "provided")
//  .project
//lazy val core_2_12 = core(scala212)
//  .settings(
//    libraryDependencies ++= Seq("org.scala-lang.modules" %% "scala-xml" % "1.0.6")
//  )
//  .dependsOn(shared)
//  .dependsOn(argonaut62_2_12 % "provided")
//  .dependsOn(http4s0150a_2_12 % "provided")
//  .project

lazy val http4s0150a =
  (project in file("scalapact-http4s-0-15-0a"))
    .settings(commonSettings: _*)
    .settings(scalaPactPublishSettings: _*)
    .settings(
      name := "scalapact-http4s-0-15-0a",
      libraryDependencies ++= Seq(
        "org.http4s" %% "http4s-blaze-server" % "0.15.0a",
        "org.http4s" %% "http4s-blaze-client" % "0.15.0a",
        "org.http4s" %% "http4s-dsl"          % "0.15.0a",
        "com.github.tomakehurst" % "wiremock" % "1.56" % "test"
      ),
      crossScalaVersions := Seq(scala210, scala211, scala212),
      crossSbtVersions := Seq(sbt13, sbt1x),
      scalaCompilerBridgeSource := {
        val sv = appConfiguration.value.provider.id.version
        ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
      },
      sbtVersion in Global := sbt1x,
      scalaCompilerBridgeSource := {
        val sv = appConfiguration.value.provider.id.version
        ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
      }
    )
    .dependsOn(shared)

//lazy val http4s0150a_2_10 = http4s0150a(scala210).dependsOn(shared)
//lazy val http4s0150a_2_11 = http4s0150a(scala211).dependsOn(shared)
//lazy val http4s0150a_2_12 = http4s0150a(scala212).dependsOn(shared)

lazy val http4s0162a =
  (project in file("scalapact-http4s-0-16-2a"))
    .settings(commonSettings: _*)
    .settings(scalaPactPublishSettings: _*)
    .settings(
      name := "scalapact-http4s-0-16-2a",
      libraryDependencies ++= Seq(
        "org.http4s" %% "http4s-blaze-server" % "0.16.2a",
        "org.http4s" %% "http4s-blaze-client" % "0.16.2a",
        "org.http4s" %% "http4s-dsl"          % "0.16.2a",
        "com.github.tomakehurst" % "wiremock" % "1.56" % "test"
      ),
      crossScalaVersions := Seq(scala210, scala211, scala212),
      crossSbtVersions := Seq(sbt13, sbt1x),
      scalaCompilerBridgeSource := {
        val sv = appConfiguration.value.provider.id.version
        ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
      },
      sbtVersion in Global := sbt1x,
      scalaCompilerBridgeSource := {
        val sv = appConfiguration.value.provider.id.version
        ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
      }
    )
    .dependsOn(shared)

//lazy val http4s0162a_2_10 = http4s0162a(scala210).dependsOn(shared)
//lazy val http4s0162a_2_11 = http4s0162a(scala211).dependsOn(shared)
//lazy val http4s0162a_2_12 = http4s0162a(scala212).dependsOn(shared)

lazy val http4s0162 =
  (project in file("scalapact-http4s-0-16-2"))
    .settings(commonSettings: _*)
    .settings(scalaPactPublishSettings: _*)
    .settings(
      name := "scalapact-http4s-0-16-2",
      libraryDependencies ++= Seq(
        "org.http4s" %% "http4s-blaze-server" % "0.16.2",
        "org.http4s" %% "http4s-blaze-client" % "0.16.2",
        "org.http4s" %% "http4s-dsl"          % "0.16.2",
        "com.github.tomakehurst" % "wiremock" % "1.56" % "test"
      ),
      crossScalaVersions := Seq(scala210, scala211, scala212),
      crossSbtVersions := Seq(sbt13, sbt1x),
      sbtVersion in Global := sbt1x,
      scalaCompilerBridgeSource := {
        val sv = appConfiguration.value.provider.id.version
        ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
      }
    )
    .dependsOn(shared)

//lazy val http4s0162_2_10 = http4s0162(scala210).dependsOn(shared)
//lazy val http4s0162_2_11 = http4s0162(scala211).dependsOn(shared)
//lazy val http4s0162_2_12 = http4s0162(scala212).dependsOn(shared)

lazy val http4s0170 =
  (project in file("scalapact-http4s-0-17-0"))
    .settings(commonSettings: _*)
    .settings(scalaPactPublishSettings: _*)
    .settings(
      name := "scalapact-http4s-0-17-0",
      libraryDependencies ++= Seq(
        "org.http4s" %% "http4s-blaze-server" % "0.17.0",
        "org.http4s" %% "http4s-blaze-client" % "0.17.0",
        "org.http4s" %% "http4s-dsl"          % "0.17.0",
        "com.github.tomakehurst" % "wiremock" % "1.56" % "test"
      ),
      crossScalaVersions := Seq(scala211, scala212),
      crossSbtVersions := Seq(sbt1x),
      sbtVersion in Global := sbt1x,
      scalaCompilerBridgeSource := {
        val sv = appConfiguration.value.provider.id.version
        ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
      }
    )
    .dependsOn(shared)

//lazy val http4s0170_2_10 = http4s0170(scala210).dependsOn(shared) // No such thing
//lazy val http4s0170_2_11 = http4s0170(scala211).dependsOn(shared)
//lazy val http4s0170_2_12 = http4s0170(scala212).dependsOn(shared)

lazy val argonaut62 =
  (project in file("scalapact-argonaut-6-2"))
    .settings(commonSettings: _*)
    .settings(scalaPactPublishSettings: _*)
    .settings(
      name := "scalapact-argonaut-6-2",
      libraryDependencies ++= Seq(
        "io.argonaut" %% "argonaut" % "6.2"
      ),
      crossScalaVersions := Seq(scala210, scala211, scala212),
      crossSbtVersions := Seq(sbt13, sbt1x),
      sbtVersion in Global := sbt1x,
      scalaCompilerBridgeSource := {
        val sv = appConfiguration.value.provider.id.version
        ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
      }
    )
    .dependsOn(shared)

//lazy val argonaut62_2_10 = argonaut62(scala210).dependsOn(shared)
//lazy val argonaut62_2_11 = argonaut62(scala211).dependsOn(shared)
//lazy val argonaut62_2_12 = argonaut62(scala212).dependsOn(shared)

lazy val argonaut61 =
  (project in file("scalapact-argonaut-6-1"))
    .settings(commonSettings: _*)
    .settings(scalaPactPublishSettings: _*)
    .settings(
      name := "scalapact-argonaut-6-1",
      libraryDependencies ++= Seq(
        "io.argonaut" %% "argonaut" % "6.1"
      ),
      crossScalaVersions := Seq(scala210, scala211),
      crossSbtVersions := Seq(sbt13),
      sbtVersion in Global := sbt1x,
      scalaCompilerBridgeSource := {
        val sv = appConfiguration.value.provider.id.version
        ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
      }
    )
    .dependsOn(shared)

//lazy val argonaut61_2_10 = argonaut61(scala210).dependsOn(shared)
//lazy val argonaut61_2_11 = argonaut61(scala211).dependsOn(shared)
//lazy val argonaut61_2_12 = argonaut61(scala212).dependsOn(shared) // No such thing

lazy val circe08 =
  (project in file("scalapact-circe-0-8"))
    .settings(commonSettings: _*)
    .settings(scalaPactPublishSettings: _*)
    .settings(
      name := "scalapact-circe-0-8",
      libraryDependencies ++= Seq(
        "io.circe" %% "circe-core",
        "io.circe" %% "circe-generic",
        "io.circe" %% "circe-parser"
      ).map(_ % "0.8.0"),
      crossScalaVersions := Seq(scala210, scala211, scala212),
      addCompilerPlugin(
        "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
      ),
      crossSbtVersions := Seq(sbt13, sbt1x),
      sbtVersion in Global := sbt1x,
      scalaCompilerBridgeSource := {
        val sv = appConfiguration.value.provider.id.version
        ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
      }
    )
    .dependsOn(shared)

//lazy val circe08_2_10 = circe08(scala210).dependsOn(shared).settings(
//  addCompilerPlugin(
//    "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
//  )
//)
//lazy val circe08_2_11 = circe08(scala211).dependsOn(shared)
//lazy val circe08_2_12 = circe08(scala212).dependsOn(shared)

lazy val pactSpec =
  (project in file("pact-spec-tests"))
    .settings(commonSettings: _*)
    .settings(
      name := "pact-spec-tests",
      crossScalaVersions := Seq(scala210, scala211, scala212),
    )
    .dependsOn(core, argonaut62)

//lazy val pactSpec_2_10 = pactSpec(scala210).dependsOn(core_2_10, argonaut62_2_10)
//lazy val pactSpec_2_11 = pactSpec(scala211).dependsOn(core_2_11, argonaut62_2_11)
//lazy val pactSpec_2_12 = pactSpec(scala212).dependsOn(core_2_12, argonaut62_2_12)

lazy val plugin =
  (project in file("scalapact-sbtplugin"))
    .settings(commonSettings: _*)
    .settings(scalaPactPublishSettings: _*)
    .dependsOn(core)
    .dependsOn(argonaut62 % "provided")
    .dependsOn(http4s0150a % "provided")
    .settings(
      sbtPlugin := true,
      name := "sbt-scalapact",
      crossSbtVersions := Seq(sbt13, sbt1x),
      sbtVersion in Global := sbt1x,
      scalaCompilerBridgeSource := {
        val sv = appConfiguration.value.provider.id.version
        ("org.scala-sbt" % "compiler-interface" % sv % "component").sources
      }
    )

lazy val framework =
  (project in file("scalapact-scalatest"))
    .settings(commonSettings: _*)
    .settings(scalaPactPublishSettings: _*)
    .settings(
      name := "scalapact-scalatest",
      libraryDependencies ++= Seq(
        "org.scalaj" %% "scalaj-http" % "2.3.0" % "test",
        "org.json4s" %% "json4s-native" % "3.5.0" % "test",
        "com.github.tomakehurst" % "wiremock" % "1.56" % "test",
        "fr.hmil" %% "roshttp" % "2.0.1" % "test"
      ),
      crossScalaVersions := Seq(scala211, scala212)
    )
    .dependsOn(core)
    .dependsOn(argonaut62 % "provided")
    .dependsOn(http4s0150a % "provided")

//lazy val framework_2_11 =
//  framework(scala211)
//    .dependsOn(core_2_11)
//    .dependsOn(argonaut62_2_11 % "provided")
//    .dependsOn(http4s0150a_2_11 % "provided")
//    .project
//lazy val framework_2_12 =
//  framework(scala212)
//    .dependsOn(core_2_12)
//    .dependsOn(argonaut62_2_12 % "provided")
//    .dependsOn(http4s0150a_2_12 % "provided")
//    .project

lazy val standalone =
  (project in file("scalapact-standalone-stubber"))
    .settings(commonSettings: _*)
    .dependsOn(core)
    .dependsOn(argonaut62)
    .dependsOn(http4s0150a)
    .settings(
      name := "scalapact-standalone-stubber",
      scalaVersion := scala212
    )

lazy val docs =
  (project in file("scalapact-docs"))
    .settings(commonSettings: _*)
    .enablePlugins(ParadoxPlugin)
    .settings(
      crossScalaVersions := Seq(scala212),
      paradoxTheme := Some(builtinParadoxTheme("generic")),
      name := "scalapact-docs",
      sourceDirectory in Paradox := sourceDirectory.value / "main" / "paradox",
      git.remoteRepo := "git@github.com:ITV/scala-pact.git"
    )

lazy val scalaPactProject =
  (project in file("."))
    .settings(commonSettings: _*)
    .aggregate(
      core,
      plugin,
      framework,
      standalone,
      docs,
      http4s0150a,
      http4s0162a,
      http4s0162,
      http4s0170,
      argonaut61,
      argonaut62,
      circe08,
      pactSpec
    )
