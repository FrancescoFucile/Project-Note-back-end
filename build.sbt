name := "project_note_back-end"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.6"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.6"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.10"
libraryDependencies += "com.rabbitmq" % "amqp-client" % "5.0.0"

enablePlugins(JavaServerAppPackaging)

/*herokuAppName in Compile := "ProjectNote"

mainClass in Compile := Some("NoteService.scala")*/