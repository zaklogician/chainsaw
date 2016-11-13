object Genetic {
  val PopulationSize = 10
  val NumberOfRules = 2
  val TournamentSize = 4
  val Iterations = 10
  val rng: java.util.Random = new java.util.Random(0xDEAFBABE)

  type Individual = List[Rule]
  val fitness = scala.collection.mutable.Map.empty[Individual,Int]

  def crossover( a: Individual, b: Individual ): Individual = {
    assert(a.length == b.length)
    val xpoint = rng.nextInt(a.length)
    val aPrefix = a.splitAt(xpoint)._1
    val bSuffix = b.splitAt(xpoint)._2
    aPrefix ++ bSuffix
  }

  def newDirection: Direction = {
    val possible: List[Direction] = List(Direction.STILL, Direction.NORTH, Direction.EAST, Direction.WEST, Direction.SOUTH)
    possible(rng.nextInt(possible.length))
  }

  def newDesc: Desc = {
    val possible: List[Desc] = List(EnemyWeaker, EnemyStronger, AllyWeaker, AllyStronger)
    possible(rng.nextInt(possible.length))
  }

  def newRule: Rule = {
    val newPattern = Pattern( newDesc, newDesc, newDesc, newDesc, newDesc, newDesc, newDesc, newDesc )
    Rule(newPattern, newDirection)
  }

  def mutate( a: Individual ): Individual = {
    val mpoint = rng.nextInt(a.length)
    a.updated(mpoint, newRule)
  }

  def winnerOf(i1: Individual, i2: Individual): Individual = {
    val file1 = "i1.bot"
    val file2 = "i2.bot"
    
   	{ 
      val oos = new java.io.ObjectOutputStream( new java.io.FileOutputStream("games/"+file1) )
	  oos.writeObject( i1.toArray )
	  oos.close()
    }
    { 
      val oos = new java.io.ObjectOutputStream( new java.io.FileOutputStream("games/"+file2) )
	  oos.writeObject( i2.toArray )
	  oos.close()
    }
    
    val cmd = s"""games/halite -d "30 30" "scala RLBot $file1" "scala RLBot $file2" """
    println("   Running " + cmd)
    val testProgOutput = scala.sys.process.Process(cmd).lines.toList
    val result = if( testProgOutput.contains("Player #1, ScalaBot, came in rank #1!") ) i1 else i2
    fitness.update(result, fitness.getOrElse(result, 0) + 1)
    result
  }

  def tournamentSelect(population: List[Individual]): Individual = {
    println("  Starting tournament")
    val tournament = population.sortBy(x => fitness.getOrElseUpdate(x,0)).take(TournamentSize)
    tournament.reduce { (champion,contender) =>
       println("   Running match " + (champion, contender))
       winnerOf(champion,contender)
    }
  }

  def initial: List[Individual] = for( _ <- ( 1 to PopulationSize ).toList ) yield { for( j <- ( 1 to NumberOfRules ).toList ) yield newRule }

  // main GA loop
  def galoop: Individual = {
    println("Starting GA loop")
    var population: List[Individual] = initial
    var iter = 0;
    while(iter < Iterations) {
      println("Iteration " + iter)
      val newPopulation = (1 to population.length/2) flatMap { i =>
        val parent1 = tournamentSelect(population)
        val parent2 = tournamentSelect(population)
        List( mutate(crossover(parent1,parent2)), mutate(crossover(parent2,parent1)) )
      }
      iter = iter + 1
    }
    fitness.toList.head._1
  }

  def main(args: Array[String]): Unit = {
    val best = galoop
    println(best)
  }

}