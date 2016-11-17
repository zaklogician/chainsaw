object Main extends GeneticAlgorithm {
  override val PopulationSize = 10
  override val NumberOfRules = 2
  override val RuleSize = 1
  override val TournamentSize = 4
  override val Iterations = 10
  override val HaliteGridSize = 10
  override val rng: RandomGen = new JavaRandomGen

  override def winnerOf(i1: Individual, i2: Individual): Individual = {
    val file1: String = "i1.bot"
    val file2: String = "i2.bot"
    
    Serialize(new java.io.FileOutputStream("games/"+file1), i1)
    Serialize(new java.io.FileOutputStream("games/"+file2), i2)
    
    val cmd = 
      s"""./halite -d "$HaliteGridSize $HaliteGridSize" "scala RLBot $file1" "scala RLBot $file2" """
    val testProgOutput = sys.process.Process(cmd, new java.io.File("./games")).lines.toList
    val result = if( testProgOutput.contains("Player #1, ScalaBot, came in rank #1!") ) i1 else i2
    fitness.update(result, fitness.getOrElse(result, 0) + 1)
    result
  }

  def main(args: Array[String]): Unit = {
    val startTime = System.currentTimeMillis()
    val best = galoop
    val endTime = System.currentTimeMillis()
    println(best)
    println( s"elapsed: ${(endTime-startTime)/1000.0}" )
  }
}

trait GeneticAlgorithm {
  type Individual = List[Rule]
  
  val PopulationSize: Int
  val NumberOfRules: Int
  val RuleSize: Int
  val TournamentSize: Int
  val Iterations: Int
  val HaliteGridSize: Int
  val rng: RandomGen
  def winnerOf(i1: Individual, i2: Individual): Individual

  /////////////////////////////////

  val fitness = scala.collection.mutable.Map.empty[Individual,Int]

  def crossover( a: Individual, b: Individual ): Individual = {
    assert(a.length == b.length)
    val xpoint = rng.nextInt(a.length)
    val aPrefix = a.splitAt(xpoint)._1
    val bSuffix = b.splitAt(xpoint)._2
    aPrefix ++ bSuffix
  }

  def mutate( a: Individual ): Individual = {
    val mpoint = rng.nextInt(a.length)
    a.updated(mpoint, rng.nextRule(RuleSize))
  }

  def tournamentSelect(population: List[Individual]): Individual = {
    // println("  Starting tournament")
    val tournament = population.sortBy(x => fitness.getOrElseUpdate(x,0)).take(TournamentSize)
    tournament.reduce { (champion,contender) =>
       winnerOf(champion,contender)
    }
  }

  def initial: List[Individual] = 
    for( _ <- ( 1 to PopulationSize ).toList ) yield 
    for( j <- ( 1 to NumberOfRules ).toList  ) yield 
    rng.nextRule(RuleSize)

  def performIteration(population: List[Individual]): List[Individual] = {
    val progress = new ProgressDisplay( population.length/2 )
    (1 to population.length/2).toList flatMap { i =>
      val parent1 = tournamentSelect(population)
      val parent2 = tournamentSelect(population)
	  progress.increment()
      List( mutate(crossover(parent1,parent2)), mutate(crossover(parent2,parent1)) )
    }
  }

  // main GA loop
  def galoop: (Individual,Int) = {
    println("Starting GA loop")
    var population: List[Individual] = initial
    var iter = 0;
    while(iter < Iterations) {
      println( s"Iteration: ${iter+1} of $Iterations" )
      population = performIteration(population)
      iter = iter + 1
    }
    fitness.toList.head
  }

}