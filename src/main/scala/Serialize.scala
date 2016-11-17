
object Serialize {

  def apply(to: java.io.OutputStream, original: Object): Unit = {
    val oos = new java.io.ObjectOutputStream( to )
    oos.writeObject( original )
    oos.close()
  }

}

object Deserialize {

  def apply[T](from: java.io.InputStream): T = {
    val ois = new java.io.ObjectInputStream(from) {
        override def resolveClass(desc: java.io.ObjectStreamClass): Class[_] = {
          try { Class.forName(desc.getName, false, getClass.getClassLoader) }
          catch { case ex: ClassNotFoundException => super.resolveClass(desc) }
        }
    }
    val clone = ois.readObject().asInstanceOf[T]
    ois.close()
    clone
  }

}
