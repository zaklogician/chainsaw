import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}
import Generators.pattern


object TestPattern extends Properties("Pattern") {

  property("matches reflexive") = Prop.forAll(pattern) { d =>
    d matches d
  }

  property("matches DontCare matches everything") = Prop.forAll(pattern) { d =>
    Pattern(DontCare,DontCare,DontCare,DontCare,DontCare,DontCare,DontCare,DontCare) matches d
  }
  
}
