
lazy val root = (project in file(".")).
  settings(
    name := "chainsaw",
    version := "0.1.0",
//    scalaVersion := "2.11.8",//"2.10.4",
    scalaVersion := "2.10.4",
    mainClass in Compile := Some("dpt.DPT")
  )

// assemblyOutputPath in assembly := file("./deploy/chainsaw" + ".jar")

// Because I'm _just_ mean
// scalacOptions += "-deprecation"

// for https://github.com/scopt/scopt
resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  //groupID % artifactID % revision
  "org.deeplearning4j" % "deeplearning4j-core" % "0.6.0",
  "org.nd4j" % "nd4j-native-platform" % "0.6.0",
  "org.deeplearning4j" % "rl4j-api" % "0.6.0",

  "org.scalacheck" % "scalacheck_2.10" % "1.13.4" % "test",
  "org.slf4j" % "slf4j-api" % "1.7.21" % "test",
  "org.slf4j" % "slf4j-simple" % "1.7.21" % "test"
// https://deeplearning4j.org/quickstart#examples
// datavec-api
)


