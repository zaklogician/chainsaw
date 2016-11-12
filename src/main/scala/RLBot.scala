import aima.core.learning.reinforcement._

case class MyPerceptStateReward() extends PerceptStateReward[Unit] {
  override def state: Unit = ()
  override def reward: Double = 1.0
}

///////////////////////////////////

class RLBot(id: Int, gameMap: GameMap) extends HaliteBot(id, gameMap) {
  private val env = new GameMapEnvironment(gameMap)

  /////////////////////////////////

  override def takeTurn(turn:BigInt, gameMap:GameMap): MoveList = {
    
	env.gameMap = gameMap 

    val moves = new MoveList()
    for (y <- 0 to gameMap.height - 1) {
      for (x <- 0 to gameMap.width - 1) {
        val site: Site = gameMap.getSite(new Location(x, y))
        if (site.owner == id) {
          val dir: Direction = Direction.randomDirection
		  //val perceptFIXME = MyPerceptStateReward()
		  //val action = env.agent.execute( perceptFIXME )
          //moves.add(new Move(new Location(x, y), action.direction))
          moves.add(new Move(new Location(x, y), dir))
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
