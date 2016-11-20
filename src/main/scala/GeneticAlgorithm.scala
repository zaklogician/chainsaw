
trait GeneticAlgorithm {
  type Individual = List[Rule]
  
  val PopulationSize: Int
  val EliteRatio: Double

  val NumberOfRules: Int
  val RuleSize: Int
  val TournamentSize: Int
  val Iterations: Int
  val HaliteGridSize: Int
  val rng: RandomGen
  def winnerOf(i1: Individual, i2: Individual): Individual

  /////////////////////////////////

  protected val fitness_ = scala.collection.mutable.Map.empty[Individual,Int]

  def fitness(x: Individual): Int = fitness_.getOrElseUpdate(x,0)

  /////////////////////////////////

  def initial: List[Individual] = 
    for( _ <- ( 1 to PopulationSize ).toList ) yield 
    for( j <- ( 1 to NumberOfRules ).toList  ) yield 
    rng.nextRule(RuleSize)

  def crossover(a: Individual, b: Individual): Individual = {
    assert(a.length == b.length)
    val xpoint = rng.nextInt(a.length)
    val aPrefix = a.splitAt(xpoint)._1
    val bSuffix = b.splitAt(xpoint)._2
    aPrefix ++ bSuffix
  }

  def mutate(a: Individual): Individual = {
    val mpoint = rng.nextInt(a.length)
    a.updated(mpoint, rng.nextRule(RuleSize))
  }

  def tournamentSelect(population: List[Individual]): Individual = {
    // println("  Starting tournament")
    val tournament = population.sortBy { -fitness(_) }.take(TournamentSize)
    tournament.reduce { (champion,contender) =>
       winnerOf(champion,contender)
    }
  }

  /////////////////////////////////

  def performIteration(population: List[Individual]): List[Individual] = {

	// "Essentials of Metaheuristics": Algorithm 33 -- The Genetic Algorithm with Elitism

	val NumElite = ( population.length * EliteRatio ).toInt

	val parentSurvivors = population.sortBy { -fitness(_) }.take( NumElite )

	val NumChildIter = ( population.length - NumElite ) / 2
    val progress = new ProgressDisplay( NumChildIter )
    val children = ( 1 to NumChildIter ).toList flatMap { i =>
      val parent1 = tournamentSelect(population)
      val parent2 = tournamentSelect(population)
	  progress.increment()
      List( mutate(crossover(parent1,parent2)), mutate(crossover(parent2,parent1)) )
    }

	parentSurvivors ++ children
  } ensuring { _.length == population.length }

  /////////////////////////////////

  def mean(xs: Seq[Int]): Double = {
	require( !xs.isEmpty ) 
	xs.sum / xs.length
  }

  def variance(xs: Seq[Int]): Double = {
	require( !xs.isEmpty ) 
	val d = xs.sum - mean(xs)
	( d * d ) / (xs.length - 1)
  }

  def median(x: Seq[Int]): Double = {
	val xs = x.sorted
	val mid = xs.length/2
	if( xs.length % 2 == 0 )
	  (xs(mid) + xs(mid-1))/2.0
	else
      xs(mid)
  }

  /////////////////////////////////

  // main GA loop
  def galoop: Map[Individual,Int] = {
    println("Starting GA loop")
    var population: List[Individual] = initial
    var iter = 0;
    while(iter < Iterations) {
      println( s"Iteration: ${iter+1} of $Iterations" )
      population = performIteration(population)

	  val fv = fitness_.values.toList
	  println( s"fitness mean: ${mean(fv)}, var: ${variance(fv)}, median: ${median(fv)}, min: ${fv.min}, max: ${fv.max}" )

      iter = iter + 1
    }
    // fitness_.toList.maxBy { _._2 }
	fitness_.toMap
  }
}

///////////////////////////////////

object Main extends GeneticAlgorithm {

  override val PopulationSize = 10
  override val EliteRatio = 0.4

  override val NumberOfRules = 10
  override val RuleSize = 1
  override val TournamentSize = 4
  override val Iterations = 10
  override val HaliteGridSize = 10
  override val rng: RandomGen = new JavaRandomGen

  /////////////////////////////////

  override def winnerOf(i1: Individual, i2: Individual): Individual = {
    val file1: String = "i1.bot"
    val file2: String = "i2.bot"
    
    Serialize(new java.io.FileOutputStream("games/"+file1), i1)
    Serialize(new java.io.FileOutputStream("games/"+file2), i2)
    
    val cmd = 
      s"""./halite -d "$HaliteGridSize $HaliteGridSize" "scala RLBot $file1" "scala RLBot $file2" """
    val testProgOutput = sys.process.Process(cmd, new java.io.File("./games")).lines.toList
    val result = if( testProgOutput.contains("Player #1, ScalaBot, came in rank #1!") ) i1 else i2
    fitness_.update(result, fitness(result) + 1)
    result
  }

  /////////////////////////////////

  def main(args: Array[String]): Unit = {
    val startTime = System.currentTimeMillis()
    val finalPopulation = galoop
    val endTime = System.currentTimeMillis()

	// most recent .hlt file is between best two individuals
	val sortedPopulation = finalPopulation.toList.sortBy { - _._2 }
	winnerOf(sortedPopulation.head._1, sortedPopulation.tail.head._1)
	val best = sortedPopulation.head
    println(best)
    println( s"elapsed: ${(endTime-startTime)/1000.0}" )
  }
}

// End ///////////////////////////////////////////////////////////////