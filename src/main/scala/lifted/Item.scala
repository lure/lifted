package lifted

import java.util.Date

/**
 * User: Shubert Alexandr
 * 5/4/15
 */
case class Item(id: Int, name: String, birth: Date = new Date, isNew:Boolean = false)
