import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}
import Generators.individual


object TestGeneticAlgorithm extends Properties("GeneticAlgorithm") {
  
  object MockGenetic extends GeneticAlgorithm {
      override val PopulationSize = 5
      override val NumberOfRules = 5
      override val RuleSize = 2
      override val TournamentSize = 4
      override val Iterations = 10
      override val HaliteGridSize = 10
      override val rng: RandomGen = new JavaRandomGen

      override def winnerOf(i1: Individual, i2: Individual): Individual = i1
  }

  property("crossover idempotent") = Prop.forAll(individual) { i =>
    MockGenetic.crossover(i,i) == i
  }

  property("mutate preserves length") = Prop.forAll(individual) { i =>
    MockGenetic.mutate(i).length == i.length
  }

}
