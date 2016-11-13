import aima.core.agent._
import aima.core.learning.reinforcement.PerceptStateReward
import aima.core.learning.reinforcement.agent._
import aima.core.probability.mdp.ActionsFunction

///////////////////////////////////

case class DirectionAction(direction: Direction) extends Action {
  override def isNoOp: Boolean = direction == Direction.STILL
}


object GetActionsUnit extends ActionsFunction[Unit,DirectionAction] {
  override def actions(state: Unit): java.util.Set[DirectionAction] = {
    import scala.collection.JavaConversions._
    java.util.EnumSet.allOf(classOf[Direction]).map { DirectionAction }
  }
}

object GetActions extends ActionsFunction[GameMap,DirectionAction] {
  override def actions(state: GameMap): java.util.Set[DirectionAction] = {
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
  val Rplus = 2		// optimistic estimate of the best possible reward obtainable in any state
}

object Agent extends 
  QLearningAgent[Unit, DirectionAction]( 
	GetActionsUnit
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

///////////////////////////////////

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
        	val dir: Direction = Agent.execute(ConstReward(Unit, 1.0)).direction
			System.setOut( out )
			System.setErr( err )

	        moves.add(new Move(new Location(x, y), dir))

		}
		catch {
			case t: Throwable => {
			  Networking.write( "abhorrent pustule!" )
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
