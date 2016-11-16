package example3

import akka.actor.Actor
import akka.event.LoggingReceive
import akka.util.Timeout
import example3.BatchProcessor.GetLoad
import example3.DataProducer.{Data, FetchData}
import example3.WorkerActor.{BatchItem, Item}
import akka.pattern.ask

import scala.concurrent.Await
import scala.concurrent.duration.DurationLong

/**
 * Created by kops513 on 11/8/16.
 */
class DataProducer extends Actor {
  def receive = LoggingReceive{
    case FetchData(currentIndex) =>
      throttleDown
      sender ! fetchBatch(currentIndex)


  def throttleDown(): Unit = {
    implicit val timeout = Timeout(1.seconds)}
    val eventuallyLoad = context.parent ? GetLoad
    try {
      val load = Await.result(eventuallyLoad, 5.seconds)

      if (load.asInstanceOf[Int] > DataProducer.MAX_LOAD) {
        Thread.sleep(5000)
        throttleDown
      }

    } catch {
      case t: Throwable =>
        // we most likely have timed out - wait a bit longer
        throttleDown
    }
  }

  val data = (1 to 50).toList
  def fetchBatch(currentIndex: Int): Data = {
    if(currentIndex+ 10 > data.size){
      context.stop(self)
      Data(List.empty)
    }else {
      val currentData = data.slice(currentIndex, currentIndex + 10)
      val batchItems = currentData.map(t => BatchItem(t))
      Data(batchItems)
    }
  }

}

object DataProducer {
  case class FetchData(currentIndex: Int)
  case class  Data(data: List[BatchItem])

  val MAX_LOAD = 5
  val batchSize = 10


}