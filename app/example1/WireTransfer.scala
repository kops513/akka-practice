package example1

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive

/**
 * Created by kops513 on 6/22/16.
 */
class WireTransfer extends Actor{

  def receive = LoggingReceive{
    case WireTransfer.Transfer(from, to, amount) =>
      from ! BankAccount.WithDraw(amount)
      context.become(awaitWithDraw(to, amount, sender))

  }

  def awaitWithDraw(to: ActorRef, amount: BigInt, client: ActorRef): Receive = LoggingReceive{
    case BankAccount.Done =>
         to ! BankAccount.Deposit(amount)
         context.become(awaitDeposit(client))
    case BankAccount.Failed =>
         client ! WireTransfer.Failure
         context.stop(self)
  }

  def awaitDeposit(client: ActorRef): Receive = LoggingReceive{
    case BankAccount.Done =>
      client ! WireTransfer.Done
      context.stop(self)
  }

}


object WireTransfer {

  case class Transfer(from: ActorRef, to: ActorRef, balance: BigInt)
  case object Done
  case object Failure


}