
lazy val root = (project in file(".")).
  settings(
    name := "chainsaw",
    version := "0.1.0",
    scalaVersion := "2.10.4",
    mainClass in Compile := Some("Main")
  )

// assemblyOutputPath in assembly := file("./deploy/chainsaw" + ".jar")

// Because I'm _just_ mean
// scalacOptions += "-deprecation"

// for https://github.com/scopt/scopt
resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  //groupID % artifactID % revision
  // "org.scalacheck" % "scalacheck_2.10" % "1.13.4" % "test"
  "com.medallia.word2vec" % "Word2VecJava" % "0.10.3"
)