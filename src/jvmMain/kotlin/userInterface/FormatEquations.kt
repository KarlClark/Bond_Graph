package userInterface
import algebra.Token
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
import java.util.LinkedHashMap

val termHeight = 30.dp
val tokenHeight = termHeight.times(.8f)
val tokenFontSize = (tokenHeight.value).sp
val subscriptFontSize = tokenFontSize.times(.4f)
val superscriptFontSize = tokenFontSize.times(.6f)
val signFontSize = tokenFontSize.times(1)
val subscriptBottomPadding = (tokenFontSize.value * .1).dp

val signPadding = termHeight.div(5f)
val minusSignBottomPadding = termHeight.times(.08f)

val token1 = Token("1", "", AnnotatedString("C"))
val token2 = Token("3", "", AnnotatedString("I"))
val token3 = Token("12", "", AnnotatedString("R"))
val token4 = Token("3", "", AnnotatedString("C"))
val token5 = Token("5", "", AnnotatedString("I"))
val token6= Token("1", "", AnnotatedString("R"))

val term2 = token1.multiply(token2).multiply(token3).multiply(token2)

val term1 = token1.multiply(token2).multiply(token3).multiply(token2).divide(token6).divide(token5).divide(token4)

val term3 = token5.multiply(token6)
val term5 = token3.multiply(token1)

val tokens = arrayListOf<Any>(token1, token2, token3)
val nTokens = arrayListOf<Token>(token2, token1, token3)
val dTokens = arrayListOf<Token>(token1, token2, token3)

val tokenMap = linkedMapOf<Token, Int>(token1 to 1, token2 to 2, token3 to 1)

val sum1 = token1.add(token2)
val sum2 = term2.subtract(term5).subtract(token2)
val term6 = multiply_f(term5, sum1).divide(token6).divide(token5)



class TokenAndExponent(var token: Token, var exponent: Integer)

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
fun runTest () {

    //Fraction(nTokens, dTokens)
    //composeSum(sum2 as Sum)
    composeTerm(term6 as Term)
}

@Composable
fun cTest(list: ArrayList<Any>){
    Column(Modifier
        .height(IntrinsicSize.Min)
        .width(IntrinsicSize.Max)
        , horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height((IntrinsicSize.Min))

        )
        {

            BasicTextField(
                "(",
                onValueChange = {},
                textStyle = TextStyle(fontSize = (termHeight.value).sp),
                modifier = Modifier
                    //.background(Color.Magenta)
                    .width(IntrinsicSize.Min)
                    .height(termHeight)
            )
            list.forEach { TokenBox(it as Token, "2") }
            BasicTextField(
                ")",
                onValueChange = {},
                textStyle = TextStyle(fontSize = (termHeight.value).sp),
                modifier = Modifier
                    //.background(Color.Magenta)
                    .width(IntrinsicSize.Min)
                    .height(termHeight)
            )
        }
    }

}

@Composable
fun TokenBox(
    tokenAndExponent: TokenAndExponent
) {
    TokenBox(tokenAndExponent.token, tokenAndExponent.exponent.toString())
}

@Composable
fun TokenBox (
    token: Token,
    exponent: String

) {
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
                .width(IntrinsicSize.Min), verticalArrangement = Arrangement.Bottom
        ) {
            BasicTextField(
                //value = token.name.toString()
                value = token.name.toString(), onValueChange = {}, modifier = Modifier
                    //.height(tokenHeight)
                    //. background(Color.Green)
                    //.wrapContentHeight(align = Alignment.Top)
                    .height(tokenHeight)
                    .padding(all = 0.dp)
                , textStyle = TextStyle(fontSize = tokenFontSize)
                ,

            )
            //Text("TTT")
        }

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
fun composeTermRow(list: ArrayList<Expr>) {
    Row (Modifier
        .height(termHeight)
        .width(IntrinsicSize.Min)
    ){
        val map = loadTokenMap(list)
        composeTokenProduct(map)
        list.forEach { expr ->
            if (expr is Sum) {
                composeSum(expr)
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
        map.forEach { (key, value) -> TokenBox(key, if (value > 1)value.toString() else "" )}
    }
}

@Composable
fun composeChar(char: String){
    BasicTextField(char
        , onValueChange = {}
        , textStyle = TextStyle(fontSize = (termHeight.value).sp)
        , modifier = Modifier
            //.background(Color.Magenta)
            .width(IntrinsicSize.Min)
            .height(termHeight)
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
fun composeSum(sum: Sum) {
    Row (Modifier
        .fillMaxWidth()
        .height((IntrinsicSize.Min))

    )
    {
        composeChar("(")
        sum.plusTerms.forEachIndexed { index, expr ->
            if (expr is Token) {
                if (index > 0) {
                    composeSign("+")
                }
                TokenBox(expr, "")
            } else {
                if (expr is Term){
                    if (index > 0) {
                        composeSign("+")
                    }
                    composeTerm(expr)
                }
            }
        }

        sum.minusTerms.forEachIndexed { index, expr ->
            if (expr is Token) {
                composeSign("-")
                TokenBox(expr, "")
            } else {
                if (expr is Term){
                    composeSign("-")
                    composeTerm(expr)
                }
            }
        }
        composeChar(")")
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
            nominaator.forEach {TokenBox(it, "2") }
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
            .background(Color.Yellow)
            , horizontalArrangement = Arrangement.Center
        )
        {
            denominaor.forEach { TokenBox(it, "") }
        }

    }
}
