import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}

object TestAgent extends Properties("") {
  property("Tests work") = Prop { 
    true
  }
}
