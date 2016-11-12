package mnist

import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Gen.{listOf, alphaStr, numChar}

object TestMnist extends
  Properties("mnist")
{
  property("actually works") =
    Prop.apply {
      MLPMnistSingleLayerExample.main(Array.empty[String]);
      true
    }
}
