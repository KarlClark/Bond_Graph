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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import bondgraph.AlgebraException
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.modules.EmptySerializersModule
import java.util.LinkedHashMap

/*
the following constants are all based on the value of termHeight.  So to change the size of the text on the
output screen, hopefully all you have to do is changed the termHeight value.
 */
val termHeight = 25.dp
val tokenHeight = termHeight.times(.8f)
val tokenFontSize = (tokenHeight.value).sp
val subscriptFontSize = tokenFontSize.times(.45f)
val superscriptFontSize = tokenFontSize.times(.6)
val signFontSize = tokenFontSize.times(1)

val sourceSubscriptHeight = tokenHeight.times(.5f)
val sourceSubscriptFontSize = tokenFontSize.times(.8)
val subscriptBottomPadding = (tokenFontSize.value * .1).dp
val signPadding = termHeight.div(5f)
val minusSignBottomPadding = termHeight.times(.08f)
val equationBottomPadding = 8.dp
val fontFamily = FontFamily.Serif
val minusSign = "\u2013"



@Composable
fun runTest () {
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
    val term10 = Number(0.0).subtract( P.multiply(token1).multiply(Number(2.0)).divide(token2).divide(token3).multiply(token1))
    val term11 = Number(0.0).subtract( token1.multiply(Number(2.0)).divide(token2).divide(token3).multiply(token1))
    val term12 = term11.multiply(P)
    val sum3 = term7.add(term3)
    val eq1 = Equation(pDot, term7)
    val eq2 = Equation(qDot, term3.add(Se))
    val eq3 = Equation(q2Dot, term9.add(Sf))

    val equations = arrayListOf<Equation>(eq1, eq2, eq3)

    /*equations.forEach {eq ->  println("${eq.toAnnotatedString()}") }
    composeEquations(equations, map)*/
    println("eq1 = ${eq1.toAnnotatedString()}")
    composeLabeledEquation("3-", eq1)
    //composeSum(eq1.rightSide as Sum, true)
    //println("term11 = ${term11.toAnnotatedString()}, term12 = ${term12.toAnnotatedString()}")
    //composeStateTerm(term12)
}

/*
Data classs to hold a token and the value it should be raised to.
 */
class TokenAndExponent(var token: Token, var exponent: Integer)

/*
This function prints out a list of equations.  The equations are of the form
dotToken = sum of state variable terms
It's complicated because it prints out the terms in the right side of the equation in same order for each equation
which is determined by the order of the equations. Sources are printed at the end. So given

dotx = by + se1 - ax - cz
dotz = mx - ny + pz + sf2
doty = sz + qx - ry

The terms would be printed in the order x,z,y so we would get
dotx = -ax -cz + by + se1
dotz = mx + pz - ny + sf2
doty = qx + sz -ry

This done purely for readability and makes it much easier to check results.

First we build a map that maps each state token to an index by examining the list of equations.  So if P-dot is
in the second equation then P is mapped to index 1 (zero relative).
Then for each equation we loop through the terms on the right side and store them in an arraylist at the
appropriate index along with a boolean saying whether to add or subtract the term.  The sources are store in a
separate array.  Then we pass through the arrays printing out the terms in the correct order.

From the compose point of view things are pretty simple. We have one column of rows where each row is an equation.
 */
