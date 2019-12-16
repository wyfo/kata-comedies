name := "kata-mystere"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.1"
libraryDependencies += "com.rabbitmq" % "amqp-client" % "5.8.0"
libraryDependencies += "com.lightbend.akka" %% "akka-stream-alpakka-amqp" % "1.1.2"

libraryDependencies += "org.scalatest" % "scalatest_2.13" % "3.1.0" % "test"
libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % "2.6.1" % Test