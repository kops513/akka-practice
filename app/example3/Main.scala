package example3

import akka.actor.{Props, ActorSystem}
import example2.BatchReaper
import example2.Reaper.WatchMe
import example3.BatchProcessor.Process

/**
 * Created by kops513 on 11/15/16.
 */
object Main extends App{
  val system = ActorSystem("main-actor")
  val batchProcessorActor = system.actorOf(Props[BatchProcessor], "batchProcessor-actor")
  batchProcessorActor ! Process
  val reaper = system.actorOf(Props[BatchReaper], "reaper")
  reaper ! WatchMe(batchProcessorActor)
}
