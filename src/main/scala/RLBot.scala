import aima.core.agent._
import aima.core.learning.reinforcement.PerceptStateReward
import aima.core.learning.reinforcement.agent._
import aima.core.probability.mdp.ActionsFunction


case class DirectionAction(direction: Direction) extends Action {
  override def isNoOp: Boolean = direction == Direction.STILL
}

object GetActions extends ActionsFunction[Unit,DirectionAction] {
  override def actions(state: Unit): java.util.Set[DirectionAction] = {
    import scala.collection.JavaConversions._
    java.util.EnumSet.allOf(classOf[Direction]).map { DirectionAction }
  }
}

object Agent extends 
  QLearningAgent[Unit, DirectionAction]( GetActions
                                       , DirectionAction(Direction.STILL)
                                       , 0.2, 1.0, 5, 2.0
                                       )


object ConstReward {
  def apply(s: Unit, r: Double): PerceptStateReward[Unit] = 
    new PerceptStateReward[Unit] {
      override def state: Unit = s
      override def reward: Double = r
    }
}



class RLBot(id: Int, gameMap:GameMap) extends HaliteBot(id, gameMap) {

  override def takeTurn(turn:BigInt, gameMap:GameMap): MoveList = {

    val moves = new MoveList()
    for (x <- 0 to gameMap.width - 1; y <- 0 to gameMap.height - 1) {
      val site: Site = gameMap.getSite(new Location(x, y))
      if (site.owner == id) {
        val dir: Direction = Direction.randomDirection
        moves.add(new Move(new Location(x, y), dir))
      }
    }
    moves
  }

}

object RLBot {

  def main(args:Array[String]):Unit = {

    val maker = new HaliteBotMaker() {
      override def makeBot(id:Int, gameMap:GameMap):HaliteBot = new RLBot(id, gameMap)
    }

    HaliteBot.run(args, maker)
  }
}
