

import aima.core.agent._
import aima.core.learning.reinforcement.agent._
import aima.core.probability.mdp.ActionsFunction
import java.util.List

///////////////////////////////////

case class DirectionAction(direction: Direction) extends Action {
  override def isNoOp: Boolean = direction == Direction.STILL
}

class GameMapEnvironment(var gameMap: GameMap) extends Environment {

  val agent = new QLearningAgent[Unit, DirectionAction](
	new ActionsFunction[Unit,DirectionAction]() { 
	  override def actions(state: Unit): java.util.Set[DirectionAction] =  {
		import scala.collection.JavaConversions._
		java.util.EnumSet.allOf(classOf[Direction]).map { DirectionAction }
	  }
	}, 
	DirectionAction(Direction.STILL), 0.2, 1.0, 5, 2.0 )

  /////////////////////////////////

  def addAgent(agent: Agent): Unit = ???

  def addEnvironmentObject(eo: EnvironmentObject): Unit = ???

  def addEnvironmentView(ev: EnvironmentView): Unit = ???

  def getAgents: java.util.List[Agent] = ???	
  def getEnvironmentObjects: java.util.List[EnvironmentObject] = ???

  def getPerformanceMeasure(forAgent: Agent): Double = ???
  def isDone: Boolean = ???
  def notifyViews(msg: String): Unit = ???

  def removeAgent(agent: Agent): Unit = ???

  def removeEnvironmentObject(eo: EnvironmentObject): Unit = ???
  def removeEnvironmentView(ev: EnvironmentView): Unit = ???

  /////////////////////////////////

  // Move the Environment one time step forward.
  def step(): Unit = ???
//  def step(): Unit = {
// 	val perceptFIXME = ()
//	val action = agent.execute( perceptFIXME )
//  }

  // Move the Environment n time steps forward.
  def step(n: Int): Unit = ???

  def stepUntilDone(): Unit = ???
}

// End ///////////////////////////////////////////////////////////////
