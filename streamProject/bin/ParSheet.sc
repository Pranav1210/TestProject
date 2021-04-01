object ParSheet {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  case class Name(firstName: String, lastName: String)
  object Name {
    def apply(f: String, middle: String, l: String): Name = Name(f+ " " + middle,l)
    
    def unapply(v: Name): Option[(String,String,String)] = Option {(v.firstName.split(" ")(0), v.firstName.split(" ")(1), v.lastName)}
    
  }
  val name  = Name("Virgil","Van", "Dike")        //> name  : ParSheet.Name = Name(Virgil Van,Dike)
  name match {
	  case Name(f,l) =>println(f+l)
  }                                               //> Virgil VanDike
  
}