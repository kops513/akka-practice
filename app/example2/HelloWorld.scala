package example2

import akka.actor.{Props, ActorSystem}
import example2.Reaper.WatchMe

/**
 * Created by kops513 on 11/8/16.
 */
object HelloWorld extends App {
  println("say hello to the world")
  val system = ActorSystem("main-actor")
  val batchProcessActor = system.actorOf(Props(new BatchProcessActor(1)), "batch-process-actor")
  val c = batchProcessActor ! ProcessBatch

  val reaper = system.actorOf(Props[BatchReaper], "reaper")
  reaper ! WatchMe(batchProcessActor)
}
