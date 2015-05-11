package lifted

import java.util.Date

import scala.collection.mutable

/**
 * User: Shubert Alexandr
 * 5/4/15
 */
// Not supposed to be thread safe.
object ItemDao {

  private val items = new mutable.HashMap[Int, Item]()
  (1 to 5).foreach { i =>
    items += (i -> Item(i, "item " + i))
  }

  private var index: Integer = items.size

  def list: List[Item] = items.values.toList.sortBy(_.id)

  def update(item: Item) = withNotNull(item) {
    items(item.id) = item.copy(isNew = false)
  }

  def insert(item: Item) = withNotNull(item) {
    items(item.id) = item
  }

  def delete(id: Integer) = items.remove(id)

  def delete(item: Item) = withNotNull(item) {
    items.remove(item.id)
  }

  def newItem = {
    index = index + 1
    Item(index, "item" + index, new Date, isNew = true)
  }

  private def withNotNull(item: Item)(f: => Any) = {
    if (item != null) {
      f
      this
    } else
      throw new IllegalArgumentException("Item can't be null")
  }

}

