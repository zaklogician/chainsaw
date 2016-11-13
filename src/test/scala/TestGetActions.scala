import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}

object TestGetActions extends Properties("GetActions") {
  val directions = 
    oneOf(Direction.STILL, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
  
  property("actions(Unit) returns every action") = Prop.forAll(directions) { d =>
     import scala.collection.JavaConversions._
     GetActions.actions(Unit).contains(DirectionAction(d))
  }
}
