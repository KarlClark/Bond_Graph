package userInterface
import algebra.Token
import algebra.Number
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.*
import algebra.*
import algebra.operations.multiply_f
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.Dp
import bondgraph.AlgebraException
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.modules.EmptySerializersModule
import java.util.LinkedHashMap

val termHeight = 30.dp
val tokenHeight = termHeight.times(.8f)
val tokenFontSize = (tokenHeight.value).sp
val subscriptFontSize = tokenFontSize.times(.4f)
val superscriptFontSize = tokenFontSize.times(.5f)
val signFontSize = tokenFontSize.times(1)

val sourceSubscriptHeight = tokenHeight.times(.5f)
val sourceSubscriptFontSize = tokenFontSize.times(.8)
val subscriptBottomPadding = (tokenFontSize.value * .1).dp
val signPadding = termHeight.div(5f)
val minusSignBottomPadding = termHeight.times(.08f)

val token1 = Token("1", "", AnnotatedString("C"))
val token2 = Token("3", "", AnnotatedString("I"))
val token3 = Token("12", "", AnnotatedString("R"))
val token4 = Token("3", "", AnnotatedString("C"))
val token5 = Token("5", "", AnnotatedString("I"))
val token6= Token("1", "", AnnotatedString("R"))
val P = Token("2", "", AnnotatedString("p"), true)
val pDot = Token("2", "", AnnotatedString("p"), true, differential = true)
val Q = Token("3", "", AnnotatedString("q"), true)
val qDot = Token("3", "", AnnotatedString("q"), true, differential = true)
val Q2 = Token("10", "", AnnotatedString("q"), true)
val q2Dot = Token("10", "", AnnotatedString("q"), true, differential = true)


val Se = Token("4", "", AnnotatedString("Se"), true)
val Sf = Token("4", "", AnnotatedString("Sf"), true)

val term2 = token1.multiply(token2).divide(token3).multiply(token2)

val term1 = token1.multiply(token2).multiply(token3).multiply(token2).divide(token6).divide(token5).divide(token4)

val term3 = token5.multiply(token6).multiply(Q)
val term5 = token3.multiply(token1)

val tokens = arrayListOf<Any>(token1, token2, token3)

val nTokens = arrayListOf<Token>(token2, token1, token3)
val dTokens = arrayListOf<Token>(token1, token2, token3)

val tokenMap = linkedMapOf<Token, Int>(token1 to 1, token2 to 2, token3 to 1)

val sum1 = token1.subtract(token2)
val sum2 = term2.subtract(term5).subtract(token2)
val term6 = multiply_f(term5, sum1).divide(token6).divide(token5)
val term7 = Number(0.0).subtract( P.multiply(token1).multiply(Number(2.0)).divide(token2).divide(token3).multiply(token1)).add (Q.multiply(token2)).add(Se)
val term8 = term6.multiply(Q).add(P.multiply(token3)).add(Sf).subtract(Se)
val term9 = term2.multiply(Q2).add(token1.multiply(token2).divide(token3).multiply(P))
val map = hashMapOf<Token, Token>(pDot to P, qDot to Q)
val sum3 = term7.add(term3)
val eq1 = Equation(pDot, term7)
val eq2 = Equation(qDot, term3.add(Se))
val eq3 = Equation(q2Dot, term9.add(Sf))

val equations = arrayListOf<Equation>(eq1, eq2, eq3)

@Composable
fun runTest () {

    equations.forEach {eq ->  println("${eq.toAnnotatedString()}") }
    composeEquations(equations, map)
}

class TokenAndExponent(var token: Token, var exponent: Integer)

