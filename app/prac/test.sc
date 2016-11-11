def maxList(items: List[Int],t: Int): Int = {
items match {
  case Nil => t
  case headVal :: tailVal =>
    if(headVal > t) maxList(tailVal, headVal)
    else maxList(tailVal,t)
}
}

val list: List[Int] = List(-9,-10)
maxList(list.tail, list.head)

def isPrime(items: Int): Boolean = {
    var isItemPrime: Boolean = true
    for(i <- 2 to items-1 ){
      if(items % i == 0) isItemPrime = false

  }
      isItemPrime
}

isPrime(15)



def lastElement(items: List[Int],temp: Int): Int = {
    items match {
      case Nil => temp
      case headVal :: tail => lastElement(tail,headVal)
    }
}

lastElement(List(1,2,3),0)


def secondLastElement[A](items: List[A], temp: A): A = {
  items match {
    case h :: Nil => temp
    case h :: tail => secondLastElement(tail,h)
    case _ => throw new NoSuchElementException
  }
}

secondLastElement(List(1,2,3,4,5),0)

def nthElement(items: List[Int], n: Int,index: Int): Int = {
  items match {
    case h :: _ if(n-1 == index) => h
    case _ :: tail => nthElement(tail,n, index+1)
    case _ => throw new IndexOutOfBoundsException
  }



}

nthElement(List(1,2,3,4,5), 4, 0)

val list1 = List(1,2,3,4,5)

list1.size


