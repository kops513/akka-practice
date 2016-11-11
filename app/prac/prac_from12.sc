
def decode(items : List[(Int,Int)]) = {
  for{
    j <- items
    k <- 1 to j._1
  }yield(j._2)
}

decode(List((4, 1), (1, 2), (2, 3), (4, 7)))

def duplicate(items: List[Int]): List[Int] = {
  items.flatMap(t => List(t,t))
}

duplicate(List(1,2,3,4))

def range(start: Int, end: Int): List[Int] = {
  val a = List(1,2,23,4,5)
  for(i <- a)yield (i)
}

def factorialOuter(n: Int): Int = {


  def factorial(n: Int, result: Int): Int = {
    if (n <= 1) result
    else factorial(n - 1, result * n)

  }
  factorial(n,1)
}

factorialOuter(5)

val array = Array(1,2,3,4,5)

val k: Int = array(3)


def rotate(n: Int, items: List[Int]) = {
 val nth = n match {
   case t if t < 0 => n + items.size
   case _ => n
 }
  items.drop(nth) ::: items.take(nth)
}
rotate(3, List(1,2,3,4,5))


val a = IndexedSeq(1,2,4)
