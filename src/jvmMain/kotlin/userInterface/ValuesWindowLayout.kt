package userInterface

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import bondgraph.Operation.*
import bondgraph.PowerVar.*
import userInterface.SaveOptions.*
import bondgraph.*
@Composable
fun processSaveAs (description: String, finishAction: () -> Unit) {
    val currentState = LocalStateInfo.current
    val id = bondGraph.getNextValueSetId()
    bondGraph.valuesSetsMap[id] = bondGraph.valuesSetWorkingCopy!!.copy(id, description)
    currentState.selectedSetId = id
    bondGraph.loadValuesSetIntoWorkingCopy(currentState.selectedSetId)
    currentState.valuesSetCopy = bondGraph.valuesSetWorkingCopy
    bondGraph.valuesSetHasChanged = false
    bondGraph.graphHasChanged = true
    finishAction.invoke()
}

@Composable
fun saveFunction(finishAction: () -> Unit) {
    val currentState = LocalStateInfo.current
    bondGraph.valuesSetsMap[currentState.selectedSetId] = bondGraph.valuesSetWorkingCopy!!
    //bondGraph.valuesSetsMap[currentState.selectedSetId] = bondGraph.valueSetWorkingCopy!!
    bondGraph.valuesSetHasChanged = false
    bondGraph.graphHasChanged = true
    finishAction.invoke()
}

@Composable
fun saveAsFunction(finishAction: () -> Unit){

    val currentState = LocalStateInfo.current
    var newText by remember { mutableStateOf("") }
    var onSubmit by remember { mutableStateOf(false) }
    var onCloseRequest by remember { mutableStateOf(false) }
    var showEnterTextDialog by remember { mutableStateOf(false) }

    if (bondGraph.valuesSetWorkingCopy!!.description == bondGraph.valuesSetsMap[currentState.selectedSetId]!!.description) {
        showEnterTextDialog = true
    } else {
        processSaveAs(bondGraph.valuesSetWorkingCopy!!.description, finishAction)
    }

    if (showEnterTextDialog){
        enterTextDialog(
            message = "Enter New Description"
            ,currentText = bondGraph.valuesSetWorkingCopy!!.description
            ,onSubmit = {
                newText = it
                showEnterTextDialog = false
                onSubmit = true
            }
            , onCloseRequest = {
                showEnterTextDialog = false
                onCloseRequest = true
            }
        )
    }

    if (onSubmit) {
        onSubmit = false
        processSaveAs(newText, finishAction)
    }

    if (onCloseRequest){
        onCloseRequest = false
        processSaveAs(bondGraph.valuesSetWorkingCopy!!.description, finishAction)
    }
}
@Composable
fun deleteFunction(finishAction: () -> Unit){

    val currentState = LocalStateInfo.current
    var showAlert by remember { mutableStateOf(false) }



    val valuesSet = bondGraph.valuesSetsMap[currentState.selectedSetId]
    if (valuesSet!!.onePortValues.isEmpty() and valuesSet.twoPortValues.isEmpty()) {
        showAlert = true
    } else {
        val valuesSetList = arrayListOf<ValuesSet>()
        bondGraph.valuesSetsMap.values.forEach { valuesSetList.add(it) }
        var nextId = valuesSetList[0].id
        for (index in 1 until valuesSetList.size){
            if (valuesSetList[index].id == currentState.selectedSetId) break
            nextId = valuesSetList[index].id
        }
        bondGraph.valuesSetsMap.remove(currentState.selectedSetId)
        currentState.selectedSetId = nextId
        bondGraph.loadValuesSetIntoWorkingCopy(currentState.selectedSetId)
        currentState.valuesSetCopy = bondGraph.valuesSetWorkingCopy
        bondGraph.graphHasChanged = true
        bondGraph.valuesSetHasChanged = false
        finishAction.invoke()
    }

    if (showAlert){
        oneButtonAlertDialog("Can't delete the set with no values", "OK"
            , {
                showAlert = false
                finishAction.invoke()
              }
            , {showAlert = false
                finishAction.invoke()
            })
    }


}


