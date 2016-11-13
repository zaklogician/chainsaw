import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}

object TestSerializeRule extends Properties("Rule") {
  property("serialize") = Prop { 
	val fname = "test-serialize-1.tmp"
	val rule = Genetic.newRule

	val oos = new java.io.ObjectOutputStream( new java.io.FileOutputStream(fname) )
	oos.writeObject( rule )
	oos.close()

	val ois = new java.io.ObjectInputStream( new java.io.FileInputStream(fname) )
	val rule2 = ois.readObject().asInstanceOf[Rule]
	ois.close()
	rule == rule2
  }

  property("serialize for arrays") = Prop { 
	val fname = "test-serialize-2.tmp"
	val rule1 = Genetic.newRule
	val rule2 = Genetic.newRule
	val rules: Array[Rule] = Array(rule1,rule2)

	val oos = new java.io.ObjectOutputStream( new java.io.FileOutputStream(fname) )
	oos.writeObject( rules )
	oos.close()

	val ois = new java.io.ObjectInputStream( new java.io.FileInputStream(fname) )
	val rules2 = ois.readObject().asInstanceOf[Array[Rule]]
	ois.close()

	rules.toList == rules2.toList
  }

}
