import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}

object TestDirectionAction extends Properties("DirectionAction") {
  val directions = 
    oneOf(Direction.STILL, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
  
  property("== is preserved") = Prop.forAll(directions, directions) { (d1,d2) =>
    (d1 == d2) ==> (DirectionAction(d1) == DirectionAction(d2))
  }
}
