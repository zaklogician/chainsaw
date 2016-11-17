import org.scalacheck.Properties
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Gen.{oneOf, listOf, alphaStr, numChar}
import Generators._


object TestPattern extends Properties("Pattern") {
  private val origin = new Location(0,0)

  property("empty matches everything") = Prop.forAll(direction, site) { (dir, site) =>
    object MockMap extends GameMap(5, 5) { }
    val neighbor = MockMap.getLocation( origin, dir )
    MockMap.contents.get(neighbor.x).set(neighbor.y, site)
    Pattern(List()).matches(origin, MockMap)(dir)
  }

  property("DontCare matches everything") = Prop.forAll(direction, site) { (dir, site) =>
    object MockMap extends GameMap(5, 5) { }
    val neighbor = MockMap.getLocation( origin, dir )
    MockMap.contents.get(neighbor.x).set(neighbor.y, site)
    Pattern(List(DontCare)).matches(origin, MockMap)(dir)
  }
  
}
