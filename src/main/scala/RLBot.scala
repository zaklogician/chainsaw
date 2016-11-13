
sealed trait Desc {
  def matches(that: Desc) = this == that
}
case object EnemyWeaker extends Desc
case object EnemyStronger extends Desc
case object AllyWeaker extends Desc
case object AllyStronger extends Desc
case object DontCare extends Desc {
  override def matches(that: Desc) = true
}

object Desc {
  def compare(site1: Site, site2: Site): Desc = {
    if (site1.owner == site2.owner) {
      if (site1.strength < site2.strength) AllyWeaker
      else                                 AllyStronger
    }
    else {
      if (site1.strength < site2.strength) EnemyWeaker
      else                                 EnemyStronger
    }
  }
}

case class Pattern( i1: Desc,           i3: Desc
                  , i4: Desc, i5: Desc, i6: Desc
                  , i7: Desc, i8: Desc, i9: Desc
                  ) {
  def matches(that: Pattern): Boolean = {
    (this.i1 matches that.i1) &&
    (this.i3 matches that.i3) &&
    (this.i4 matches that.i4) &&
    (this.i5 matches that.i5) &&
    (this.i6 matches that.i6) &&
    (this.i7 matches that.i7) &&
    (this.i8 matches that.i8)
  }
}

@SerialVersionUID(0L)
case class Rule(pattern: Pattern, direction: Direction) extends Serializable

object Pattern {
  def safeLoc(gameMap: GameMap, x: Int, y: Int): Location = {
    new Location((x + gameMap.width) % gameMap.width, (y + gameMap.height) % gameMap.height)
  }

  def southOf(gameMap: GameMap, loc: Location): Pattern = {
    val self: Site = gameMap.getSite( safeLoc(gameMap,loc.x, loc.y)   )
    val s1: Site = gameMap.getSite( safeLoc(gameMap,loc.x-1, loc.y)   )
    val s3: Site = gameMap.getSite( safeLoc(gameMap,loc.x+1, loc.y)   ) 
    val s4: Site = gameMap.getSite( safeLoc(gameMap,loc.x-1, loc.y-1) )
    val s5: Site = gameMap.getSite( safeLoc(gameMap,loc.x,   loc.y-1) )
    val s6: Site = gameMap.getSite( safeLoc(gameMap,loc.x+1, loc.y-1) )
    val s7: Site = gameMap.getSite( safeLoc(gameMap,loc.x-1, loc.y-2) )
    val s8: Site = gameMap.getSite( safeLoc(gameMap,loc.x,   loc.y-2) )
    val s9: Site = gameMap.getSite( safeLoc(gameMap,loc.x+1, loc.y-2) )
    Pattern( Desc.compare(self, s1), Desc.compare(self, s3)
           , Desc.compare(self, s4), Desc.compare(self, s5), Desc.compare(self, s6)
           , Desc.compare(self, s7), Desc.compare(self, s8), Desc.compare(self, s9)
           )
  }

  def northOf(gameMap: GameMap, loc: Location): Pattern = {
    val self: Site = gameMap.getSite( safeLoc(gameMap,loc.x, loc.y)   )
    val s1: Site = gameMap.getSite( safeLoc(gameMap,loc.x+1, loc.y)   )
    val s3: Site = gameMap.getSite( safeLoc(gameMap,loc.x-1, loc.y)   ) 
    val s4: Site = gameMap.getSite( safeLoc(gameMap,loc.x+1, loc.y+1) )
    val s5: Site = gameMap.getSite( safeLoc(gameMap,loc.x,   loc.y+1) )
    val s6: Site = gameMap.getSite( safeLoc(gameMap,loc.x-1, loc.y+1) )
    val s7: Site = gameMap.getSite( safeLoc(gameMap,loc.x+1, loc.y+2) )
    val s8: Site = gameMap.getSite( safeLoc(gameMap,loc.x,   loc.y+2) )
    val s9: Site = gameMap.getSite( safeLoc(gameMap,loc.x-1, loc.y+2) )
    Pattern( Desc.compare(self, s1), Desc.compare(self, s3)
           , Desc.compare(self, s4), Desc.compare(self, s5), Desc.compare(self, s6)
           , Desc.compare(self, s7), Desc.compare(self, s8), Desc.compare(self, s9)
           )
  }

