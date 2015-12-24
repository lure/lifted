package lifted

import net.liftweb.http.S._
import net.liftweb.http.SHtml
import net.liftweb.http.SHtml.{ChoiceItem, ChoiceHolder}

import scala.xml.{Null, UnprefixedAttribute}

object CheckboxGroup {

  /**
   * Generate a ChoiceHolder of possible checkbox type inputs that calls back to the given function when the form is submitted.
   *
   * @param possible complete sequence of possible values, each a separate checkbox when rendered
   * @param actual values to be preselected
   * @param func function to receive all values corresponding to the checked boxes
   * @param attrs sequence of attributes to apply to each checkbox input element
   * @return ChoiceHolder containing the checkboxes and values in order
   */
  def apply(possible: Seq[CheckOption], actual: Seq[Long], func: Seq[Long] => Any, attrs: SHtml.ElemAttr*): ChoiceHolder[String] = {
    def checked(in: Boolean) = if (in) new UnprefixedAttribute("checked", "checked", Null) else Null

    fmapFunc {
      LFuncHolder((selectedChoiceValues: List[String]) => {

        val selectedValues = selectedChoiceValues.map(_.toLong).filter(s => possible.exists(p => p.value == s))
        func(selectedValues)
        true
      })
    } { name =>
      ChoiceHolder(possible.toList.zipWithIndex.map { possibleChoice =>
        ChoiceItem[String](
          possibleChoice._1.label,
          attrs.foldLeft(<input type="checkbox" name={name} value={possibleChoice._1.value.toString}/>)(_ % _) %
            checked(actual.contains(possibleChoice._1.value)) ++ {
            if (possibleChoice._2 == 0)
                <input type="hidden" name={name} value="-1"/>
            else
              Nil
          }
        )
      })
    }
  }

  final case class CheckOption(value: Long, label: String)
}
