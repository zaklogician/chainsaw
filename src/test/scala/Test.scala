import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Gen.{listOf, alphaStr, numChar}
import aima.core.learning.reinforcement.example._

object TestMnist extends Properties("aima") {
  property("main") = Prop.apply {
    aima.LearningDemo.main( Array.empty[String] )
    true
  }
}
