package lifted.snippet

import lifted.{ItemDao, Item}
import net.liftweb.http.RequestVar

/**
 * User: Shubert Alexandr
 * 5/9/15
 */
object ItemVar  extends RequestVar[Item](ItemDao.newItem)

