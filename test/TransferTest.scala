import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import example1.{WireTransfer, BankAccount, TransferMain}
import org.scalatest.WordSpecLike
import org.scalatest.Matchers
/**
 * Created by kops513 on 11/10/16.
 */
class TransferTest extends TestKit(ActorSystem("transfer-system")) with ImplicitSender
with WordSpecLike with Matchers{

  val accountA = TestActorRef[BankAccount]
  val accountB = TestActorRef[BankAccount]
  val transaction = TestActorRef[WireTransfer]
  val amount = 100

  "Account A" should {
   " contain an amount 100 " in {
      accountA ! BankAccount.Deposit(amount)
      expectMsg(BankAccount.Done)
   }

    "transfer amount 100 to account B" in {
      transaction ! WireTransfer.Transfer(accountA, accountB, amount)
      expectMsg(WireTransfer.Done )
    }
   }

  "Account B" should {
    "withdraw amount 100" in {
      accountB ! BankAccount.WithDraw(amount)
      expectMsg(BankAccount.Done)
    }

    "send error msg while withdrawing amount 100" in {
      accountB ! BankAccount.WithDraw(amount)
      expectMsg(BankAccount.Failed(BankAccount.withDrawAmountFailedMsg))
    }
  }

  system.terminate()

}