@Composable
fun composeEquations(equations: ArrayList<Equation>, dotTokenToTokenMap: Map<Token, Token>) {
    val tokenToIndexMap = linkedMapOf<Token, Int>()
    equations.forEachIndexed { index, eq ->
        val leftSide = eq.leftSide
        if (leftSide !is Token) {
            throw AlgebraException("composeEquations(equations) bad equation. Left side is not a Token. equations = ${eq.toAnnotatedString()}")
        }
        val token = dotTokenToTokenMap[leftSide]
        if (token != null) {
            tokenToIndexMap[token] = index
        }
    }
    tokenToIndexMap.forEach {key, value -> println(" key ${key.toAnnotatedString()} to value = $value")  }

    Column (Modifier
        .height(IntrinsicSize.Min)
        .width(IntrinsicSize.Min)
    ) {


        equations.forEach { eq ->
            Row (Modifier
                .height(IntrinsicSize.Min)
                .width(IntrinsicSize.Min)
                //.background(Color.Blue)
                , verticalAlignment = Alignment.CenterVertically

            ) {

                val rightSide = eq.rightSide

                composeToken(eq.leftSide as Token, "")
                composeSign("=")

                if (rightSide !is Sum) {
                    composeExpression(rightSide)
                   } else {

                    val expressions = arrayListOf<Pair<Expr?, Boolean>>()
                    repeat(equations.size){
                        expressions.add(Pair(null, false))
                    }

                    val sourceExpressions  = arrayListOf<Pair <Expr, Boolean>>()
                    println ("equations.size = ${userInterface.equations.size}  expressions.size = ${expressions.size}")

                    rightSide.plusTerms.forEach{term ->
                        if (isStateVariableExpr(term)){
                            val index = tokenToIndexMap[getTokenFromStateExpression(term)]
                            println("index = $index")
                            if (index == null){
                                sourceExpressions.add(Pair(term, false))
                            } else {
                                expressions[index]= Pair(term, false)
                            }
                        }
                    }

                    rightSide.minusTerms.forEach{term ->
                        if (isStateVariableExpr(term)){
                            val index = tokenToIndexMap[getTokenFromStateExpression(term)]
                            if (index == null){
                                sourceExpressions.add(Pair(term, true))
                            } else {
                                expressions[index]= Pair(term, true)
                            }
                        }
                    }

                    expressions.forEachIndexed { index, pair ->

                        if (pair.first != null) {
                            if (pair.second) {
                                composeSign("-")
                            } else {
                                if (index > 0) {
                                    composeSign("+")
                                }
                            }

                            composeExpression(pair.first!!)
                        }
                    }

                    sourceExpressions.forEach { pair ->
                        if (pair.second) {
                            composeSign("-")
                        } else {
                            composeSign("+")
                        }
                        composeExpression(pair.first)
                    }
                }
            }
        }
    }
}

fun loadTokenMap(list: ArrayList<Expr>): LinkedHashMap<Token, Int> {
    val map = linkedMapOf<Token, Int>()
    list.forEach { expr ->
        if (expr is Token) {
            if (map.contains(expr)) {
                var i = map[expr]
                if (i != null) {
                    i += 1
                    map[expr] = i
                }
            } else {
                map[expr] = 1
            }
        }
    }
    return map
}

@Composable
fun composeExpression(expr: Expr){
    when (expr) {
        is Token -> composeToken(expr, "")
        is Number -> composeNumber(expr)
        is Term -> composeStateTerm(expr)
        is Sum -> if (sumContainsStateExpressions(expr)) {
            composeSum(expr, false)
        }
            else {
                composeSum(expr, true)
            }
    }
}

@Composable
fun composeStateTerm(expr: Expr) {
    Row (Modifier
        .height(IntrinsicSize.Min)
        .width(IntrinsicSize.Min)
        //.background(Color.Blue)
        , verticalAlignment = Alignment.CenterVertically

    ) {
        var workingExpr: Expr
        var isStateTerm = false
        if (isStateVariableExpr(expr)) {
            workingExpr = getTermFromStateExpression(expr)
            isStateTerm = true
        } else {
            workingExpr = expr
        }

        println("workingExpr = ${workingExpr.toAnnotatedString()} isStateTerm = $isStateTerm")
        when (workingExpr) {
            is Token -> composeToken(workingExpr, "")
            is Number -> composeNumber(workingExpr)
            is Term -> composeTerm(workingExpr)
            is Sum -> composeSum(workingExpr, true)
        }

        Spacer(Modifier.width(5.dp))

        if (isStateTerm){
            composeToken(getTokenFromStateExpression(expr), "")
        }

    }
}

@Composable
fun composeSubscript(sourceString: String, numberString: String){
    if (sourceString == "") {
        BasicTextField(
            value = numberString, onValueChange = {}, textStyle = TextStyle(fontSize = subscriptFontSize)
        )
    } else {
        Row (Modifier
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Min)
            //.background(Color.Red)
            , verticalAlignment = Alignment.Bottom
        ) {
            BasicTextField(modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                //.background(Color.Yellow)
                ,value = sourceString, onValueChange = {}, textStyle = TextStyle(fontSize = sourceSubscriptFontSize)
            )

            BasicTextField(modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .padding(bottom = subscriptBottomPadding)
                ,value = numberString, onValueChange = {}, textStyle = TextStyle(fontSize = subscriptFontSize)
            )
        }
    }
}

