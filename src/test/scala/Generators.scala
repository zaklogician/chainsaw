import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}

object Generators {

  private val directions = Direction.values.toList

  def direction: Gen[Direction] = Gen.oneOf(directions)
  def comparison: Gen[Comparison] = Gen.oneOf(
    IsStrongerEnemy,
    IsStrongerAlly,
    IsWeakerEnemy,
    IsWeakerAlly,
    IsMoreProductive,
    IsLessProductive,
    DontCare
  ) 

  def pattern: Gen[Pattern] = for {
    cs <- Gen.listOf(comparison)
  } yield Pattern(cs)
  
  def rule: Gen[Rule] = for { p <- pattern; d <- direction } yield Rule(p,d)

  def individual: Gen[List[Rule]] = Gen.listOf(rule).suchThat( _.length > 1 )

  def site: Gen[Site] = for {
    gowner <- Gen.choose[Int](0,3)
    gstrength <- Gen.choose[Int](0,255)
    gproduction <- Gen.choose[Int](1,10)
  } yield new Site {
    owner = gowner
    strength = gstrength
    production = gproduction
  }
}