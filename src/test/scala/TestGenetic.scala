import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}
import Generators.individual


object TestGenetic extends Properties("Genetic") {

  property("crossover idempotent") = Prop.forAll(individual) { i =>
    Genetic.crossover(i,i) == i
  }

  property("mutate preserves length") = Prop.forAll(individual) { i =>
    Genetic.mutate(i).length == i.length
  }

}