@Composable
fun composeToken(
    tokenAndExponent: TokenAndExponent
) {
    composeToken(tokenAndExponent.token, tokenAndExponent.exponent.toString())
}

@Composable
fun composeToken (
    token: Token,
    exponent: String

) {
    var name: String
    var sourceString: String = ""
    val tokenName = token.name.toString()
    name = tokenName
    if (token.differential) {
        name = tokenName.toString() + "\u0307"
    } else {
        if (tokenName == "Se") {
            name = "S"
            sourceString ="e"
        } else {
            if (tokenName == "Sf") {
                name = "S"
                sourceString = "f"
            }
        }
    }

    println("name = $name")
    var subscript = token.bondId1
    if (token.bondId2 != "") {
        subscript = subscript + "," + token.bondId2
        println("token.name = " + token.name.toString())
    }
    Row(
        Modifier
            .width(IntrinsicSize.Min)
            .height(termHeight)
            //.background(Color.Yellow)
            .fillMaxWidth()
    ) {
        Column(
            Modifier
                //.background(Color.Red)
                .height(termHeight)
                .width(IntrinsicSize.Min)
                //.background(Color.Blue)
            , verticalArrangement = Arrangement.Bottom
        ) {
            BasicTextField(
                //value = token.name.toString()
                value = name.toString(), onValueChange = {}, modifier = Modifier
                    //.height(tokenHeight)
                    //. background(Color.Green)
                    //.wrapContentHeight(align = Alignment.Top)
                    .height(tokenHeight)
                    .padding(all = 0.dp)
                , textStyle = TextStyle(fontSize = tokenFontSize)
                ,

            )
        }

        if (name == "S") {
            Column(
                Modifier
                    //.background(Color.White)
                    .height(termHeight)
                    .width(IntrinsicSize.Min)
                    //.background(Color.Red)
                    //.padding(bottom = subscriptBottomPadding), verticalArrangement = Arrangement.SpaceBetween
                , verticalArrangement = Arrangement.Bottom

            ) {
                composeSubscript(sourceString, subscript)
            }
        } else {
            Column(
                Modifier
                    //.background(Color.White)
                    .height(termHeight)
                    .width(IntrinsicSize.Min)
                    //.background(Color.Red)
                    .padding(bottom = subscriptBottomPadding), verticalArrangement = Arrangement.SpaceBetween

            ) {

                BasicTextField(
                    value = exponent, onValueChange = {}, textStyle = TextStyle(fontSize = superscriptFontSize)
                )

                BasicTextField(
                value = subscript, onValueChange = {}, textStyle = TextStyle(fontSize = subscriptFontSize)
            )
                //composeSubscript(sourceString, subscript)
            }
        }

    }
}
@Composable
fun composeTerm(term: Term) {
    //val numeratorMap = loadTokenMap(term.numerators)
    println ("composeTerm(term) term = ${term.toAnnotatedString()}")

    if (term.denominators.isEmpty()) {
        composeTermRow(term.numerators)
    } else {
        Column(Modifier
            .height(IntrinsicSize.Min)
            .width(IntrinsicSize.Max)
            , horizontalAlignment = Alignment.CenterHorizontally

        ) {
            val denominatorMap = loadTokenMap(term.denominators)
            composeTermRow(term.numerators)
            Divider(
                modifier = Modifier.fillMaxWidth(), // Makes the divider fill the available width
                thickness = 3.dp, // Sets the thickness of the line
                color = Color.Gray // Sets the color of the line
            )
            composeTermRow(term.denominators)
        }
    }
}

@Composable
fun composeNumber(number: Number){
    Column(
        Modifier
            //.background(Color.Red)
            .height(termHeight)
            .width(IntrinsicSize.Min)
            //.background(Color.Blue)
        , verticalArrangement = Arrangement.Bottom
    ) {
        BasicTextField(
            //value = token.name.toString()
            value = number.value.toString(), onValueChange = {}, modifier = Modifier
                .height(tokenHeight)
                .width(IntrinsicSize.Min)
                //.background((Color.Red))
            //.padding(all = 0.dp)
            , textStyle = TextStyle(fontSize = tokenFontSize)
        )
    }
}

