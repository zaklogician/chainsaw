import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}

object TestGetActionsUnit extends Properties("GetActions") {
  val directions = 
    oneOf(Direction.STILL, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
  
  property("actions returns every action") = Prop.forAll(directions) { d =>
     import scala.collection.JavaConversions._
     GetActions[Unit]().actions(Unit).contains(DirectionAction(d))
  }
  
}
