import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}

object TestConstReward extends Properties("ConstReward") {
  property("apply(s,r) has reward r") = Prop.forAll { (s: Unit, r: Double) =>
     val sr = ConstReward(s,r)
     sr.reward == r 
  }
}
