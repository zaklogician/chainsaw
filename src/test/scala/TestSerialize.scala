import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}
import Generators._

object TestSerialize extends Properties("Serialize") {
  property("rules") = Prop.forAll(rule) { original =>
	val baos = new java.io.ByteArrayOutputStream();

	val oos = new java.io.ObjectOutputStream( baos )
	oos.writeObject( original )
	oos.close()
        
	val ois = new java.io.ObjectInputStream( new java.io.ByteArrayInputStream(baos.toByteArray) )
	val clone = ois.readObject().asInstanceOf[Rule]
	ois.close()
	original == clone
  }

  property("individuals") = Prop.forAll(listOf(rule)) { original =>
	val baos = new java.io.ByteArrayOutputStream();

	val oos = new java.io.ObjectOutputStream( baos )
	oos.writeObject( original.toArray )
	oos.close()
        
	val ois = new java.io.ObjectInputStream( new java.io.ByteArrayInputStream(baos.toByteArray) )
	val clone = ois.readObject().asInstanceOf[Array[Rule]]
	ois.close()
	original.toList == clone.toList
  }

}
