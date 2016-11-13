import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}

object Generators {

  def desc: Gen[Desc] = Gen.oneOf(EnemyWeaker, EnemyStronger, AllyWeaker, AllyStronger, DontCare)

  def pattern: Gen[Pattern] = for
    { i1 <- desc;             i3 <- desc
    ; i4 <- desc; i5 <- desc; i6 <- desc 
    ; i7 <- desc; i8 <- desc; i9 <- desc  
    } yield Pattern(i1,i3,i4,i5,i6,i7,i8,i9)
  
  def direction: Gen[Direction] = 
    Gen.oneOf(Direction.STILL, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST)
  
  def rule: Gen[Rule] = for { p <- pattern; d <- direction } yield Rule(p,d)

}