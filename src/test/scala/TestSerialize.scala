import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}
import Generators._

object TestSerialize extends Properties("Serialize") {
  property("rules") = Prop.forAll(rule) { original =>
	val baos = new java.io.ByteArrayOutputStream();
	Serialize(baos, original)

        val bais = new java.io.ByteArrayInputStream(baos.toByteArray)
	val clone = Deserialize[Rule](bais)

	original == clone
  }

  property("individuals") = Prop.forAll(listOf(rule)) { original =>
	val baos = new java.io.ByteArrayOutputStream();

	Serialize(baos, original)
        
        val bais = new java.io.ByteArrayInputStream(baos.toByteArray)
	val clone = Deserialize[List[Rule]](bais)

	original == clone
  }

}
