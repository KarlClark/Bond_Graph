package userInterface

import algebra.testCases
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.isShiftPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import bondgraph.BondGraph
import bondgraph.ElementTypes
import bondgraph.ElementDisplayData
import bondgraph.displayIntermediateResults
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.pathString
import kotlin.io.path.readText
import kotlin.io.path.writeText


val bondGraph = BondGraph("test graph")

object MyConstants {
    val labelFontSize: TextUnit = 15.sp
    val bottomBarFontSize = 15.sp
    val resultsFontSize = 20.sp
    val elementNameFontSize: TextUnit =20.sp
    val subTextFontSize: TextUnit = 15.sp
    val menuBarFontSize = 18.sp
    val menuItemFontSize = 14.sp
    val valuesBarFontSize = 14.sp
    val valuesBarFontSizeSmall = 12.sp
    val plusSignFontSize = 22.sp
    val valuesFontSize = 14.sp

    val valuesRowHeight = 35.dp
    val setColumnWidth = 180.dp
    val valuesColumnWidth = 600.dp
    val valuesGeneralPadding = 6.dp
    val valueColumnWidth = 100.dp
    val unitsColumnWidth = 60.dp
    val descriptionColumnWidth = 150.dp
    val diplayNameWidth = 60.dp
    val expandIconSIze = 10.dp
    val smallDialogWidth = 250.dp
    val smallDialogHeight = 180.dp
    val largeDialogWidth = 450.dp
    val largeDialogHeight = 250.dp

    val myGreen = Color(10, 140, 10)
    val myWhite = Color(250, 250, 250)
    val myOrange = Color(237, 102, 43)
    val offColor = Color.LightGray
    val onColor = myGreen
    val lightBlue = Color(144, 190, 248)
    val mediumBlue = Color(53, 135, 237)
    val mediumGray = Color(180, 180, 180)
    val draggingColor = Color.Red
    val notDraggingColor = Color.Black
    val defaultElementColor = Color.Black
    val defaultBondColor = Color.Black
    val graphBackground = Color(223, 216, 214)
    val resultsBackground = Color(206, 232, 246)
    val unassignedColor = myOrange
    val arbitrarilyAssignedColor = myGreen
    val derivativeCausalityColor = myGreen
    val valuesBarsColor = mediumBlue
    val valuesBarsTextColor = Color.White
    val setDefaultColor = Color.LightGray
    val setSelectedColor = lightBlue

}

enum class SaveOptions {
    SAVE,
    SAVE_AS,
    DONT_SAVE,
    CANCEL
}


// This composable builds the sidebar for the window.
@Composable
fun sideBar() {

    val currentState = LocalStateInfo.current
    var bondModeColor by remember { mutableStateOf (Color.LightGray)}
    var nodeModeColor by remember { mutableStateOf( MyConstants.myGreen)}


    Box(Modifier
        .fillMaxHeight()
        .background(Color.Gray)
    ) {
        Column(  // will contain three other columns stacked on top of each other
            Modifier
                .padding(2.dp)  // Box background will show through creating a border
                .width(IntrinsicSize.Min)
                .fillMaxHeight()
            ,horizontalAlignment = Alignment.CenterHorizontally
            ,verticalArrangement = Arrangement.spacedBy(2.dp, alignment = Alignment.Top)
        )
        {

            Column (Modifier  // Contains two Texts each of which can respond to tap gestures.
                .background(MyConstants.myWhite)
                .fillMaxWidth()
                .padding(end = 12.dp)
                ,horizontalAlignment = Alignment.CenterHorizontally
                ,verticalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterVertically)

            ){

                Text("Bond\nMode", color = bondModeColor, textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top= 10.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    // Switch to BOND_MODE if there are at least two elements to draw a bond between
                                    if (currentState.mode == Mode.ELEMENT_MODE && bondGraph.getElementList().size >= 2) {
                                        bondModeColor = MyConstants.onColor
                                        nodeModeColor = MyConstants.offColor
                                        currentState.mode = Mode.BOND_MODE

                                    }
                                }
                            )

                        }
                )

                Text("Node\nMode", color = nodeModeColor, textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 10.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    // Switch to NODE_MODE
                                    if (currentState.mode == Mode.BOND_MODE) {
                                        nodeModeColor = MyConstants.onColor
                                        bondModeColor = MyConstants.offColor
                                        currentState.mode = Mode.ELEMENT_MODE
                                    }
                                }
                            )
                        }
                )
            }

            Column ( // this is the column that displays our dragTarget composables, one for each element type.
                 horizontalAlignment = Alignment.CenterHorizontally
                //,verticalArrangement = Arrangement.spacedBy(12.dp, alignment = Alignment.Bottom)
                ,verticalArrangement = Arrangement.spacedBy(12.dp)
                ,modifier= Modifier
                    .background(Color.LightGray)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ){
                Spacer (modifier = Modifier .height(1.dp))
                var id = 1000
                enumValues<ElementTypes>().forEach {
                    if (it != ElementTypes.INVALID_TYPE) {displayElement (ElementDisplayData (id++, it.toAnnotatedString(), Offset.Zero, 0f, 0f, Offset.Zero))
                    }
                    if (it == ElementTypes.ONE_JUNCTION || it == ElementTypes.MODULATED_TRANSFORMER) Divider(thickness = 2.dp, color = Color.Black)
                }
                Spacer (modifier = Modifier .height(1.dp))
            }

            Column ( // this column holds the Augment and Derive buttons.
                Modifier
                    .background(Color.White)
                    .padding(2.dp)
                    //.fillMaxHeight().width(60.dp).fillMaxWidth()
                    .wrapContentWidth()

                ,horizontalAlignment = Alignment.CenterHorizontally
                ,verticalArrangement = Arrangement.spacedBy(5.dp, alignment = Alignment.CenterVertically)
            ){

                Button (colors = ButtonDefaults.buttonColors(backgroundColor = MyConstants.myGreen, contentColor = Color.White)
                    ,onClick = {
                    currentState.augment = true
                }
                ){
                    Text("Augment")
                }

                Button (colors = ButtonDefaults.buttonColors(backgroundColor = MyConstants.myGreen, contentColor = Color.White)
                    ,onClick = {
                        currentState.derive = true
                    }){
                    Text("Derive")
                }

                if (currentState.augment){  // Augment button was clicked
                    currentState.augment = false
                    bondGraph.augment()
                }

                if (currentState.derive){ // Derive button was clicked
                    currentState.derive = false
                    bondGraph.derive()
                }
            }
        }
    }
}

