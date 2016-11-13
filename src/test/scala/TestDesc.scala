import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}
import Generators.desc


object TestDesc extends Properties("Desc") {

  property("matches reflexive") = Prop.forAll(desc) { d =>
    d matches d
  }
  
}

object TestDontCare extends Properties("DontCare") {

  property("matches everything") = Prop.forAll(desc) { d =>
    DontCare matches d
  }
  
}