@Composable
fun composeTermRow(list: ArrayList<Expr>) {
    Row (Modifier
        .height(termHeight)
        .width(IntrinsicSize.Min)
        , verticalAlignment = Alignment.Bottom
    ){
        list.forEach { expr ->
            if (expr is Number) {
                composeNumber(expr)
            }
        }
        val map = loadTokenMap(list)
        composeTokenProduct(map)
        list.forEach { expr ->
            if (expr is Sum) {
                composeSum(expr, true)
            }
        }
    }
}

@Composable
fun composeTokenProduct(map: Map<Token, Int>) {
    Row (Modifier
        .height(termHeight)
        .width(IntrinsicSize.Min)
    ) {
        map.forEach { (key, value) -> composeToken(key, if (value > 1)value.toString() else "" )}
    }
}

@Composable
fun composeChar(char: String, height: Dp){
    BasicTextField(char
        , onValueChange = {}
        , textStyle = TextStyle(fontSize = (height.value).sp)
        , modifier = Modifier
            //.background(Color.Magenta)
            .width(IntrinsicSize.Min)
            .height(height)
    )
}
@Composable
fun composeSign(sign: String) {
    val bottomPadding = if (sign == "-") minusSignBottomPadding else 0.dp
    Column(
        Modifier
            //.background(Color.Red)
            .height(termHeight)
            .width(IntrinsicSize.Min), verticalArrangement = Arrangement.Bottom

    ) {
        BasicTextField(sign, onValueChange = {}, textStyle = TextStyle(fontSize = signFontSize), modifier = Modifier
            //.background(Color.Magenta)
            .width(IntrinsicSize.Min)
            .height(tokenHeight)
            //.background(Color.Red)
            .padding(start = signPadding, end = signPadding, bottom = bottomPadding)
        )
    }
}

@Composable
fun composeSum(sum: Sum, encloseInParentheses: Boolean) {
    Row (Modifier
        //.fillMaxWidth()
        .width(IntrinsicSize.Min)
        .height((IntrinsicSize.Min))
        , verticalAlignment = Alignment.CenterVertically
    )
    {
        val  height = if (expressionContainsFractions(sum)) termHeight.times(2) else termHeight

        if (encloseInParentheses){
            composeChar("(", height )
        }

        sum.plusTerms.forEachIndexed { index, expr ->
            if (index > 0) {
                composeSign("+")
            }
            when (expr) {
                is Token -> composeToken(expr, "")
                is Number -> composeNumber(expr)
                is Term -> composeStateTerm(expr)
                is Sum -> composeSum(expr, true)
            }
        }

        sum.minusTerms.forEach { expr ->
            composeSign("-")

            when (expr) {
                is Token -> composeToken(expr, "")
                is Number -> composeNumber(expr)
                is Term -> composeTerm(expr)
                is Sum -> composeSum(expr, true)
            }
        }

        if (encloseInParentheses) {
            composeChar(")", height)
        }
    }
}

@Composable
fun Fraction(nominaator: ArrayList<Token>, denominaor: ArrayList<Token>) {
    Column(Modifier
        .height(IntrinsicSize.Min)
        .width(IntrinsicSize.Max)
        , horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row (Modifier
            .fillMaxWidth()
            .height((IntrinsicSize.Min))

        )
        {
            BasicTextField("("
                , onValueChange = {}
                , textStyle = TextStyle(fontSize = (termHeight.value).sp)
                , modifier = Modifier
                    //.background(Color.Magenta)
                    .width(IntrinsicSize.Min)
                    .height(termHeight)
            )
            nominaator.forEach {composeToken(it, "2") }
            BasicTextField(")"
                ,onValueChange = {}
                , textStyle = TextStyle(fontSize = (termHeight.value).sp)
                , modifier = Modifier
                    //.background(Color.Magenta)
                    .width(IntrinsicSize.Min)
                    .height(termHeight)
            )
        }

        Divider(
            modifier = Modifier.fillMaxWidth(), // Makes the divider fill the available width
            thickness = 3.dp, // Sets the thickness of the line
            color = Color.Gray // Sets the color of the line
        )


        Row (Modifier
            .width(IntrinsicSize.Max)
            .height(IntrinsicSize.Min)
            //.background(Color.Yellow)
            , horizontalArrangement = Arrangement.Center
        )
        {
            denominaor.forEach { composeToken(it, "") }
        }

    }
}