// This composable builds the bottom bar of our window, which
// contains to clickable Texts
@Composable
fun bottomBar() {

    val currentState = LocalStateInfo.current
    var triggerResults by remember{ mutableStateOf(false) }

    Row(Modifier
        .height(60.dp)
        .requiredHeightIn(60.dp, 60.dp)
        .fillMaxWidth()
        .background(Color.DarkGray)
    ){

        Text("Results"
            , fontSize = MyConstants.bottomBarFontSize
            , color = Color.White
            , modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp )
                .weight(1f)  // weight will push the "Clear" text below all the way to the right.
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {  // toggle showResults boolean with each click, raising and lowering the results screen.
                            if (currentState.showResultsWindow){
                                currentState.showResultsWindow = false
                            } else {
                                triggerResults = true
                            }
                        }
                    )
                 }
        )
        Text("Clear"
            , fontSize = MyConstants.bottomBarFontSize
            , color = Color.White
            , modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            currentState.clearGraph = true
                        }
                    )
                }
        )

        if (currentState.resultsWindowState.isMinimized) {
            currentState.showResultsWindow = false
        }
    }

    if (triggerResults){
        showResults()
        triggerResults = false
    }
}

/*
This composable covers the entire window with the visible UI.  It also handles
raising and lowering the results screen.  The main us is sidebar on the left
with a drawing area on the right.  There is also a bar across the bottom.
 */
@Composable
fun windowBox() {

    val state = LocalStateInfo.current

     Column(                 // Contains a draggable on top followed by the results screen
         modifier = Modifier // followed by the bottom bar. The results screen is not always visible.
             .fillMaxSize()
             .background(Color.Yellow)
     ) {

         draggable(  // dragTargets can be dragged over anything in this scope.  Contains one row.
             Modifier
                 .background(color = Color.Gray)
                 .fillMaxWidth()
         ) {
             Row(  // Sidebar on the left and drawing area on the right.
                 Modifier
                     //.fillMaxSize()
                     .background(color = Color.Red)
             ) {
                 sideBar()
                 dropTarget(
                     modifier = Modifier.background(color = MyConstants.graphBackground)
                         .fillMaxSize()
                 )
             }
         }
         bottomBar()
     }
}

