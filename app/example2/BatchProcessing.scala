package example2

import akka.actor.{PoisonPill, ActorSystem, Props, Actor}
import akka.event.{LoggingReceive, Logging}
import akka.routing.{RoundRobinPool, RoundRobinGroup}

/**
 * Created by kops513 on 11/7/16.
 */



trait BatchItem{
val  id: Int
}
case object ProcessBatch
case class BatchItems(override val id: Int) extends BatchItem
case class ProcessItem( id: BatchItem)
case object ProcessedOneItem
case class ItemProcessingError(itemId: Int, message: String, error: Option[Throwable])

class ItemProcessingWorker extends Actor{
  val log = Logging(context.system, "application")

  def receive = LoggingReceive{
    case ProcessItem(item) =>
      try{
        process(item) match {
          case None => sender ! ProcessedOneItem
          case Some(error) => sender ! error
        }
      }catch{
        case t: Throwable =>
          sender ! ItemProcessingError(item.id,"unhandled error", Some(t))
      }
    case _ =>
        log.error("invalid item")
  }

  def process(item: BatchItem): Option[ItemProcessingError] = {
    log.info("processing item")
    if(item.id % 2 == 0 ) None else Some(ItemProcessingError(item.id,"odd number", Some(new NumberFormatException("exception"))))
  }

}

class BatchProcessActor(datasetId: Int) extends Actor{
   val log = Logging(context.system, "application")
  val workers = context.actorOf(RoundRobinPool(100).props(Props[ItemProcessingWorker]),"route")

  //total number of items fetched
  var totalItemCount = -1

  var currentBatchSize: Int = 0
  var currentProcessedItemsCount: Int = 0
  var currentProcessingErrors: List[ItemProcessingError] = List.empty

  var allProcessedItemsCount = 0
  var allProcessingErrors: List[ItemProcessingError] = List.empty

  def receive = LoggingReceive{

    case ProcessBatch=>
      if (totalItemCount == -1) {
        totalItemCount = totalItems
            log.info(s"Starting to process set with ID $datasetId, we have $totalItemCount items to go through")
      }
      val batch = fetchBatch
      processBatch(batch)

    case ProcessedOneItem =>
        log.info("one item processed")
      currentProcessedItemsCount = currentProcessedItemsCount + 1
      continueProcessing()

    case error @ ItemProcessingError(_, _, _) =>
       log.info("error found")

      currentProcessingErrors = error :: currentProcessingErrors
      continueProcessing()

  }

  def processBatch(batchItem: List[BatchItem]) = {
    if(batchItem.isEmpty){
        log.info(s"Done migrating all items for data set $datasetId .  $totalItems processed Items and error size is ${allProcessingErrors.size}")
      context.stop(self)
    } else {
    // reset processing state for the current batch
    currentBatchSize = batchItem.size
    allProcessedItemsCount = currentProcessedItemsCount + allProcessedItemsCount
    currentProcessedItemsCount = 0
    allProcessingErrors = currentProcessingErrors ::: allProcessingErrors
    currentProcessingErrors = List.empty

    //distribute the work
        log.info(s"sending item ${batchItem.head} to process")

      batchItem foreach { item =>
      workers !  ProcessItem(item)
    }

  }
  }
  def continueProcessing() = {

    val itemsProcessed = currentProcessedItemsCount + currentProcessingErrors.size

    if (itemsProcessed > 0 && itemsProcessed % 6 == 0) {
         log.info(s"Processed $itemsProcessed out of $currentBatchSize with ${currentProcessingErrors.size} errors")
    }

    if (itemsProcessed == currentBatchSize) {
      self ! ProcessBatch
    }

  }

  var totalItems: Int = 18
  var count = 0;

  def fetchBatch: List[BatchItem]  = {
    val items = (1 to 6).toList.map(i => i+count)
    if(count == 18)
      List.empty
    else {
      count = count + 6
      items.map { t => BatchItems(id = t)}
    }
  }

}
