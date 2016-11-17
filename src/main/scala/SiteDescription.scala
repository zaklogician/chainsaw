sealed trait Comparison {
  def compare(site: Site, owner: Site): Boolean
}
case object IsStrongerEnemy  extends Comparison {
  override def compare(site: Site, owner: Site): Boolean = 
    (site.owner != owner.owner) && (site.strength >= owner.strength)
}
case object IsStrongerAlly   extends Comparison {
  override def compare(site: Site, owner: Site): Boolean = 
    (site.owner == owner.owner) && (site.strength >= owner.strength)
}
case object IsWeakerEnemy    extends Comparison {
  override def compare(site: Site, owner: Site): Boolean = 
    (site.owner != owner.owner) && (site.strength <  owner.strength)
}
case object IsWeakerAlly     extends Comparison {
  override def compare(site: Site, owner: Site): Boolean = 
    (site.owner == owner.owner) && (site.strength < owner.strength)
}
case object IsMoreProductive extends Comparison {
  override def compare(site: Site, owner: Site): Boolean = 
    (site.production >= owner.production)
}
case object IsLessProductive extends Comparison {
  override def compare(site: Site, owner: Site): Boolean = 
    (site.production <  owner.production)
}
case object DontCare         extends Comparison {
  override def compare(site: Site, owner: Site): Boolean = true
}


case class Pattern(comparisons: List[Comparison]) {
  def matches(origin: Location, gameMap: GameMap)(direction: Direction): Boolean = {
    val owner: Site = gameMap.getSite(origin)
    var current: Location = origin
    var isMatching: Boolean = false
    comparisons.forall { c => 
      current = gameMap.getLocation(current, direction)
      val site: Site = gameMap.getSite(current)
      c compare (site, owner)
    }
  }
}

@SerialVersionUID(0L)
case class Rule(pattern: Pattern, direction: Direction) extends Serializable