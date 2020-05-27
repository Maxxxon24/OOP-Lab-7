package component

import data.*
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.router.dom.*

interface AppProps : RProps {
    var students: Array<Student>
}

interface AppState : RState {
    var presents: Array<Array<Boolean>>
    var lessons: Array<Lesson>
}

interface RouteNumberResult : RProps {
    var number: String
}

class App : RComponent<AppProps, AppState>() {
    override fun componentWillMount() {
        state.lessons = lessonsList
        state.presents = Array(state.lessons.size) {
            Array(props.students.size) { false }
        }
    }

    override fun RBuilder.render() {
        header {
            h1 { +"App" }
            nav {
                ul {
                    li { navLink("/lessons") { +"Lessons" } }
                    li { navLink("/students") { +"Students" } }
                    li { navLink("/addlesson") { +"addlesson" } }
                }
            }
        }

        switch {
            route("/addlesson",
                exact = true,
                render = {
                    addLesson (addInputLesson())
                }
            )
            route("/lessons",
                exact = true,
                render = {
                    lessonList(state.lessons)
                }
            )
            route("/students",
                exact = true,
                render = {
                    studentList(props.students)
                }
            )
            route("/lessons/:number",
                render = { route_props: RouteResultProps<RouteNumberResult> ->
                    val num = route_props.match.params.number.toIntOrNull() ?: -1
                    val lesson = state.lessons.getOrNull(num)
                    if (lesson != null)
                        lessonFull(
                            lesson,
                            props.students,
                            state.presents[num]
                        ) { onClick(num, it) }
                    else
                        p { +"No such lesson" }
                }
            )
            route("/students/:number",
                render = { route_props: RouteResultProps<RouteNumberResult> ->
                    val num = route_props.match.params.number.toIntOrNull() ?: -1
                    val student = props.students.getOrNull(num)
                    if (student != null)
                        studentFull(
                            state.lessons,
                            student,
                            state.presents.map {
                                it[num]
                            }.toTypedArray()
                        ) { onClick(it, num) }
                    else
                        p { +"No such student" }
                }
            )
        }
    }

    private fun addInputLesson(): (String) -> Unit {
        return { newLesson: String ->
            setState {
                lessons += Lesson(newLesson)
                presents += arrayOf(Array(props.students.size) { false })
            }
        }
    }

    private fun onClick(indexLesson: Int, indexStudent: Int) =
        { _: Event ->
            setState {
                presents[indexLesson][indexStudent] =
                    !presents[indexLesson][indexStudent]
            }
        }
}

fun RBuilder.app(
    students: Array<Student>
) = child(App::class) {
    attrs.students = students
}