  def eastOf(gameMap: GameMap, loc: Location): Pattern = {
    val self: Site = gameMap.getSite( safeLoc(gameMap,loc.x, loc.y)   )
    val s1: Site = gameMap.getSite( safeLoc(gameMap,loc.x, loc.y-1)   )
    val s3: Site = gameMap.getSite( safeLoc(gameMap,loc.x, loc.y+1)   ) 
    val s4: Site = gameMap.getSite( safeLoc(gameMap,loc.x-1, loc.y-1) )
    val s5: Site = gameMap.getSite( safeLoc(gameMap,loc.x-1,   loc.y) )
    val s6: Site = gameMap.getSite( safeLoc(gameMap,loc.x-1, loc.y+1) )
    val s7: Site = gameMap.getSite( safeLoc(gameMap,loc.x-2, loc.y-1) )
    val s8: Site = gameMap.getSite( safeLoc(gameMap,loc.x-2,   loc.y) )
    val s9: Site = gameMap.getSite( safeLoc(gameMap,loc.x-2, loc.y+1) )
    Pattern( Desc.compare(self, s1), Desc.compare(self, s3)
           , Desc.compare(self, s4), Desc.compare(self, s5), Desc.compare(self, s6)
           , Desc.compare(self, s7), Desc.compare(self, s8), Desc.compare(self, s9)
           )
  }

  def westOf(gameMap: GameMap, loc: Location): Pattern = {
    val self: Site = gameMap.getSite( safeLoc(gameMap,loc.x, loc.y)   )
    val s1: Site = gameMap.getSite( safeLoc(gameMap,loc.x  , loc.y+1) )
    val s3: Site = gameMap.getSite( safeLoc(gameMap,loc.x  , loc.y-1) ) 
    val s4: Site = gameMap.getSite( safeLoc(gameMap,loc.x-1, loc.y+1) )
    val s5: Site = gameMap.getSite( safeLoc(gameMap,loc.x-1, loc.y  ) )
    val s6: Site = gameMap.getSite( safeLoc(gameMap,loc.x-1, loc.y-1) )
    val s7: Site = gameMap.getSite( safeLoc(gameMap,loc.x-2, loc.y+1) )
    val s8: Site = gameMap.getSite( safeLoc(gameMap,loc.x-2, loc.y  ) )
    val s9: Site = gameMap.getSite( safeLoc(gameMap,loc.x-2, loc.y-1) )
    Pattern( Desc.compare(self, s1), Desc.compare(self, s3)
           , Desc.compare(self, s4), Desc.compare(self, s5), Desc.compare(self, s6)
           , Desc.compare(self, s7), Desc.compare(self, s8), Desc.compare(self, s9)
           )
  }

}


class RLBot(id: Int, gameMap:GameMap) extends HaliteBot(id, gameMap) {
  import RLBot._

  override def takeTurn(turn:BigInt, gameMap:GameMap): MoveList = {
    // Random moves
    val moves = new MoveList()
    for (x <- 0 to gameMap.height - 1; y <- 0 to gameMap.width - 1) {
        val site: Site = gameMap.getSite(new Location(x, y))
        if (site.owner == id) {
          val loc: Location = new Location(x, y)
          val extracted: List[Pattern] = List( Pattern.northOf(gameMap,loc)
                                             , Pattern.southOf(gameMap,loc)
                                             , Pattern.eastOf(gameMap,loc)
                                             , Pattern.westOf(gameMap,loc)
                                             )
          val dir: Direction = 
            rules.flatMap( r => if (extracted.exists(r.pattern matches _)) List(r.direction) else Nil ).headOption getOrElse (Direction.STILL)
          moves.add(new Move(loc, dir))
        }
    }
    moves
  }

}

object RLBot {

  //var rules: List[Rule] = List()
  import Direction._
  var rules: List[Rule] = List(Rule(Pattern(EnemyWeaker,AllyStronger,EnemyWeaker,AllyWeaker,EnemyWeaker,AllyWeaker,AllyWeaker,EnemyWeaker),NORTH), Rule(Pattern(EnemyWeaker,EnemyWeaker,AllyStronger,EnemyStronger,EnemyWeaker,EnemyStronger,AllyWeaker,AllyStronger),SOUTH))

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

  def main(args:Array[String]):Unit = {

	try {
      run(args)
	}
	catch {
	  case t: Throwable => {
		val ps = new java.io.PrintStream( new java.io.FileOutputStream( "error.tmp" ) )
		ps.println( t )
		ps.close()
	  }
	}
  }
}
