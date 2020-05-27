package component

import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import kotlin.browser.document

interface AddLessonProps : RProps{
    var addLesson : (String) ->  Unit
}

val addLessonFC = functionalComponent<AddLessonProps> { props ->
    div{
        span{ +"новый предмет: " }
        input(InputType.text) { attrs.id = "newLesson" }
        button{
            +"Добавить"
            attrs.onClickFunction = {
                props.addLesson(
                    (document.getElementById("newLesson") as HTMLInputElement).value
                )
            }
        }
    }
}

fun RBuilder.addLesson(ClickFun : (String) -> Unit ) =
    child(addLessonFC){
        attrs.addLesson = ClickFun
    }
