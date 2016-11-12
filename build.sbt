
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
  "org.deeplearning4j" % "deeplearning4j-core" % "0.6.0"
)


