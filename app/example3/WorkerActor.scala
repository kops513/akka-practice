package example3

import akka.actor.Actor
import akka.event.LoggingReceive

/**
 * Created by kops513 on 11/8/16.
 */
class WorkerActor extends Actor{
  def receive = LoggingReceive{
    case WorkerActor.ProcessItem(item) =>
      try{
        process(item) match {
          case None => sender ! WorkerActor.ProcessedOneItem
          case Some(t) => sender ! t
        }

      }catch {
        case t: Throwable =>
          sender ! WorkerActor.ItemProcessingError(item.id,t.toString,Some(t))
      }
  }
  def process(item: WorkerActor.Item): Option[WorkerActor.ItemProcessingError] = {
    if(item.id % 2 == 0)
      None
    else
      Some(WorkerActor.ItemProcessingError(item.id,"", Some(new NumberFormatException(s"even number ${item.id} found"))))

  }
}

object WorkerActor{
  case object Process
  trait Item{
    val id: Int
  }
  case class BatchItem(override val id: Int) extends Item

  case class ProcessItem(item: Item)
  case class ItemProcessingError(itemId: Int, message: String, error: Option[Throwable])
  case object ProcessedOneItem

}

