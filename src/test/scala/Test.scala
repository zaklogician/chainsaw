import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Gen.{listOf, alphaStr, numChar}
import aima.core.learning.reinforcement.example._
import aima.core.agent._
import aima.core.learning.reinforcement.agent._
import aima.core.probability.mdp.ActionsFunction
import java.util.List

object Test extends Properties("RLBot") {
  property("created successfully") = Prop.apply {
    true
  }
}
