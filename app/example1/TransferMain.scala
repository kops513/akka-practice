package example1

import akka.actor.{PoisonPill, Props, Actor}
import akka.event.LoggingReceive

/**
 * Created by kops513 on 6/22/16.
 * Transfer Actor  is for transferring money from one account to another. It wil wait for the amount to be withdrawen from one account
 * and deposit to the other and once its done , it stop
 */
class TransferMain extends Actor{

  val accountA = context.actorOf(Props[BankAccount], "accountA")
  val accountB = context.actorOf(Props[BankAccount], "accountB")

  accountA ! BankAccount.Deposit(100)

  def receive = LoggingReceive{
    case BankAccount.Done => transfer(50)
  }

  def transfer(amount: BigInt): Unit = {
    val transaction = context.actorOf(Props[WireTransfer], "transfer")
    transaction ! WireTransfer.Transfer(accountA, accountB, amount)
    context.become(LoggingReceive{
      case WireTransfer.Done =>
        accountA ! PoisonPill
        accountB ! PoisonPill
        context.stop(self)

    })
  }

}