@Composable
fun mainWindow() {

    val currentState = LocalStateInfo.current

    var open by remember { mutableStateOf(false) }
    var saveAs by remember { mutableStateOf(false) }
    var save by  remember {mutableStateOf(false)}
    val fileOptions = arrayListOf<String>("Open", "Save", "Save As")
    var processAfterSaveAction by remember { mutableStateOf(false) }
    var buildGraph by remember { mutableStateOf(false) }
    var startUp by remember { mutableStateOf(true) }
    var expandMenu by remember { mutableStateOf(false) }
    lateinit var  bondGraphData: String
    //val state = remember { StateInfo() }
    val pathToDataFile = getDataFilePath()



    //var xx by remember{ mutableStateOf(() -> unit = {})}
    if (runTestCases) testCases()


    @Composable
    fun myMenuBar() {

        //val currentState = LocalStateInfo.current
        val options = arrayListOf("Intermediate results")
        val startStates = arrayListOf(displayIntermediateResults)

        Row(
            Modifier
                .height(35.dp)
                .requiredHeightIn(35.dp, 35.dp)
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {


            Column (modifier = Modifier
                .fillMaxHeight()
                , verticalArrangement = Arrangement.Center

            ) {
                dropDownSelectionBox(items = fileOptions, title = "Files", fontSize = MyConstants.menuBarFontSize, titleBackgroundColor = Color.LightGray, width = 60.dp, spacing = 6.dp) {
                    when (it) {
                        0 -> open = true
                        1 -> save = true
                        2 -> saveAs = true
                    }
                }
            }

            Text("Values"
                , fontSize = MyConstants.menuBarFontSize
                , textAlign = TextAlign.Center
                , modifier = Modifier
                    .clickable {
                        currentState.showValuesWindow = true
                        currentState.valuesWindowState.isMinimized = false
                        currentState.valuesWindowOnTop = true
                        currentState.valuesWindowOnTop = false
                        println("Values clicked") }
                    //.padding(horizontal = 12.dp)
                    .align(Alignment.CenterVertically)
            )

            Column (modifier = Modifier
                .fillMaxHeight()
                , verticalArrangement = Arrangement.Center

            ) {
                dropDownCheckboxGroup(
                    labels = options,
                    startStates = startStates,
                    title = "Options",
                    fontSize = MyConstants.menuBarFontSize,
                    titleBackgroundColor = Color.LightGray,
                    //dropDownBackgroundColor = Color.LightGray
                ) { id, state ->
                    displayIntermediateResults = state
                }
            }

            Text(text = pathToBondGraphFile?.toString() ?: ""
                , textAlign = TextAlign.Center
                , fontSize = MyConstants.menuBarFontSize
                , color = Color.Blue
                , modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth()

            )
        }
    }



    Window(
        onCloseRequest = {currentState.exit = true}
        ,state = WindowState(width = 1200.dp, height = 800.dp)
        //, icon = (painterResource("one_by_one_pixel.jpg"))

        ,onKeyEvent = {
            if (it.isShiftPressed) {
                isShifted = true
            } else {
                isShifted = false
            }
            false
        }
    ) {

        Column {

            myMenuBar()

            windowBox()

            @Composable
            fun processSaveAsDialog() {
                fileDialog("Test String", false) {
                    saveAs = false
                    save = false
                    if (it != null) {
                        if (!pathToDataFile.exists()) {
                            Files.createFile(pathToDataFile)
                        }
                        pathToDataFile.writeText(it.pathString)
                        it.writeText(bondGraph.toSerializedStrings())
                        pathToBondGraphFile = it
                        bondGraph.graphHasChanged = false
                        processAfterSaveAction = true
                    }
                }
            }

            @Composable
            fun processSave() {
                //save = false
                if (pathToBondGraphFile == null) {
                    processSaveAsDialog()
                } else {
                    pathToBondGraphFile?.writeText(bondGraph.toSerializedStrings())
                    bondGraph.graphHasChanged = false
                    currentState.afterSaveAction?.invoke()
                    save = false
                }
            }

            if (open) {
                if (bondGraph.graphHasChanged) {
                    currentState.afterSaveAction = { open = true }
                    currentState.showSaveFileDialog = true
                    open = false
                } else {
                    fileDialog("Test String", true) {
                        @Composable
                        open = false
                        if (it != null) {
                            if (!pathToDataFile.exists()) {
                                Files.createFile(pathToDataFile)
                            }
                            pathToDataFile.writeText(it.pathString)
                            bondGraphData = it.readText()
                            buildGraph = true
                            pathToBondGraphFile = it
                        }

                    }
                }
            }

            if (saveAs) {
                processSaveAsDialog()
            }

            if (save) {
                processSave()
            }

            if (processAfterSaveAction) {
                currentState.afterSaveAction?.invoke()
                processAfterSaveAction = false
            }



            if (buildGraph) {
                buildGraph = false
                bondGraph.fromSerializedStrings(bondGraphData)
            }

            if (startUp) {
                startUp = false
                if (pathToDataFile.exists()) {
                    val pathString = pathToDataFile.readText()
                    val path = Paths.get(pathString)
                    if (path.exists()) {
                        val data = path.readText()
                        pathToBondGraphFile = path
                        bondGraph.fromSerializedStrings(data)
                        bondGraph.graphHasChanged = false
                    }
                }
            }

            if (currentState.clearGraph) { // Clear the work area of the current bond graph drawing.
                if (bondGraph.graphHasChanged) {
                    currentState.afterSaveAction = {
                        bondGraph.clear()
                        pathToBondGraphFile = null
                    }
                    currentState.showSaveFileDialog = true

                    currentState.clearGraph = false
                } else {
                    bondGraph.clear()
                    pathToBondGraphFile = null
                    currentState.clearGraph = false
                }
            }

            if (currentState.showSaveFileDialog) {

                saveDialog(
                    message = "Save changes to this bond graph?",
                    onSave = {
                        save = true
                        currentState.showSaveFileDialog = false
                    },
                    onSaveAs = {
                        saveAs = true
                        currentState.showSaveFileDialog = false
                    },
                    onDontSave = {
                        bondGraph.graphHasChanged = false
                        currentState.showSaveFileDialog = false
                        processAfterSaveAction = true
                    },
                    onCancel = {
                        currentState.clearGraph = false
                        currentState.showSaveFileDialog = false
                    },
                    onCloseRequest = {
                        currentState.clearGraph = false
                        currentState.showSaveFileDialog = false
                    }
                )
            }
        }
    }
}