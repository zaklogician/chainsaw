import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}

object TestSerializeRule extends Properties("Rule") {
  property("Tests serialize") = Prop { 
	val fname = "tmp.txt"
	val rule = new Genetic( new java.util.Random() ).newRule

	val oos = new java.io.ObjectOutputStream( new java.io.FileOutputStream(fname) )
	oos.writeObject( rule )
	oos.close()

	val ois = new java.io.ObjectInputStream( new java.io.FileInputStream(fname) )
	val rule2 = ois.readObject().asInstanceOf[Rule]
	ois.close()

	rule == rule2
  }
}
