package neet.code.flashwear.feature_progress.presentation.progress


sealed class ProgressEvent {
    data class SelectProgressGraph(val selectedGraph: ProgressGraph): ProgressEvent()
    data class SelectedTimeScale(val selectedTimeScale: TimeScaleGraph): ProgressEvent()
}