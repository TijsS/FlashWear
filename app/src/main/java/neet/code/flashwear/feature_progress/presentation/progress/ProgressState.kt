package neet.code.flashwear.feature_progress.presentation.progress

import com.madrapps.plot.line.DataPoint
import neet.code.flashwear.feature_question.domain.model.Question
import org.joda.time.LocalDate

data class ProgressState (


    var timeSpentTotal: Map<TimeScaleGraph, List<DataPoint>> = mapOf(),

    var dateOfFirstLearnSession: LocalDate? = null,
    var selectedProgressGraph: ProgressGraph = ProgressGraph.Time,
    var selectedTimeScaleGraph: TimeScaleGraph = TimeScaleGraph.Day,
)


enum class ProgressGraph{
    Time
}

enum class TimeScaleGraph{
    Day, Week, Month
}