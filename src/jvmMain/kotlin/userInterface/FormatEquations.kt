package userInterface

/*
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
import androidx.compose.ui.unit.plus
import androidx.compose.ui.unit.sp

val termHeight = 30.dp
val tokenHeight = termHeight.times(1)
val tokenFontSize = (termHeight.value * .7).sp
val subscriptFontSize = tokenFontSize.times(.4f)
val superscriptFontSize = tokenFontSize.times(.60f)
val subscriptBottomPadding = (tokenFontSize.value * .1).dp

val token1 = Token("1", "", AnnotatedString("C"))
val token2 = Token("1", "", AnnotatedString("I"))
val token3 = Token("1", "", AnnotatedString("R"))

val tokens = arrayListOf<Any>(token1, token2, token3)



class TokenAndExponent(var token: Token, var exponent: Integer)

@Composable
fun runTest () {
    testWindow(tokens){list -> cTest(list)}
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
                .width(IntrinsicSize.Min), verticalArrangement = Arrangement.Top
        ) {
            BasicTextField(
                //value = token.name.toString()
                value = token.name.toString(), onValueChange = {}, modifier = Modifier
                    //.height(tokenHeight)
                    //.background(Color.Green)
                    //.wrapContentHeight(align = Alignment.Top)
                    .height(termHeight)
                    .padding(all = 0.dp)
                , textStyle = TextStyle(fontSize = tokenFontSize)

            )
            //Text("TTT")
        }

        Column(
            Modifier
                //.background(Color.White)
                .height(termHeight)
                .width(IntrinsicSize.Min)
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

       */
/* HorizontalDivider(
            modifier = Modifier.fillMaxWidth(), // Makes the divider fill the available width
            thickness = 3.dp, // Sets the thickness of the line
            color = Color.Gray // Sets the color of the line
        )*//*


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
}*/
