import com.sun.jndi.dns._
import scala.collection.JavaConverters

val addrs = new DnsName("www.google.com")
addrs.getAll.