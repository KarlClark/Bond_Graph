package userInterface

import algebra.Equation
import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window


@Composable
fun showResults() {
    val currentState = LocalStateInfo.current
    currentState.showResultsWindow = true
    currentState.resultsWindowState.isMinimized = false
    currentState.resultsWindowOnTop = true
    currentState.resultsWindowOnTop = false
}

@Composable
fun showTestWindow() {
    val currentState = LocalStateInfo.current
    currentState.showTestWindow = true
    currentState.testWindowOnTop = true
    currentState.testWindowOnTop = false
}

@Composable
fun showComposedResults() {
    val currentState = LocalStateInfo.current
    currentState.showComposedResultsWIndow = true
    currentState.composedResultsWindowState.isMinimized = false
    currentState.composedResultsWindowOnTop = true
    currentState.composedResultsWindowOnTop = false
}

@Composable
fun resultsWindow() {
    var closeRequest by remember { mutableStateOf(false) }
    val results = remember{ bondGraph.results.resultsList}
    val scrollState = rememberScrollState()
    val currentState = LocalStateInfo.current

    //var valuesSetCopy by remember(bondGraph.valueSetWorkingCopy) { mutableStateOf( bondGraph.valueSetWorkingCopy) }
    Window(
        //onCloseRequest = {currentState.showValuesWindow = false}
        onCloseRequest = { closeRequest = true },
        state = currentState.resultsWindowState,
        alwaysOnTop = currentState.resultsWindowOnTop
    ) {
        Box(modifier = Modifier
            .padding(end = 12.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)


            ) {
                results.forEach {
                    Text(
                        it, fontSize = MyConstants.resultsFontSize, modifier = Modifier
                            .padding(start = 10.dp, top = 5.dp)
                    )
                }
            }

            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(14.dp)
                    .fillMaxHeight()

                , adapter = rememberScrollbarAdapter(
                    scrollState = scrollState
                )
            )
        }
    }

    if (closeRequest){
        currentState.showResultsWindow = false
    }
}

@Composable
fun testWindow() {
    var closeRequest by remember { mutableStateOf(false) }
    val currentState = LocalStateInfo.current

    Window (
        onCloseRequest = {closeRequest = true}
        ,state = currentState.testWindowState
        ,alwaysOnTop = currentState.testWindowOnTop
    ) {
       Box (){
           runTest()
       }
    }

    if (closeRequest){
        println("close test window")
        currentState.showTestWindow = false
    }
}

@Composable
fun composedResultsWindow() {

    var closeRequest by remember {mutableStateOf(false)}
    val intermediateResults = remember{ bondGraph.intermediateResults}
    val finalResults = remember{ bondGraph.finalResults}
    val verticalScrollState = rememberScrollState()
    val horizontalScrollState = rememberScrollState()
    val currentState = LocalStateInfo.current

    Window (
        onCloseRequest = {closeRequest = true}
        ,state = currentState.composedResultsWindowState
        ,alwaysOnTop = currentState.composedResultsWindowOnTop
    ){
        Box(modifier = Modifier
            .padding(all = 12.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(verticalScrollState)
                    .horizontalScroll(horizontalScrollState)

            ) {
                intermediateResults.forEach { pair ->
                    composeLabeledEquation(pair.first, pair.second)
                }

                val equationList = arrayListOf<Equation>()
                finalResults.forEach { eq ->
                    equationList.add(eq)
                }

                composeEquations(equationList, bondGraph.dotTokenToTokenMap)

            }

            VerticalScrollbar(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(14.dp)
                    .fillMaxHeight()

                , adapter = rememberScrollbarAdapter(
                    scrollState = verticalScrollState
                )
            )

            HorizontalScrollbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(14.dp)
                    .fillMaxWidth()

                , adapter = rememberScrollbarAdapter(
                    scrollState = horizontalScrollState
                )
            )

        }
    }

    if (closeRequest){
        currentState.showComposedResultsWIndow = false
    }
}