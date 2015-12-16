package bootstrap.liftweb

import lifted.snippet.ItemSnippet2
import net.liftweb.common.{Empty, Full}
import net.liftweb.http._
import net.liftweb.sitemap.Loc.{Hidden, LocGroup}
import net.liftweb.sitemap.{Menu, SiteMap}
import net.liftweb.util.Helpers._

/**
  * User: Shubert Alexandr
  * 5/4/15
  */
class Boot {
  //val helpMenu = Menu(Loc("HelpHome", ("items2" :: "edit" :: "" :: Nil) -> true, "Help", TemplateBox(() => Templates("items" :: "list" :: Nil))))

  val menu = SiteMap(
    Menu.i("index") / "index",
    Menu.i("items") / "items" / "list" >> LocGroup("items"),
    Menu.i("items.edit") / "items" / "edit" >> LocGroup("items") >> Hidden,
    ItemSnippet2.ItemListMenu,
    ItemSnippet2.ItemEditMenu
  )


  LiftRules.addToPackages("lifted")
  LiftRules.setSiteMap(menu)
  LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

  //noinspection MatchToPartialFunction
  LiftRules.noticesAutoFadeOut.default.set((msg: NoticeType.Value) => msg match {
    case NoticeType.Notice => Full(4 seconds, 2 seconds)
    case _ => Empty
  })
}