@Composable
fun setsBar (){

    val currentState = LocalStateInfo.current

    Column(modifier = Modifier
        .background(Color.Red)
        .fillMaxWidth()
    ) {
        Divider(
            thickness = 1.dp, color = Color.Black
        )

        Row(
            modifier = Modifier
                .height(MyConstants.valuesRowHeight)
                .background(MyConstants.valuesBarsColor)
                .fillMaxWidth()

        ) {

            Text(
                "Value Sets",
                fontSize = MyConstants.valuesBarFontSize,
                textAlign = TextAlign.Left,
                color = MyConstants.valuesBarsTextColor,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )

            Text("+",
                fontSize = MyConstants.plusSignFontSize,
                textAlign = TextAlign.Center,
                color = MyConstants.valuesBarsTextColor,
                modifier = Modifier
                    .clickable {
                        currentState.selectedSetId = bondGraph.createValueSet()
                        bondGraph.loadValuesSetIntoWorkingCopy(currentState.selectedSetId)
                        currentState.valuesSetCopy = bondGraph.valuesSetWorkingCopy
                        //bondGraph.valuesSetHasChanged = true
                    }
                    .padding(horizontal = 12.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Divider(
            thickness = 1.dp, color = Color.Black
        )
    }
}

@Composable
fun setColumn () {

    val currentState = LocalStateInfo.current

    Column(
        modifier = Modifier
            .background(Color.DarkGray)
            .width(MyConstants.setColumnWidth)
            .fillMaxHeight()


    ) {

        setsBar()

        LazyColumn(
            modifier = Modifier
                .padding(MyConstants.valuesGeneralPadding)
                .background(Color.DarkGray)
            , verticalArrangement = Arrangement.spacedBy(MyConstants.valuesGeneralPadding)
        ) {
            bondGraph.valuesSetsMap.values.forEach {
                item { setItem(it)  }
            }
        }
    }
}

@Composable
fun setItem(valuesSet: ValuesSet) {

    val currentState = LocalStateInfo.current
    var itemClicked by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var saveOption:SaveOptions? by remember { mutableStateOf(null) }
    var cancel by remember { mutableStateOf(false) }

    fun loadValuesSet () {
        currentState.selectedSetId = valuesSet.id
        bondGraph.loadValuesSetIntoWorkingCopy(currentState.selectedSetId)
        currentState.valuesSetCopy = bondGraph.valuesSetWorkingCopy
        bondGraph.valuesSetHasChanged = false
    }


    if (showDialog) {
        saveDialog(
              message = "Save this Values Set?"
            , onSave = { saveOption = SAVE }
            , onSaveAs = { saveOption = SAVE_AS }
            , onDontSave = { saveOption = DONT_SAVE }
            , onCancel = { saveOption = CANCEL }
            , onCloseRequest = {
                showDialog = false
            }
        )
    }

    if (itemClicked){
        itemClicked = false
        if (bondGraph.valuesSetHasChanged){
            showDialog = true
        } else {
            loadValuesSet()
        }
    }

    if (saveOption != null) {
        when(saveOption){

            SAVE -> saveFunction {
                loadValuesSet()
                showDialog = false
                saveOption = null
            }

            SAVE_AS ->  saveAsFunction {
                    loadValuesSet()
                    showDialog = false
                saveOption = null
                }

            DONT_SAVE -> {
                loadValuesSet()
                showDialog = false
                saveOption = null
            }

            CANCEL -> {
                showDialog = false
                saveOption = null
            }

            null -> {}
        }


    }

    Box(modifier = Modifier
        .border(BorderStroke(width = 1.dp, color = Color.Black))
        .background(color = if (currentState.selectedSetId == valuesSet.id) MyConstants.setSelectedColor else MyConstants.setDefaultColor)
        .fillMaxWidth()
        .clickable {
           itemClicked = true
        }


    ) {
        Text(valuesSet.description //if (currentState.selectedSetId == valuesSet.id) currentState.setDescription else valuesSet.description
            , modifier = Modifier
                .fillMaxWidth()
                .padding(MyConstants.valuesGeneralPadding)
        )


    }
}

@Composable
fun valuesBar () {

    val currentState = LocalStateInfo.current
    var save by remember { mutableStateOf(false) }
    var saveAs by remember { mutableStateOf(false) }
    var delete by remember { mutableStateOf(false) }
    var showEnterTextDialog by remember { mutableStateOf(false) }
    var showAlert by remember {mutableStateOf(false)}







    Column( modifier = Modifier
        .fillMaxWidth()

    ) {

        Divider(
            thickness = 1.dp, color = Color.Black
        )

        Row(
            modifier = Modifier
                .height(MyConstants.valuesRowHeight)
                .background(MyConstants.valuesBarsColor)
                .fillMaxWidth()

        ) {
            Column(

            ) {

                Text(
                    currentState.setDescription,
                    textAlign = TextAlign.Center,
                    fontSize = MyConstants.valuesBarFontSize,
                    color = MyConstants.valuesBarsTextColor,
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 2.dp)
                )

                Row(
                ) {

                    Text("Save",
                        fontSize = MyConstants.valuesBarFontSizeSmall,
                        color = MyConstants.valuesBarsTextColor,
                        modifier = Modifier
                            .clickable {
                               save = true
                            }
                            .padding(horizontal = 14.dp)
                    )

                    Text("Save As",
                        fontSize = MyConstants.valuesBarFontSizeSmall,
                        color = MyConstants.valuesBarsTextColor,
                        modifier = Modifier
                            .clickable { saveAs = true }
                            .padding(horizontal = 12.dp)
                    )

                    Text("Delete",
                        fontSize = MyConstants.valuesBarFontSizeSmall,
                        color = MyConstants.valuesBarsTextColor,
                        modifier = Modifier
                            .clickable { delete = true }
                            .padding(horizontal = 12.dp)
                    )
                }

            }
        }

        Divider(
            thickness = 1.dp, color = Color.Black
        )
    }

    if (save) {
        save = false
        saveFunction(){}
    }

    if (saveAs) {
        saveAsFunction(){saveAs = false}
    }

    if (delete){
        deleteFunction(){delete = false}
    }
}

@Composable
fun setDescriptionBar(valuesSet: ValuesSet){
    val currentState = LocalStateInfo.current
    var description by remember(valuesSet.description) { mutableStateOf(valuesSet.description) }
    Column (modifier = Modifier
        .fillMaxWidth()
        .background(MyConstants.mediumGray)
        .padding( 6.dp)

    ) {
        Row(
            modifier = Modifier
                .height(MyConstants.valuesRowHeight)
                .background(MyConstants.myWhite)
                .fillMaxWidth()
                .border(BorderStroke(width = 1.dp, Color.Black))
                , verticalAlignment = Alignment.CenterVertically

        ) {
            Text(
                "Description:", fontSize = MyConstants.valuesBarFontSize, modifier = Modifier
                    .padding(horizontal = 6.dp)

            )

            Column(

            ) {
                BasicTextField(modifier = Modifier
                    .fillMaxWidth()
                    , value = description
                    , onValueChange = {newText ->
                        description = buildString {
                            newText.forEach {
                                if ( ! (it == '\t' || it == '\n')) append (it)
                            }
                        }
                        valuesSet.description = description
                        currentState.setDescription = description
                        bondGraph.valuesSetHasChanged = true
                    }
                )

                Divider(modifier = Modifier
                    .padding(end = 6.dp)
                    , thickness = 1.dp
                    , color = Color.Black
                )
            }

        }


    }
}

@Composable
fun valuesColumn(valuesSet: ValuesSet) {
    var description by remember(valuesSet.description) { mutableStateOf(valuesSet.description) }
    val currentState = LocalStateInfo.current
    val scrollState = rememberScrollState()
    val eList = arrayListOf<Element>()

    currentState.setDescription = description
    Column(modifier = Modifier
        .background(Color.DarkGray)
        .width(MyConstants.valuesColumnWidth)
        .fillMaxHeight()
    ) {

        valuesBar()

        setDescriptionBar(valuesSet)

        Box( modifier = Modifier
                .fillMaxSize()
                .background(MyConstants.mediumGray)
                .padding(start = 1.dp, end = 12.dp, top = 6.dp, bottom = 32.dp) // big bottom margin to leave room for drop down menu in twoPortItem composable

        ) {


            /*
                Used regular column because I couldn't get a lazyColumn to work with my desired focus switching and
                the scrolling.  Switching focus to a component not currently visible is difficult because the component
                isn't composed yet.  Switching focus during composition cause a crash.  With a regular column all
                the components are already composed even if they are not visible.
            */
            Column(
                modifier = Modifier
                    // Can't use vertical padding or Arrangement.spacedBy on LazyColumn because it messes up the scrollbar.
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {

                val focusRequesterList = arrayListOf<FocusRequester>()
                //val onePortValueDataList = bondGraph.getOnePortValueDataList(currentState.selectedSetId)
                //val twoPortValueDataList = bondGraph.getTwoPortValueDataList(currentState.selectedSetId)
                val onePortValueDataList = arrayListOf<OnePortValueData>()
                val twoPortValueDataList = arrayListOf<TwoPortValueData>()
                valuesSet.onePortValues.values.forEach { onePortValueDataList.add(it)}
                valuesSet.twoPortValues.values.forEach { twoPortValueDataList.add(it) }
                if (onePortValueDataList.size + twoPortValueDataList.size > 0) {
                    for (index in 0 until onePortValueDataList.size + twoPortValueDataList.size) {
                        focusRequesterList.add(FocusRequester())
                    }
                    focusRequesterList.add(focusRequesterList[0])

                    var count = 0
                    onePortValueDataList.forEach {
                        onePortItem(it, focusRequesterList[count], focusRequesterList[count + 1], count == 0)
                        count++
                    }

                    twoPortValueDataList.forEach {
                        twoPortItem(it, focusRequesterList[count], focusRequesterList[count + 1])
                        count++
                    }
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
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun onePortItem(onePortValueData: OnePortValueData, valueFocusRequester: FocusRequester, nextItemFocusRequester: FocusRequester, initialFocus: Boolean){

    var valueInput by remember(onePortValueData.value) { mutableStateOf(if (onePortValueData.value == null) "" else onePortValueData.value.toString()) }
    var unitsInput by remember(onePortValueData.units) { mutableStateOf(if (onePortValueData.units == null) "" else onePortValueData.units) }
    var descriptionInput by remember(onePortValueData.description) { mutableStateOf(if (onePortValueData.description == null) "" else onePortValueData.description) }
    var initialFocus = initialFocus
    val focusManager = LocalFocusManager.current
    Box (modifier = Modifier
        .padding(6.dp)

    ) {
        Box(
            modifier = Modifier
                .background(Color.LightGray)

        ) {
            Row(
                modifier = Modifier
                    .border(BorderStroke(width = 1.dp, Color.Black))
                    .background(Color.LightGray),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically


            ) {

                Text(
                    onePortValueData.element.displayId,
                    modifier = Modifier
                        .width(MyConstants.diplayNameWidth),
                    textAlign = TextAlign.Center,
                )

                Row(
                    modifier = Modifier
                        .background(MyConstants.myWhite)
                    , horizontalArrangement = Arrangement.spacedBy(6.dp)

                ) {

                    Column(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .absolutePadding(left = MyConstants.valuesGeneralPadding),
                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Text(
                            "Value",
                            modifier = Modifier
                                .padding(vertical = 6.dp),
                            textAlign = TextAlign.Center,
                            fontSize = MyConstants.valuesFontSize
                        )
                        BasicTextField(modifier = Modifier
                            .width(MyConstants.valueColumnWidth)
                            .focusProperties {
                                down = nextItemFocusRequester
                            }
                            .focusRequester(valueFocusRequester)
                            .onKeyEvent {
                                if (it.key == Key.Tab) {
                                    focusManager.moveFocus(FocusDirection.Right)
                                }
                                if (it.key == Key.Enter) {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                                true
                            }
                            , value = valueInput
                            , onValueChange = { newText ->
                                var periodCount = 0
                                bondGraph.valuesSetHasChanged = true
                                valueInput = buildString {
                                    newText.forEach {
                                        when {
                                            it == '.' -> {
                                                if (periodCount++ == 0) {
                                                    append(it)
                                                }
                                            }

                                            it == '0' -> {
                                                if ((length == 1 && get(0) != '0') || length != 1) {
                                                    append(it)
                                                }
                                            }

                                            it.isDigit() -> {
                                                if (length == 1 && get(0) == '0') {
                                                    deleteAt(0)
                                                }
                                                append(it)
                                            }
                                        }
                                    }
                                }
                                if (valueInput != "") {
                                    onePortValueData.value = valueInput.toDouble()
                                }
                            }
                        )
                        Divider(
                            thickness = 1.dp,
                            color = Color.Black,
                            modifier = Modifier.width(MyConstants.valueColumnWidth).padding(bottom = 12.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(top = 6.dp), horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Text(
                            "Units", modifier = Modifier
                                .padding(vertical = 6.dp), fontSize = MyConstants.valuesFontSize
                        )

                        BasicTextField(modifier = Modifier
                            .width(MyConstants.unitsColumnWidth)
                            .focusProperties {
                                down = nextItemFocusRequester
                            }

                            .onKeyEvent {
                                if (it.key == Key.Tab) {
                                    focusManager.moveFocus(FocusDirection.Right)
                                }
                                if (it.key == Key.Enter) {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                                true
                            }
                            , value = unitsInput
                            , onValueChange = { newText ->
                                bondGraph.valuesSetHasChanged = true
                                unitsInput = buildString {
                                    newText.forEach {
                                        if ( ! (it == '\t' || it == '\n')) append(it)
                                    }
                                }
                                onePortValueData.units = unitsInput
                            }
                        )
                        Divider(
                            thickness = 1.dp,
                            color = Color.Black,
                            modifier = Modifier.width(MyConstants.unitsColumnWidth)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(top = 6.dp), horizontalAlignment = Alignment.CenterHorizontally

                    ) {
                        Text(
                            "Description",
                            modifier = Modifier
                                .padding(vertical = 6.dp),
                            textAlign = TextAlign.Center,
                            fontSize = MyConstants.valuesFontSize
                        )

                        BasicTextField(modifier = Modifier
                            .fillMaxWidth()
                            .focusProperties {
                                right = valueFocusRequester
                                down = nextItemFocusRequester
                            }
                            .onKeyEvent {
                                if (it.key == Key.Tab) {
                                    focusManager.moveFocus(FocusDirection.Right)
                                }
                                if (it.key == Key.Enter) {
                                    focusManager.moveFocus(FocusDirection.Down)
                                }
                                true
                            }
                            ,value = descriptionInput
                            ,onValueChange = { newText ->
                                bondGraph.valuesSetHasChanged = true
                                descriptionInput = buildString {
                                    newText.forEach {
                                    if ( ! (it == '\t' || it == '\n')) append(it)
                                    }
                                }
                                onePortValueData.description = descriptionInput
                            }
                        )
                        Divider(
                            thickness = 1.dp,
                            color = Color.Black,
                            modifier = Modifier.absolutePadding(right = MyConstants.valuesGeneralPadding)
                        )
                    }
                }


            }

        }
    }
}
@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun twoPortItem(twoPortValueData: TwoPortValueData, valueFocusRequester: FocusRequester, nextItemFocusRequester: FocusRequester){
    var valueInput by remember { mutableStateOf(if (twoPortValueData.value == null) "" else twoPortValueData.value.toString()) }
    var unitsInput by remember { mutableStateOf(if (twoPortValueData.units == null) "" else twoPortValueData.units) }
    var descriptionInput by remember { mutableStateOf(if (twoPortValueData.description == null) "" else twoPortValueData.description) }
    val unitsFocusRequester = FocusRequester()
    val descriptionFocusRequester = FocusRequester()
    val operationStrings = arrayListOf(MULTIPLY.toString(), DIVIDE.toString())
    var operationsIndex by remember { mutableStateOf(if (twoPortValueData.operation == MULTIPLY) 0 else 1) }
    val focusManager = LocalFocusManager.current
    val bondPair = if (twoPortValueData.bond1.displayId < twoPortValueData.bond2.displayId) Pair(twoPortValueData.bond1, twoPortValueData.bond2) else Pair(twoPortValueData.bond2, twoPortValueData.bond1)
    val powerVars = arrayListOf("effort - " + bondPair.first.displayId, "effort - " + bondPair.second.displayId, "flow - " + bondPair.first.displayId, "flow - "  + bondPair.second.displayId)
    val currentPowerVarString = twoPortValueData.powerVar1.toString() + " - " + twoPortValueData.bond1.displayId
    var powerVarsIndex by remember { mutableStateOf(powerVars.indexOf(currentPowerVarString)) }
    val indexMap = if (twoPortValueData.element is Transformer)
        mapOf<Int, Int>(0 to 1, 1 to 0, 2 to 3, 3 to 2 ) else mapOf(0 to 3, 1 to 2, 2 to 1, 3 to 0)


    Box (modifier = Modifier
        .padding(6.dp)

    ) {
        Box(
            modifier = Modifier
                .background(Color.LightGray)

        ) {
            Row(
                modifier = Modifier
                    .border(BorderStroke(width = 1.dp, Color.Black))
                    .background(Color.LightGray),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically


            ) {

                Text(
                    twoPortValueData.element.displayId,
                    modifier = Modifier
                        .width(MyConstants.diplayNameWidth),
                    textAlign = TextAlign.Center,
                )

                Column(
                    modifier = Modifier
                        .background(MyConstants.myWhite)

                ) {
                    Row(
                        modifier = Modifier
                            .background(MyConstants.myWhite)
                            .padding(MyConstants.valuesGeneralPadding)
                            .fillMaxWidth()
                        , horizontalArrangement = Arrangement.spacedBy(6.dp)

                    ) {

                        dropDownSelectionBox(items = operationStrings, startIndex = operationsIndex) {
                            bondGraph.valuesSetHasChanged = true
                            operationsIndex = it
                            twoPortValueData.operation = Operation.toEnum(operationStrings[it])
                        }

                        dropDownSelectionBox(items = powerVars, startIndex = powerVarsIndex) {
                            bondGraph.valuesSetHasChanged = true
                            powerVarsIndex = it
                            if (it == 0 || it == 2){
                                twoPortValueData.bond1 = bondPair.first
                                twoPortValueData.bond2 = bondPair.second
                            } else {
                                twoPortValueData.bond1 = bondPair.second
                                twoPortValueData.bond2 = bondPair.first
                            }

                            if (it == 0 || it == 1) {
                                twoPortValueData.powerVar1 = EFFORT
                            } else {
                                twoPortValueData.powerVar1 = FLOW
                            }

                            if (indexMap[it] == 0 || indexMap[it] == 1) {
                                twoPortValueData.powerVar2 = EFFORT
                            } else {
                                twoPortValueData.powerVar2 = FLOW
                            }
                        }

                        Text(
                            "by", modifier = Modifier
                                .padding(top = 3.dp)
                        )

                        Column(

                        ) {
                            BasicTextField(modifier = Modifier
                                .width(MyConstants.valueColumnWidth)
                                .padding(top = 3.dp)
                                .focusProperties {
                                    right = unitsFocusRequester
                                    down = nextItemFocusRequester
                                }
                                .focusRequester(valueFocusRequester)
                                .onKeyEvent {
                                    if (it.key == Key.Tab) {
                                        focusManager.moveFocus(FocusDirection.Right)
                                    }
                                    if (it.key == Key.Enter) {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                    true
                                }
                                , value = valueInput
                                , onValueChange = { newText ->
                                    bondGraph.valuesSetHasChanged = true
                                    var periodCount = 0
                                    valueInput = buildString {
                                        newText.forEach {
                                            when {
                                                it == '.' -> {
                                                    if (periodCount++ == 0) {
                                                        append(it)
                                                    }
                                                }

                                                it == '0' -> {
                                                    if ((length == 1 && get(0) != '0') || length != 1) {
                                                        append(it)
                                                    }
                                                }

                                                it.isDigit() -> {
                                                    if (length == 1 && get(0) == '0') {
                                                        deleteAt(0)
                                                    }
                                                    append(it)
                                                }
                                            }
                                        }
                                    }
                                if (valueInput != "") {
                                    twoPortValueData.value = valueInput.toDouble()
                                }
                            }
                            )
                            Divider(
                                thickness = 1.dp,
                                color = Color.Black,
                                modifier = Modifier
                                    .width(MyConstants.valueColumnWidth)
                                    .padding(bottom = 12.dp)
                            )
                        }
                        Text(
                            " to get " + powerVars[indexMap[powerVarsIndex]!!],
                            modifier = Modifier
                                .padding(top = 3.dp)
                        )
                    }
                    Row(modifier = Modifier
                        .background(MyConstants.myWhite)

                    ) {
                        Text(
                            "Units", modifier = Modifier
                                .padding(horizontal = 6.dp)
                            , fontSize = MyConstants.valuesFontSize
                        )
                        Column(

                        ) {
                            BasicTextField(modifier = Modifier
                                .width(MyConstants.unitsColumnWidth)
                                .focusProperties {
                                    down = nextItemFocusRequester
                                }
                                .focusRequester(unitsFocusRequester)
                                .onKeyEvent {
                                    if (it.key == Key.Tab) {
                                        focusManager.moveFocus(FocusDirection.Right)
                                    }
                                    if (it.key == Key.Enter) {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                    true
                                }
                                , value = unitsInput
                                , onValueChange = { newText ->
                                    bondGraph.valuesSetHasChanged = true
                                    unitsInput = buildString {
                                        newText.forEach {
                                            if (!(it == '\t' || it == '\n')) append(it)
                                        }
                                    }
                                    twoPortValueData.units = unitsInput
                                }
                            )
                            Divider(
                                thickness = 1.dp,
                                color = Color.Black,
                                modifier = Modifier
                                    .width(MyConstants.unitsColumnWidth)
                                    .padding(bottom = 12.dp)
                            )
                        }

                        Text(
                            "Description",
                            modifier = Modifier
                                .padding(horizontal = 6.dp),
                            textAlign = TextAlign.Center,
                            fontSize = MyConstants.valuesFontSize
                        )

                        Column(

                        ) {
                            BasicTextField(modifier = Modifier
                                .fillMaxWidth()
                                .focusProperties {
                                    right = valueFocusRequester
                                    down = nextItemFocusRequester
                                }
                                .focusRequester(descriptionFocusRequester)
                                .onKeyEvent {
                                    if (it.key == Key.Tab) {
                                        focusManager.moveFocus(FocusDirection.Right)
                                    }
                                    if (it.key == Key.Enter) {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                    true
                                }
                                , value = descriptionInput
                                , onValueChange = { newText ->
                                    bondGraph.valuesSetHasChanged = true
                                    descriptionInput = buildString {
                                        newText.forEach {
                                            if (!(it == '\t' || it == '\n')) append(it)
                                        }
                                    }
                                    twoPortValueData.description = descriptionInput
                                }
                            )
                            Divider(
                                thickness = 1.dp,
                                color = Color.Black,
                                modifier = Modifier.absolutePadding(right = MyConstants.valuesGeneralPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun valuesWindow() {
    var closeRequest by remember { mutableStateOf(false) }
    var saveOption:SaveOptions? by remember { mutableStateOf(null) }
    //var showDialog by remember { mutableStateOf(false) }

    val currentState = LocalStateInfo.current
    //var valuesSetCopy by remember(bondGraph.valueSetWorkingCopy) { mutableStateOf( bondGraph.valueSetWorkingCopy) }
    Window(
        //onCloseRequest = {currentState.showValuesWindow = false}
        onCloseRequest = {closeRequest = true}
        ,state = currentState.valuesWindowState
        , alwaysOnTop = currentState.valuesWindowOnTop
        //,state = rememberWindowState(width = Dp.Unspecified),
    ) {


        Box  {

            Row (modifier = Modifier
                .wrapContentWidth()
                .fillMaxHeight()
                .background(Color.LightGray)

            ) {

                setColumn()

                Divider(
                      thickness = 1.dp
                    , color = Color.White
                    , modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)

                )

                currentState.valuesSetCopy?.let {
                    valuesColumn(it!!)
                }
            }
        }

        if (closeRequest) {
            closeRequest = false
            if (bondGraph.valuesSetHasChanged) {
                currentState.showSaveValuesSetDialog = true
            } else {
                currentState.showValuesWindow = false
            }
        }

        if (currentState.showSaveValuesSetDialog) {
            saveDialog(
                  message = "Save this Values Set?"
                , onSave = {saveOption = SAVE}
                , onSaveAs = {saveOption = SAVE_AS}
                , onDontSave = {saveOption = DONT_SAVE}
                , onCancel = {saveOption = CANCEL}
                , onCloseRequest = {
                    currentState.showSaveValuesSetDialog = false
                    currentState.showValuesWindow = false
                }
            )
        }

        if (saveOption != null){
            when (saveOption){

                SAVE -> saveFunction {
                    currentState.showValuesWindow = false
                    saveOption = null
                }

                SAVE_AS -> saveAsFunction {
                    currentState.showValuesWindow = false
                    saveOption = null
                }

                DONT_SAVE -> {
                    bondGraph.loadValuesSetIntoWorkingCopy(currentState.selectedSetId)
                    currentState.valuesSetCopy = bondGraph.valuesSetWorkingCopy
                    bondGraph.valuesSetHasChanged = false
                    currentState.showSaveValuesSetDialog = false
                    currentState.showValuesWindow = false
                    saveOption = null
                }

                CANCEL -> {saveOption = null}

                null -> {}
            }
            currentState.showSaveValuesSetDialog = false
        }
    }
}