import java.util.logging._


class RLBot(id: Int, gameMap:GameMap) extends HaliteBot(id, gameMap) {
  import RLBot._
  import Direction._

  override def takeTurn(turn:BigInt, gameMap:GameMap): MoveList = {
    val moves = new MoveList()
    for (x <- 0 to gameMap.height - 1; y <- 0 to gameMap.width - 1) {
        val location: Location = new Location(x,y)
        val site: Site = gameMap.getSite(location)
        if (site.owner == id) {
          val pref: Map[Direction,Int] = Map()
          
          for(rule <- rules) {
            def success(d: Direction) = rule.pattern.matches(location,gameMap)(d)
            for(d <- List(NORTH,EAST,SOUTH,WEST)) {
              if( success(d) ) pref.updated(d, pref(d) + 1)
            }
          }
        
          val dir: Direction = pref.maxBy(_._2)._1
          moves.add(new Move(location, dir))
        }
    }
    moves
  }
 

}

///////////////////////////////////

object RLBot {

  private val LOGGER = Logger.getLogger( classOf[RLBot].getName )

  /////////////////////////////////

  var rules: List[Rule] = List()

  /////////////////////////////////

  private def run(args:Array[String]):Unit = {

	if(!args.isEmpty) {
		val ois = new java.io.ObjectInputStream( new java.io.FileInputStream( args(0) ) )
		val ruleArray: Array[Rule] = ois.readObject().asInstanceOf[Array[Rule]]
		rules = ruleArray.toList
		ois.close()
	}

    val maker = new HaliteBotMaker() {
      override def makeBot(id:Int, gameMap:GameMap):HaliteBot = new RLBot(id, gameMap)
    }

    HaliteBot.run(args, maker)
  }

  /////////////////////////////////

  def addConsoleLogging: Unit = LOGGER.addHandler( new ConsoleHandler() )
  def addFileLogging(prefix: String): Unit = {
	val dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format( new java.util.Date() )
	LOGGER.addHandler( new FileHandler( s"${prefix}${dateStr}-${System.currentTimeMillis}.txt" ) )
  }

  /////////////////////////////////

  def main(args:Array[String]):Unit = {

	try {
	  addFileLogging( "RLBot-log-" )
      run(args)
	}
	catch {
	  case t: Throwable => {
		LOGGER.log(Level.SEVERE, "Caught Throwable in RLBOT.main", t)
		// val ps = new java.io.PrintStream( new java.io.FileOutputStream( "error.tmp" ) )
		// ps.println( t )
		// ps.close()
	  }
	}
  }
}

// End ///////////////////////////////////////////////////////////////
