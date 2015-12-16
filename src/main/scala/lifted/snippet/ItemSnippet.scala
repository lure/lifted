package lifted.snippet

import lifted.{Item, ItemDao}
import net.liftweb.common.Box
import net.liftweb.http.{RequestVar, S, SHtml}
import net.liftweb.sitemap.{*, Loc, Menu}
import net.liftweb.util.Helpers._

import scala.xml.{Elem, Text}

class ItemSnippet {
  object ItemVar  extends RequestVar[Item](ItemDao.newItem)

  def list = {
    "data-name=itemList *" #> ItemDao.list.map {
      item =>
        "data-name=th *" #> item.id &
          "data-name=item-link [href]" #> "#" & //ItemMenu.calcHref(item) &
          "data-name=item-link *" #> item.name &
          "data-name=br *" #> item.birth.toString &
          "data-name=actions *" #> {
            SHtml.link("/items/edit", () => ItemVar(item), Text("edit")) ++ Text(" ") ++
              SHtml.link("/items/list", () => ItemDao.delete(item), Text("delete"))
          }
    }
  }

  def edit = {
    val item = ItemVar.get
    "#hidden" #> SHtml.hidden(() => ItemVar(item)) &
      "#item.id *" #> (if (!item.isNew) "Editing id: " + item.id else "New item") &
      "#item.name" #> SHtml.text(item.name, nm => ItemVar(ItemVar.is.copy(name = nm))) &
      "#submit" #> SHtml.onSubmitUnit(OnSave) &
      "#item.name.div [class+]" #> (if (S.errors.nonEmpty) "has-error" else "") &
      "#cancel" #> SHtml.button("Cancel", () => {
        S.notice("msg", "Edit canceled")
        S.redirectTo("/items/list")
      })
  }

  def OnSave(): Unit = {
    if (ItemVar.is.name.length < 3) {
      S.error("Item name must be at least 3 characters")
    } else {
      ItemDao.update(ItemVar.is)
      S.notice("msg", "Save: success")
      S.redirectTo("/item/list")
    }
  }
}