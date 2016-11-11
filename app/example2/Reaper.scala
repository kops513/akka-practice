package example2

import akka.actor.{Terminated, Actor, ActorRef}

import scala.collection.mutable.ArrayBuffer

/**
 * Created by kops513 on 11/10/16.
 */
object Reaper {
  case class WatchMe(ref: ActorRef)

}

abstract class Reaper extends Actor{
  import Reaper._

  //keep track of what we are watching
  val watched = ArrayBuffer.empty[ActorRef]

  //Derivation need to implement this method. Its the hook
  // that is called when everything is dead
  def allSoulsReaped(): Unit

  //watch and check for Termination
  def receive = {
    case WatchMe(ref) =>
      context.watch(ref)
      watched += ref
    case Terminated(ref) =>
      watched -= ref
      if (watched.isEmpty) allSoulsReaped()
  }
}


class BatchReaper extends Reaper{
  //Derivation need to implement this method. Its the hook
  override def allSoulsReaped(): Unit = {
    context.system.terminate()
  }
}