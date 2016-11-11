val a = Array(1,3,5,7,2,4,6,8)
val mid: Int =   (a.size  % 2)  match{
  case 0 => a.size / 2 -1
  case _ => a.size / 2
}
val b = (0 to mid).toList.map { i =>
  val high = i + mid + 1
  if(high <= a.size - 1)
   List(a(i), a(high))
    else
    List(a(i))
}.flatten

val l = List(1,3,5,7,2,4,6,8)

def divide(l1: List[Int], l2: List[Int]): List[Int] = {
  (l1, l2) match {
    case (h1 :: Nil, h2 :: Nil) => h1 :: List(h2)
    case _ =>
      val split1 = l1.splitAt(l1.size / 2)
      val split2 = l2.splitAt(l2.size / 2)
      divide(split1._1, split2._1) ::: divide(split1._2, split2._2)

  }
}

divide(List(1,3,5,7), List(2,4,6,8))

//max diff between two array

//sort 0 1 2
var array = Array(0,1,2,0,1,2,0,1,2,0,0)
var low = 0
var middle = 0
var high = array.size - 1
while(middle <= high) {
  array(middle) match {
    case 0 =>
      swap(array(low), array(middle))
      low= low + 1
      middle = middle + 1
    case 1 => middle = middle + 1
    case 2 =>
      swap(array(middle), array(high))
      high= high - 1


  }
}

def swap(a: Int, b: Int): Array