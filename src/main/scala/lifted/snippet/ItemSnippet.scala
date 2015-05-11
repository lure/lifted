package lifted.snippet

import lifted.ItemDao
import net.liftweb.http.{S, SHtml}
import net.liftweb.util.Helpers._

import scala.xml.{Elem, Text}

class ItemSnippet {

  def list = {
    "@itemList *" #> ItemDao.list.map {
      item =>
        "@th *" #> item.id &
          "@nm *" #> item.name &
          "@br *" #> item.birth.toString &
          ".actions *" #> {
            SHtml.link("/item/edit", () => ItemVar(item), Text("edit")) ++ Text(" ") ++
              SHtml.link("/item/list", () => ItemDao.delete(item), Text("delete"))
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
        S.redirectTo("/item/list")
      })
  }

  private def OnSave(): Unit = {
    if (ItemVar.is.name.length < 3) {
      S.error("Item name must be at least 3 characters")
    } else {
      ItemDao.update(ItemVar.is)
      S.notice("msg", "Save: success")
      S.redirectTo("/item/list")
    }

  }
}
