import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}

object TestAgent extends Properties("Agent") {
  property("initialized") = Prop { 
    Agent != null
  }

  property("isAlive on init") = Prop {
    Agent.isAlive
  }

  property("execute outputs valid direction") = Prop.forAll { (r: Double) =>
//    println(Agent.execute(ConstReward(Unit, r)))
//    true
	  false
  }
}