@Composable
fun composeEquations(equations: ArrayList<Equation>, dotTokenToTokenMap: Map<Token, Token>) {
    val tokenToIndexMap = linkedMapOf<Token, Int>()

    // build token to index map
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
        , verticalArrangement = Arrangement.spacedBy(equationBottomPadding)
    ) {


        equations.forEach { eq ->
            Row (Modifier
                .height(IntrinsicSize.Min)
                .width(IntrinsicSize.Min)
                //.background(Color.Blue)
                , verticalAlignment = Alignment.CenterVertically

            ) {
                val rightSide = eq.rightSide

                // print left side of equation and equal sign
                composeToken(eq.leftSide as Token, "")
                composeSign("=")

                if (rightSide !is Sum) {
                    composeExpression(rightSide)
                   } else {

                    // Create an arraylist for the state terms and initialize it.
                    val expressions = arrayListOf<Pair<Expr?, Boolean>>()
                    repeat(equations.size){
                        expressions.add(Pair(null, false))
                    }

                    val sourceExpressions  = arrayListOf<Pair <Expr, Boolean>>() // list for sources
                    println ("equations.size = ${equations.size}  expressions.size = ${expressions.size}")

                    // loop through the the plus and minus terms of the right side of the equation
                    // storing each term at the appropriate index in the appropriate list
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

                    // loop through expressions and compose them onto the row with plus and minus signs.
                    expressions.forEachIndexed { index, pair ->

                        if (pair.first != null) {
                            if (pair.second) {
                                composeSign(minusSign)
                            } else {
                                if (index > 0) {
                                    composeSign("+")
                                }
                            }

                            composeExpression(pair.first!!)
                        }
                    }

                    // now compose the source expressions.
                    sourceExpressions.forEach { pair ->
                        if (pair.second) {
                            composeSign(minusSign)
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

/*
loop through the arraylist and count how many times each token appears
in the list.  Build map relating token to number of occurrences. This
is used to determine exponents.
 */
fun loadTokenToExponentMap(list: ArrayList<Expr>): LinkedHashMap<Token, Int> {
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

/*
Prints out an equation without regard to the order of any terms. Consist of one row.
 */
@Composable
fun composeEquation(equation: Equation){
    Row (Modifier
        .height(IntrinsicSize.Min)
        .width(IntrinsicSize.Min)
        .padding(bottom = equationBottomPadding)
        , verticalAlignment = Alignment.CenterVertically

    ) {
        composeExpression(equation.leftSide)
        composeSign("=")
        composeExpression(equation.rightSide)
    }
}

/*
The BondGraph.derive function has a option for display intermediate results of equations.  This is used for
debugging.  Each equation has a label so we can see what part of the code generated the result. So this function
simply prints a string followed by and equation.
 */
@Composable
fun composeLabeledEquation(label: String, equation: Equation){
    Row (Modifier
        .height(IntrinsicSize.Min)
        .width(IntrinsicSize.Max)
        , verticalAlignment = Alignment.CenterVertically

    ) {
        BasicTextField(
            value = label+"   ", onValueChange = {}, modifier = Modifier
                .height(tokenHeight)
                .width(IntrinsicSize.Min)
                .padding(all = 0.dp)
            , textStyle = TextStyle(fontSize = tokenFontSize, fontFamily = fontFamily)
            )
        composeEquation(equation)
    }
}

/*
There are four types of expressions, Token, Number, Term, and Sum.  We have a composable function for each
of these.  This function just evaluates an expression and calls the correct function.
 */
@Composable
fun composeExpression(expr: Expr){
    when (expr) {
        is Token -> composeToken(expr, "")
        is Number -> composeNumber(expr)
        is Term -> composeStateTerm(expr)
        is Sum -> if (sumContainsStateExpressions(expr)) {
            //Basically, don't put parenthesis around the right side of an equation
            composeSum(expr, false)
        }
            else {
                composeSum(expr, true)
            }
    }
}

/*
A state term is basically a term multiplied by a state token.  (xy/z)P We want to display these as with the term
on the left and the token on the right.  If there are fractions, the row will be double height, but we want to
keep the token and non-fractions centered horizontally in the row.
        xy
abP + ----- Q
        mn

So, split or the term and token (if there is a state token). Then in a row, compose the term followed by the token.
 */
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

/*
Compose the subscript for a token.  The subscript is an optional string followed by a number.  So
if the sourceString is nulll, simply text the numberString, else create a row and compose the two strings
back to back.
 */
@Composable
fun composeSubscript(sourceString: String, numberString: String){
    if (sourceString == "") {
        BasicTextField(
            value = numberString
            , onValueChange = {}
            , textStyle = TextStyle(fontSize = subscriptFontSize,  fontFamily = fontFamily)
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
                //.padding(bottom = subscriptBottomPadding)

                //.background(Color.Yellow)
                ,value = sourceString, onValueChange = {}, textStyle = TextStyle(fontSize = sourceSubscriptFontSize)
            )

            BasicTextField(modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                //.padding(bottom = subscriptBottomPadding)
                ,value = numberString
                , onValueChange = {}
                , textStyle = TextStyle(fontSize = subscriptFontSize, fontFamily = fontFamily)
            )
        }
    }
}



/*
There are several variations in displaying a token.

    - Most are one letter. like p or R
    - Sources are two letters Se or Sf. the e and f are printed in smaller font that the Capital S.
    - All tokens have a numbered subscript.
    - The one-letter tokens may have an exponent.
    - If the token is a differential token, then we need to print a dot above the letter.

The composable is a row with two columns.  The first column contains the letter (the S for a source) at
full height. The second column is either for a source or a single letter.
    - Source          a subscript e or f followed by a subscript number at the bottom of the column.
    - single letter   a number subscript on the bottom of the column with an optional exponent on the top.
 */
@Composable
fun composeToken (
    token: Token,
    exponent: String

) {
    var name: String
    var sourceString: String = ""

    // Determine the letter name.  Start with the name of the token.  Then, if it's a differential token,
    // add the dot over the top of the letter. Else if the name is a source (Se, Sf) then change the name
    // to S and store the e or f in sourceString
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
        Column( // column for the letter name
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
                , textStyle = TextStyle(fontSize = tokenFontSize, fontFamily = fontFamily)
                ,

            )
        }

        if (name == "S") {
            Column(  // column for an e or f followed by a number subscript for a source
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
        } else {  // column for number subscript on the bottom and a optional exponent on the top.
            Column(
                Modifier
                    //.background(Color.White)
                    .height(termHeight)
                    .width(IntrinsicSize.Min)
                    //.background(Color.Red)
                    .padding(bottom = subscriptBottomPadding), verticalArrangement = Arrangement.SpaceBetween

            ) {

                BasicTextField(
                    value = exponent, onValueChange = {}, textStyle = TextStyle(fontSize = superscriptFontSize, fontFamily = fontFamily)
                )

                BasicTextField(
                value = subscript, onValueChange = {}, textStyle = TextStyle(fontSize = subscriptFontSize, fontFamily = fontFamily)
            )
                //composeSubscript(sourceString, subscript)
            }
        }

    }
}

/*
A Term can be a simple product of tokens and sums or it could be a fraction.  A fraction is basically
two products placed one above the other separated by a line.  This composable works basically like this.
If the term has no denominators it is just a row, if the term has denominators, it is column consisting
of a top row, a separator and then a bottom row.
 */
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

/*
This composable takes the value of a Number, converts it to a string and puts it in
a BasicTextField.
 */
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
            , textStyle = TextStyle(fontSize = tokenFontSize, fontFamily = fontFamily)
        )
    }
}

/*
This function is given an arrayList of expressions which is a product i.e. IRC or 2CIC(R + C).
The list represent either the numerator or denominator of a fraction. This function does several
things to format the product.
    - The list may contain one number.  If it does, this is printed first.
    - If the list contains more than one of any token (like the second example above that has
      two C's) we will print C with an exponent instead of separate tokens.
    - if the list contains one element, and it is a sum it can be printed without being surrounded
      by parenthesis. Otherwise, sums must be enclosed in parentheses.
The composable is a row with the expressions printed side by side.
 */
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

        val map = loadTokenToExponentMap(list)
        composeTokenProduct(map)

        val needsParens = list.size > 1
        list.forEach { expr ->
            if (expr is Sum) {
                composeSum(expr, needsParens)
            }
        }
    }
}

