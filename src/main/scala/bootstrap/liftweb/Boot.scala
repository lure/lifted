package bootstrap.liftweb

import net.liftweb.common.{Empty, Full}
import net.liftweb.http.{Html5Properties, LiftRules, NoticeType, Req}
import net.liftweb.sitemap.Loc.Hidden
import net.liftweb.sitemap.{Menu, SiteMap}
import net.liftweb.util.Helpers._

/**
 * User: Shubert Alexandr
 * 5/4/15
 */
class Boot {

  val menu = SiteMap(
    Menu.i("index") / "index",
    Menu.i("items") / "item" / "list",
    Menu.i("items.edit") / "item" / "edit" >> Hidden
  )

  LiftRules.addToPackages("lifted")
  LiftRules.setSiteMap(menu)
  LiftRules.htmlProperties.default.set((r: Req) => new Html5Properties(r.userAgent))

  LiftRules.noticesAutoFadeOut.default.set((msg: NoticeType.Value) => msg match {
    case NoticeType.Notice => Full(4 seconds, 2 seconds)
    case _ => Empty
  })
}
