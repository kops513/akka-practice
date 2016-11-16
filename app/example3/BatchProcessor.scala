package example3

import akka.actor.{Kill, PoisonPill, Props, Actor}
import akka.event.{LoggingReceive, Logging}
import akka.routing.RoundRobinPool
import example3.BatchProcessor.{ Process, GetLoad}
import example3.DataProducer.Data
import example3.WorkerActor.{ProcessItem, BatchItem, ItemProcessingError, ProcessedOneItem}
import akka.util.Timeout
import scala.concurrent.duration.DurationLong

/**
 * Created by kops513 on 11/14/16.
 */
class BatchProcessor extends Actor{

  var currentItemCount = 0
  var totalItemCount = -1
  var totalErrors: List[ItemProcessingError] = List.empty
  val log = Logging(context.system, "application")
  var dataProducerCount = 0
  val workers = context.actorOf(RoundRobinPool(100).props(Props[WorkerActor]),"route")
  val producer = context.actorOf(Props[DataProducer],"producer")
  var allProcessedItemsCount = 0
  var allProcessingErrors: List[ItemProcessingError] = List.empty

  def receive = LoggingReceive{
    case Process =>
       if(totalItemCount == -1)
       {
         totalItemCount = totalItems
         log.info(s"Starting to process for totalItems = $totalItemCount")
       }
       val index = allProcessedItemsCount + allProcessingErrors.size
       log.debug("current index = "+ index )
       log.debug("current Item Count ="+currentItemCount)
      if(currentItemCount < DataProducer.MAX_LOAD) {
        producer ! DataProducer.FetchData(dataProducerCount)
        dataProducerCount = dataProducerCount + DataProducer.batchSize
      }
    case GetLoad =>
          sender ! currentItemCount
    case ProcessedOneItem =>
      currentItemCount = currentItemCount - 1
      allProcessedItemsCount = allProcessedItemsCount + 1
      continueBatchProcessing()
    case e:ItemProcessingError =>
      allProcessingErrors = e :: allProcessingErrors
      currentItemCount = currentItemCount - 1
      continueBatchProcessing()
    case Data(items) =>
       processBatch(items)

  }

  def processBatch(items: List[BatchItem]) = {
    if(items.isEmpty){
      log.info(s"Done migrating all items, totalItems = $totalItems, errors = ${allProcessingErrors.size}")
       context.stop(self)
    }else{
      items foreach{ item =>
      workers ! ProcessItem(item)
      currentItemCount = currentItemCount + 1
      }
    }
  }

  def continueBatchProcessing() = {
    val totalProcessedItems = allProcessedItemsCount + allProcessingErrors.size
    if(totalProcessedItems > 0 && totalProcessedItems % DataProducer.batchSize == 0){
      log.info("Batch Processing completed")
    }
       self ! Process
  }

  def totalItems = 50

}

object BatchProcessor {
  case object GetLoad
  case object Process
}
