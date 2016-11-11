import java.io.Writer
 trait Animal

case class Dog (name: String) extends Animal

case class Cat (name: String) extends Animal

class Types{
  def checkWhichAnimal[T<: Animal](a: T){
    if(a.isInstanceOf[Dog]) println("dog")
    else println("cat")
  }
}

val check = new Types
check.checkWhichAnimal(new Dog("dog"))
trait Log {
  def warning(message: String)
}

class Logger {
  def log(types: String, message: String): Unit ={
    println(message)
  }
}

implicit class LoggerAdapter(logger: Logger) extends Log{
  def warning(message: String) = logger.log("warning", message)
}

val log: Log = new Logger
log.warning("kops")
lazy val fs: Stream[Int] = 0 #:: fs.scanLeft(1)(_ + _)

val r = Range(20, Int.MaxValue)



