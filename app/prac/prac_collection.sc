def binarySearch1(items: Array[Int],searchItem: Int): Int = {
  def findVal(high: Int, low: Int ): Int = {
    val mid = (high + low) / 2
    if(mid >= items.length) {
      -1
    }
    else {
      searchItem match {
        case t if (t == items(mid))  => mid
        case t if (t < items(mid)) => findVal(mid-1, low)
        case _ => findVal(high, mid+1)
      }

    }
  }

  findVal(items.length, 0)
}
binarySearch1(Array(1,2,3,4,5),5)
def binarySearch(item: Int, array: Array[Int]) = {
  val arraySize = array.length
  val highVal = arraySize - 1
  val lowVal = 0
  if(item > array(highVal) || item < array(lowVal)) throw new Exception
  def findVal(high: Int, low: Int, item: Int): Int = {
    val mid: Int = (high + low)/2
    println(array(mid))
    if(item  == array(mid) ) array(mid)
    else if (item > mid ) findVal(high,mid+1,item)
    else  findVal(mid-1,low, item)
  }
  findVal(highVal,lowVal,item)

}

binarySearch(1, Array(1,2,3,4,5,6,7,8))
def quickSort(items: Array[Int]): Array[Int] = {
  if(items.length <= 1 )
    items
  else{
    val pivot = items.length / 2
    val pivotVal = items(pivot)
    Array.concat(quickSort(items.filter( _ < pivotVal)),items.filter( _ == pivotVal), quickSort(items.filter( _ > pivotVal)))

  }
}

quickSort(Array(4,5,2,3,4,7,8,9,5,4))

val currentTime1 = System.currentTimeMillis()
def bubbleSort( items: List[Int]): List[Int] = {

  def sort(tempItems: List[Int]): List[Int] = {
    tempItems match {
      case Nil => Nil
      case xs :: Nil => List(xs)
      case xs :: xy :: xt =>
        if (xs > xy)
          xy :: sort(xs :: xt)
        else
          xs :: sort(xy :: xt)
    }
  }
  val itemLength = items.length
  if(itemLength <= 1)
    items
  else {
   val o = sort(items)
   val n: List[Int] =  ( bubbleSort(o.take(itemLength - 1)) ::: List(o.last))
   n
  }
}

bubbleSort(List(3,4,5,7,1,2,3,8))
val currentTime2 = System.currentTimeMillis()
val t = currentTime2 - currentTime1
println("buubleSort1 = "+t)
val currentTime3 = System.currentTimeMillis()
def sortRecursive(input: List[Int]): List[Int] = {
  if (input != Nil && input.tail != Nil) {
    if (input.head > input.tail.head) {
      sortRecursive(List(input.tail.head, input.head) ::: input.tail.tail)
    } else {
      val sortResult = sortRecursive(input.tail)
      if (input.head > sortResult.head) sortRecursive(List(sortResult.head, input.head) ::: sortResult.tail) else List(input.head) ::: sortResult
    }
  } else {
    input
  }
}
sortRecursive(List(3,4,5,7,1,2,3,8))
val currentTime4  = System.currentTimeMillis()
val t1 = currentTime4 - currentTime3
println("buubleSort2 = "+t1)

def mergeSort(items: List[Int]): List[Int]= {
  val length = items.size
  if(length <= 1) items
  else {
    def merge(left: List[Int], right: List[Int]): List[Int] = {
      (left, right) match {
        case (left, Nil) => left
        case (Nil, right) => right
        case (lh :: lt, rh :: rt) =>
          if(lh > rh)
            rh :: merge(left,rt)
          else
            lh :: merge(lt, right)
      }
    }
    val (left, right) = items.splitAt(length/2)
    merge(mergeSort(left), mergeSort(right))
  }
}
mergeSort(List(3,6,1,2,8,7,8,5))
println(fib(1, 2))
def fib(prevPrev: Int, prev: Int) {
  val next = prevPrev + prev
  println(next)
  if (next > 1000000) System.exit(0)
  fib(prev, next)
}