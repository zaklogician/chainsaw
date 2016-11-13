import aima.core.agent._
import aima.core.learning.reinforcement.PerceptStateReward
import aima.core.learning.reinforcement.agent._
import aima.core.probability.mdp.ActionsFunction

///////////////////////////////////

case class DirectionAction(direction: Direction) extends Action {
  override def isNoOp: Boolean = direction == Direction.STILL
}

case class GetActions[State]() extends ActionsFunction[State,DirectionAction] {
  override def actions(state: State): java.util.Set[DirectionAction] = {
    import scala.collection.JavaConversions._
    java.util.EnumSet.allOf(classOf[Direction]).map { DirectionAction }
  }
}

///////////////////////////////////

object AgentParams {
  // http://aimacode.github.io/aima-java/aima3e/javadoc/aima-core/aima/core/learning/reinforcement/agent/QLearningAgent.html :
  val Alpha = 0.2	// a fixed learning rate
  val Gamma = 1.0	// discount to be used
  val Ne = 5		// fixed parameter for internal use by QLearningAgent in the method f(u, n)
  val Rplus = 200		// optimistic estimate of the best possible reward obtainable in any state
}

object Agent extends 
  QLearningAgent[GameMap, DirectionAction]( 
	GetActions[GameMap]()
	, DirectionAction(Direction.STILL)
	, AgentParams.Alpha, AgentParams.Gamma, AgentParams.Ne, AgentParams.Rplus )

///////////////////////////////////

object ConstReward {
  def apply(s: Unit, r: Double): PerceptStateReward[Unit] = 
    new PerceptStateReward[Unit] {
      override def state: Unit = s
      override def reward: Double = r
    }
}

object TerritoryReward {
  def apply(gameMap: GameMap, myBotId: Int): PerceptStateReward[GameMap] = 
    new PerceptStateReward[GameMap] {
      override def state: GameMap = gameMap
      override def reward: Double = {
		var result = 0.0
    	for ( x <- 0 to gameMap.width - 1; y <- 0 to gameMap.height - 1 )
      	  if (gameMap.getSite(new Location(x, y)).owner == myBotId)
			result += 1

		result
	  }
    }
}

object StrengthReward {
  def apply(gameMap: GameMap, myBotId: Int): PerceptStateReward[GameMap] = 
    new PerceptStateReward[GameMap] {
      override def state: GameMap = gameMap
      override def reward: Double = {
		var result = 0.0
    	for ( x <- 0 to gameMap.width - 1; y <- 0 to gameMap.height - 1 ) {
			val site = gameMap.getSite(new Location(x, y))
      	  if (site.owner == myBotId)
			result += site.strength
		}

		result
	  }
    }
}

object DeltaStrengthReward {
  var oldGameMap: Option[GameMap] = None

  def apply(gameMap: GameMap, myBotId: Int): PerceptStateReward[GameMap] = {
    new PerceptStateReward[GameMap] {
      override def state: GameMap = gameMap
      override def reward: Double = {
		var result: Double = 0.0
    	for ( x <- 0 to gameMap.width - 1; y <- 0 to gameMap.height - 1 ) {
          val loc: Location = new Location(x, y)
	      val oldStrength: Option[Double] = for ( m <- oldGameMap ) yield {
            val oldSite = m.getSite(loc)
            if (oldSite.owner == myBotId) oldSite.strength else 0
          }
          val site: Site = gameMap.getSite(loc)
      	  if (site.owner == myBotId) result = 1.0*site.strength - oldStrength.getOrElse(0.0)
		}
        oldGameMap = Some(gameMap)
		result*0.001
	  }
    }
  }
}


object RandomReward {
  def apply(gameMap: GameMap, myBotId: Int): PerceptStateReward[GameMap] = 
    new PerceptStateReward[GameMap] {
      override def state: GameMap = gameMap
      override def reward: Double = 0.5-java.lang.Math.random()
    }
} 
///////////////////////////////////

object MoveLog extends java.io.PrintStream( new java.io.FileOutputStream( "movelog.txt" ) )

class RLBot(id: Int, gameMap: GameMap) extends HaliteBot(id, gameMap) {
  
  override def takeTurn(turn: BigInt, gameMap: GameMap): MoveList = {

    val moves = new MoveList()
    for ( x <- 0 to gameMap.width - 1; y <- 0 to gameMap.height - 1 ) {
      val site: Site = gameMap.getSite(new Location(x, y))
      if (site.owner == id) {
        try {
			
			val out = new java.io.PrintStream( System.out )
			val err = new java.io.PrintStream( System.err )
			System.setOut( new java.io.PrintStream( new java.io.FileOutputStream( "out-redir.txt" ) ) ) // NUL:" )  ) )
			System.setErr( new java.io.PrintStream( new java.io.FileOutputStream( "err-redir.txt" ) ) ) // NUL:" )  ) )
        	
            // val dir: Direction = Agent.execute(ConstReward(Unit, 1.0)).direction
			// val dir: Direction = Agent.execute(DeltaStrengthReward(gameMap, id)).direction
            val dir: Direction = Agent.execute(RandomReward(gameMap, id)).direction

			System.setOut( out )
			System.setErr( err )
            
            MoveLog.print(" loc: " + x + ", " + y + " dir: " + dir + "\n")

	        moves.add(new Move(new Location(x, y), dir))

		}
		catch {
			case t: Throwable => {
			  val ps = new java.io.PrintStream( new java.io.FileOutputStream( "error.txt" ) )
			  ps.print( t )
			  ps.close()
			}
		}
      }
    }
    moves
  }

}

///////////////////////////////////

object RLBot {

  def main(args:Array[String]):Unit = {

    val maker = new HaliteBotMaker() {
      override def makeBot(id:Int, gameMap:GameMap):HaliteBot = new RLBot(id, gameMap)
    }

    HaliteBot.run(args, maker)
  }
}

// End ///////////////////////////////////////////////////////////////
