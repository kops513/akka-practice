println(fib(1, 2))

def fib(prevPrev: Int, prev: Int) {
  val next = prevPrev + prev
  println(next)
  if (next > 1000000) System.exit(0)
  fib(prev, next)
}



val items = List(1,2,3,3,2,1,6)
def isPalidrome(items: List[Int]): Boolean = {
  val length = items.length
  val mid  = if(length % 2 == 0) length / 2 else length/2 + 1
  val (k,c) = length %2  match {
    case 0 => (items.take( length/ 2), items.drop(length/ 2).reverse)
    case 1 => (items.take(length/2  ), items.drop(length/2 +1).reverse)

  }
  println(k,c)
  def matchList(t: List[Int], p: List[Int]): Boolean = {
    if(t.size <= 0) true
    else {
      if(t.head == p.head) matchList(t.tail,p.tail) else false
    }

  }
  matchList(k,c)
}

isPalidrome(items)
def flatten(t : List[_]): List[Any] = {
  t flatMap {
    case r: List[_] => flatten(r)
    case e => List(e)
  }
}

flatten(List(List(1, 1), 2, List(3, List(5, 8, List(4,5,6)))))
