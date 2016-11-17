
trait RandomGen {
  def nextInt: Int
  def nextInt(bound: Int): Int
  def nextDirection: Direction
  def nextComparison: Comparison
  def nextPattern(size: Int): Pattern
  def nextRule(size: Int): Rule
}

class JavaRandomGen extends java.util.Random with RandomGen {
  private val directions: Array[Direction] = Direction.values
  private val comparisons: Array[Comparison] =
  Array(
    IsStrongerEnemy,
    IsStrongerAlly,
    IsWeakerEnemy,
    IsWeakerAlly,
    IsMoreProductive,
    IsLessProductive,
    DontCare
  )
  
  override def nextDirection: Direction = {
    directions(nextInt(directions.length))
  }

  override def nextComparison: Comparison = {
    comparisons(nextInt(comparisons.length))
  }

  override def nextPattern(size: Int): Pattern = Pattern {
    for (x <- (1 to size).toList) yield nextComparison
  }

  override def nextRule(size: Int): Rule = {
    val pattern = nextPattern(size)
    val direction = nextDirection
    Rule(pattern,direction)
  }
}