/*
This function is given a map that relates tokens to their exponent.  It prints out each token with
its subscript number and exponent(if the exponent is greater than 1) side by side in a row creating
a product.
 */
@Composable
fun composeTokenProduct(map: Map<Token, Int>) {
    Row (Modifier
        .height(termHeight)
        .width(IntrinsicSize.Min)
    ) {
        map.forEach { (key, value) -> composeToken(key, if (value > 1)value.toString() else "" )}
    }
}

/*
Places a character in a BasicTextField.  The size of the field and the font size
are determined by the height parameter.
 */
@Composable
fun composeChar(char: String, height: Dp){
    BasicTextField(char
        , onValueChange = {}
        , textStyle = TextStyle(fontSize = (height.value).sp, fontFamily = fontFamily)
        , modifier = Modifier
            //.background(Color.Magenta)
            .width(IntrinsicSize.Min)
            .height(height)
    )
}
/*
This function will be given a plus sign or minus sign, which is placed in a text field with some
padding on each side.  Minus signs are given some bottom since they seem to print a little low
in my opinion.
 */
@Composable
fun composeSign(sign: String) {
    val bottomPadding = if (sign == minusSign) minusSignBottomPadding else 0.dp
    Column(
        Modifier
            //.background(Color.Red)
            .height(termHeight)
            .width(IntrinsicSize.Min), verticalArrangement = Arrangement.Bottom

    ) {
        BasicTextField(sign, onValueChange = {}
            , textStyle = TextStyle(fontSize = signFontSize, fontFamily = fontFamily)
            , modifier = Modifier
            //.background(Color.Magenta)
            .width(IntrinsicSize.Min)
            .height(tokenHeight)
            //.background(Color.Red)
            .padding(start = signPadding, end = signPadding, bottom = bottomPadding)
        )
    }
}

/*
Prints out a Sum, which is a row of expressions separated by plus signs or minus signs
and optionally enclosed in parentheses. Each expression could be a Token, Number, Term,
or another Sum, so this function uses other functions to print these out.  A Term may
be a fraction so the row may be double height with some expressions using the full
height and others being just half height.  Half height expressions should be centered
vertically in the row.
 */
@Composable
fun composeSum(sum: Sum, encloseInParentheses: Boolean) {
    Row (Modifier
        //.fillMaxWidth()
        .width(IntrinsicSize.Min)
        .height((IntrinsicSize.Min))
        , verticalAlignment = Alignment.CenterVertically
    )
    {
        println("composeSum(sum) sum = ${sum.toAnnotatedString()}")
        val  height = if (expressionContainsFractions(sum)) termHeight.times(2) else termHeight

        if (encloseInParentheses){
            composeChar("(", height )
        }

        sum.plusTerms.forEachIndexed { index, expr ->
            if (index > 0) {
                composeSign("+")
            }
            println ("composeSum expr = ${expr.toAnnotatedString()}: ${expr::class.simpleName}")
            when (expr) {
                is Token -> composeToken(expr, "")
                is Number -> composeNumber(expr)
                is Term -> composeStateTerm(expr)
                is Sum -> composeSum(expr, true)
            }
        }

        sum.minusTerms.forEach { expr ->
            composeSign(minusSign)
            println ("composeSum expr = ${expr.toAnnotatedString()}: ${expr::class.simpleName}")
            when (expr) {
                is Token -> composeToken(expr, "")
                is Number -> composeNumber(expr)
                is Term -> composeStateTerm(expr)
                is Sum -> composeSum(expr, true)
            }
        }

        if (encloseInParentheses) {
            composeChar(")", height)
        }
    }
}

