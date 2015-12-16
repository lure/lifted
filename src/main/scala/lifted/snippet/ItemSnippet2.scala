package lifted.snippet


import lifted.{Item, ItemDao}
import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.http._
import net.liftweb.http.js.{JsCmds, JsCmd}
import net.liftweb.http.js.JsCmds.RedirectTo
import net.liftweb.sitemap.Loc._
import net.liftweb.sitemap.{Loc, Menu}
import net.liftweb.util.Helpers
import net.liftweb.util.Helpers._

import scala.util.{Success, Try}
import scala.xml.Text

object ItemSnippet2 {
  lazy val ItemListMenu = Menu.i("items2") / "items2" / "list" >> LocGroup("items2")

  lazy val ItemCreateMenu2 = (Menu.i("items2.new") / "items2" / "edit" / "new").loc.calcDefaultHref

  lazy val ItemEditMenu = Menu.param[Item]("items2.edit", Loc.LinkText(getName),
    loadItem, (item: Item) => item.id.toString) / "items2" / "edit" >>
    MatchWithoutCurrentValue >>
    IfValue(_.isDefined, () => RedirectResponse(ItemListMenu.loc.calcDefaultHref)) >>
    Hidden >> LocGroup("items2")


  private object ItemMemoize extends RequestMemoize[String, Box[Item]] {
    override protected def __nameSalt: String = Helpers.randomString(20)
  }

  def loadItem(id: String): Box[Item] = {
    def tryItem =
      Try(id.toInt) match {
        case Success(int_id) => Box.legacyNullTest(ItemDao.findById(int_id))
        case _ if id == "new" => Full(ItemDao.newItem)
        case _ => Empty
      }

    if (S.inStatefulScope_?) {
      ItemMemoize(id, tryItem)
    } else {
      tryItem
    }
  }


  def getName(item: Item): Text = Text(if (item != null) item.name else "Unknown item")
}

class ItemSnippet2 extends StatefulSnippet {

  import lifted.snippet.ItemSnippet2._

  private var cursorItem: Box[Item] = Empty

  override def dispatch = {
    case "edit" => edit
    case "list" => list
  }

  def list = {
    cursorItem = Empty
    "data-name=itemList *" #> ItemDao.list.map {
      item =>
        "data-name=th *" #> item.id &
          "data-name=item-link [href]" #> ItemEditMenu.calcHref(item) &
          "data-name=item-link *" #> item.name &
          "data-name=br *" #> item.birth.toString &
          "data-name=actions *" #> SHtml.button(Text("delete"), () => {
            ItemDao.delete(item)
            S.redirectTo("/items2/list")
          })
    } &
      "data-name=add-button [onclick]" #> SHtml.onEvent(s => RedirectTo(ItemCreateMenu2))
  }

  //cancel button forces one more server trip with isdefined check :(
  def edit = {
    cursorItem.or(ItemEditMenu.currentValue) match {
      case Full(item) =>
        "#item.id *" #> (if (!item.isNew) "Editing id: " + item.id else "New item") &
          "#item.name" #> SHtml.text(item.name, nm => cursorItem = Full(item.copy(name = nm))) &
          "#submit" #> SHtml.onSubmitUnit(OnSave) &
          "#item.name.div [class+]" #> (if (S.errors.nonEmpty) "has-error" else "") &
          "#cancel" #> SHtml.ajaxButton("Cancel", () => {
            RedirectTo(ItemListMenu.loc.calcDefaultHref, () => S.notice("msg", "Edit: canceled"))
          })
//        "#cancel" #> SHtml.button("Cancel", () => {
//          S.notice("msg", "Edit: canceled")
//          S.redirectTo(ItemListMenu.loc.calcDefaultHref)
//        })

      case _ => S.redirectTo(ItemListMenu.loc.calcDefaultHref)
    }
  }

  private def OnSave(): Unit = cursorItem.foreach(item =>
    if (item.name.length < 3) {
      S.error("Item name must be at least 3 characters")
    } else {
      ItemDao.update(item)
      S.notice("msg", "Edit: success")
      S.redirectTo(ItemListMenu.loc.calcDefaultHref)
    })
}
