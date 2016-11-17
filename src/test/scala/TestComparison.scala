import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}
import Generators._

object TestDontCare extends Properties("DontCare") {
  property("compare always succeeds") = Prop.forAll(site,site) { (s,o) =>
    DontCare compare (s,o)
  }
}