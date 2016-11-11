package example1

import akka.actor.Actor
import akka.event.LoggingReceive

/**
 * Created by kops513 on 6/22/16.
 * A BankAccount is an actor that deposit or withdraw the amount from an individual
 */
class BankAccount  extends Actor{

  var balance = BigInt(0)

  def receive = LoggingReceive{
    case BankAccount.Deposit(amount) =>
      balance += amount
      sender ! BankAccount.Done
    case BankAccount.WithDraw(amount) if amount <= balance =>
      balance -= amount
      sender ! BankAccount.Done
    case BankAccount.WithDraw(amount) if amount > balance =>
      sender ! BankAccount.Failed(BankAccount.withDrawAmountFailedMsg)
    case _ =>
      sender ! BankAccount.Failed(BankAccount.failedMsg)
  }
}

object BankAccount {
  case class Deposit(amount: BigInt){
    require(amount > 0)
  }

  case class WithDraw(amount: BigInt){
    require(amount > 0)
  }

  case object Done

  case class Failed(msg: String)

  val withDrawAmountFailedMsg = "withdraw amount should be less than account balance"
  val failedMsg = "Bank Account can only deposit or withdraw amount"

}