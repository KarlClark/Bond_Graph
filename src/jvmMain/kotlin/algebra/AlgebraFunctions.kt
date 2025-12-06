package algebra

import androidx.compose.ui.text.AnnotatedString
import bondgraph.AlgebraException
import kotlin.math.absoluteValue
import algebra.Sign.POSITIVE
import algebra.Sign.NEGATIVE
import algebra.operations.*
import androidx.compose.runtime.mutableStateMapOf


fun testCases(){

    val at = Token("k", "", AnnotatedString("A"))
    val bt = Token("k", "", AnnotatedString("B"))
    val ct = Token("k", "", AnnotatedString("C"))
    val xt = Token("k", "", AnnotatedString("X"))
    val yt = Token("k", "", AnnotatedString("Y"))
    val zt = Token("k", "", AnnotatedString("Z"))
    val n10 = Number(10.0)
    val n2 = Number(2.0)
    val n6 = Number(6.0)


    var term1 = Term()
    var term2 = Term()
    var expr1: Expr
    var expr2: Expr
    var expr3: Expr
    var expr4: Expr
    var expr5: Expr
    var sum1 = Sum().add(Number(4.0)).add(Number(6.0))
    var sum2 = Sum().subtract(Number(5.0))
    var expr = sum1.divide(sum2)
   /* println("*************************************************************************")
    println("expr = ${expr.toAnnotatedString()}: ${expr::class.simpleName}")
    if (expr is Term) {
        val coefficientAndTerm = stripCoefficient(expr)
        println("stripCoefficiett = number = ${coefficientAndTerm.coefficient} expr = ${coefficientAndTerm.expr.toAnnotatedString()}")
    }
    expr = sum1.divide(sum2)

    println("*************************************************************************")
    println("expr = ${expr.toAnnotatedString()}: ${expr::class.simpleName}")

    expr1 = Number(10.0).multiply(at)
    expr2 = bt.multiply(Sum().subtract(Number(2.0)))
    expr3 = expr1.divide(expr2)
    println("*************************************************************************")
    println("${expr1.toAnnotatedString()} divided by ${expr2.toAnnotatedString()} = ${expr3.toAnnotatedString()}")
*/
    /*println("Test cases &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&")
    println(Term().multiply(xt).divide(yt).multiply(n10).toAnnotatedString())
    println("-------------------------------------------------------------")
    expr1 = sum1.add(at).add(bt).add(n10)
    println("expr1 = ${expr1.toAnnotatedString()}")
    expr2 = term1.multiply(xt).divide(yt)
    println("expr2 = ${expr2.toAnnotatedString()}")
    expr3 = expr1.multiply(expr2)
    expr4 = expr2.multiply(at).divide(bt).divide(n10)
    expr5 = Term().multiply(bt).divide(ct).multiply(n6)

    println("expr3 = ${expr3.toAnnotatedString()}")
    println("expr4 = ${expr4.toAnnotatedString()}")
    println("expr5 = ${expr5.toAnnotatedString()}")
    expr5 = expr3.divide(expr5)
    println("expr5 = ${expr5.toAnnotatedString()}")
    println ("result ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    println("${expr1.toAnnotatedString()} x ${expr2.toAnnotatedString()} = ${expr3.toAnnotatedString()}  ${expr3::class.simpleName}")
    expr3 = expr1.divide(n2)
    println(expr3.toAnnotatedString())
    expr3 = expr3.multiply(n10)
    println(expr3.toAnnotatedString())
    //expr3 = expr3.multiply(expr1)
    expr3 = expr3.divide(n6)
    println(expr3.toAnnotatedString())
    expr3 = expr3.subtract(n10)
    println(expr3.toAnnotatedString())
    expr3 = expr3.multiply(n6)
    println(expr3.toAnnotatedString())
    expr3 = expr3.add(n6)
    println(expr3.toAnnotatedString())
    expr3 = expr3.add(expr3)
    println(expr3.toAnnotatedString())
    println ("result ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    println("${expr2.toAnnotatedString()} x ${expr1.toAnnotatedString()} = ${expr3.toAnnotatedString()}  ${expr3::class.simpleName}")*/

    val sum = Number(1.0).subtract(Number(6.0))
    expr  = Number(10.0).divide(sum)
    println("%%% expr=${expr.toAnnotatedString()}")
    println ("sum = ${sum.toAnnotatedString()}: ${sum::class.simpleName}")

    val builder = Matrix.Builder(3)
    for (i in 1 .. 3){
        for (j in 1 .. 3){
            builder.add(Number(i.toDouble()).add(Number(j.toDouble())))
        }
    }
    val m1 = builder.build()
    m1.printOut()
    val m2 = m1.cofactor(1,1)
    m2.printOut()
    println("det m2 = ${m2.det(true).toAnnotatedString()}")
    var det = m1.det(true)
    println("det m1 = ${det.toAnnotatedString()}: ${det::class.simpleName}")
    //println("plusterms.size = ${(det as Sum).plusTerms.size}")
    //println("minusterms.size = ${(det as Sum).minusTerms.size}")
    //t as Sum).plusTerms.forEach { println("pluseterm ${it.toAnnotatedString()}: ${it::class.simpleName}" ) }
    //(det as Sum).minusTerms.forEach { println("minusterm ${it.toAnnotatedString()}: ${it::class.simpleName}" ) }
    val builder2 = Matrix.Builder(3)
    builder2.add(Number(2.0)).add(Number(2.0)).add(Number(1.0))
        .add(Sum().subtract(Number(1.0))).add(Number(0.0)).add(Sum().subtract(Number(1.0)))
            .add(Number(3.0)).add(Sum().subtract(Number(1.0))).add(Number(3.0))
    val m3 = builder2.build()
    m3.printOut()
    det = m3.det(true)
    println("det = ${det.toAnnotatedString()}: ${det::class.simpleName}")
    val builder3 = Matrix.Builder(3)
    builder3.add(Number(1.0)).add(Number(3.0)).add(Sum().subtract(Number(2.0)))
        .add(Number(2.0)).add(Sum().subtract(Number(1.0))).add (Number(1.0))
        .add(Sum().subtract(Number(2.0))).add(Number(2.0)).add(Number(3.0))
    val m4 = builder3.build()
    m4.printOut()
    det = m4.det(true)
    println("det = ${det.toAnnotatedString()}: ${det::class.simpleName}")

    val builder4 = Matrix.Builder(3)
    builder4.add(Token("1", "", AnnotatedString("a")))
        .add(Token("1", "", AnnotatedString("b")))
        .add(Token("1", "", AnnotatedString("c")))
        .add(Token("2", "", AnnotatedString("a")))
        .add(Token("2", "", AnnotatedString("b")))
        .add(Token("2", "", AnnotatedString("c")))
        .add(Token("3", "", AnnotatedString("a")))
        .add(Token("3", "", AnnotatedString("b")))
        .add(Token("3", "", AnnotatedString("c")))
    val coeff = builder4.build()
    val const = arrayListOf<Expr>(Token("1", "", AnnotatedString("d")), Token("2", "", AnnotatedString("d")),Token("3", "", AnnotatedString("d")))
    val variables = arrayListOf<Token>(Token("", "", AnnotatedString("x")), Token("", "", AnnotatedString("y")), Token("", "", AnnotatedString("z")))
    val equations = Matrix.solveCramer(coeff, variables, const)
    println ("######################################################")
    equations.forEach { println("${it.toAnnotatedString()}") }

    val builder5 = Matrix.Builder(3)
    builder5.add(Number(1.0)).add(Number(1.0)).add(Number(1.0))
        .add(Number(0.0)).add(Number(1.0)).add(Number(3.0))
            .add(Number(1.0)).add(Sum().subtract(Number(2.0))).add(Number(1.0))
    val coeff2 = builder5.build()
    val const2 = arrayListOf<Expr>(Number(6.0), Number(11.0), Number(0.0))
    val equations2 = Matrix.solveCramer(coeff2, variables, const2)
    println ("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")

    equations2.forEach { println("${it.toAnnotatedString()}") }

   /* val builder6 = Matrix.Builder(2)
    builder6.add(Number(1.0)).add(Sum().subtract(Token("2", "", AnnotatedString("R"))))
        .add(Sum().subtract(Token("6", "", AnnotatedString(("R"))))).add(Number(1.0))
    val coeff3 = builder6.build()
    val const3 = arrayListOf<Expr>(Token("1", "", AnnotatedString("R")), Token("3", "", AnnotatedString("R")))
    val variables3 = arrayListOf<Token>(Token("3", "", AnnotatedString("p"), differential = true), Token("5", "", AnnotatedString("p"), differential = true))
    val equations3 = Matrix.solveCramer(coeff3, variables3, const3)
    println ("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
    equations3.forEach { println("${it.toAnnotatedString()}") }*/

  /*  val builder7 = Matrix.Builder(2)
    builder7.add(Number(7.0)).add(Number(2.0))
        .add(Number(0.0)).add(Number(4.0))
    val coeff4 = builder7.build()
    val const4 = arrayListOf<Expr>(Number(20.0), Number(12.0))
    val equations4 = Matrix.solveCramer(coeff4, variables, const4)
    println ("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
    equations4.forEach { println("${it.toAnnotatedString()}") }*/

}

fun testCases2 (){
    val tX = Token("X", powerVar = true)
    val tY = Token("Y", powerVar = true)
    val tZ = Token("Z", powerVar = true)
    val tL = Token("L")
    val tM = Token("M")
    val tN = Token("N")

    val rightSide1 = tX.multiply(Number(3.0)).subtract(tY.multiply(Number(2.0))).add(tZ).subtract(Number(15.0))
    val rightSide2 = tX.add(Number(3.0).multiply(tY)).subtract(tZ.multiply(Number(2.0))).add(Number(14.0))
    val rightSide3 = tY.subtract(tX).add(tZ.multiply(Number(2.0)))
    val equation1 = Equation(tX, rightSide1)
    val equation2 = Equation(tY, rightSide2)
    val equation3 = Equation(tZ, rightSide3)
    val equations = arrayListOf<Equation>(equation1, equation2, equation3)
    val solved = solve(equations)
    equations.forEach { println("${it.toAnnotatedString()}") }
    solved.forEach { println("${it.toAnnotatedString()}") }

}

fun testCases3() {
    val tX = Token("X", powerVar = true)
    val tY = Token("Y", powerVar = true)
    val tZ = Token("Z", powerVar = true)
    val tA = Token("A")
    val tB = Token("B")
    val tC = Token("C")
    val tL = Token("L")
    val tM = Token("M")
    val tN = Token("N")
    val tO = Token("O")
    val tP = Token("P", powerVar = true )


    //val leftSide1 = (tY.divide(Number(2.0))).subtract(tZ.divide(Number(2.0))).add(Number(2.0))
    //val leftSide2 = Number(4.0).subtract(tX.divide(Number(3.0))).subtract(tZ.multiply(Number(2.0)).divide(Number(3.0)))
    //val leftSide3 = divide(Number(16.0), Number(3.0)).subtract(tX).subtract(tY.multiply(Number(2.0)).divide(Number(3.0)))


    //val leftSide1 = multiply(Number(3.0), tX).subtract(Number(2.0).multiply(tY)).add(tZ).subtract(Number(15.0)).add (tA.multiply(tB))
    //val leftSide2 = tX.add(multiply(Number(3.0), tY).subtract(Number(2.0).multiply(tZ))).add(Number(14.0)).add(tB.multiply(tC))
    //val leftSide3 = tY.subtract(tX).add(Number(2.0).multiply(tZ)).add(tB)
    val leftSide4 = (tB.multiply(tP)).divide(tC).add(tN.multiply(tM))
    val leftSide5 = tM.multiply(tO).multiply(tP).add(tA.multiply(tB)).add(tN.multiply(tM).multiply(tY))
    val leftside6 = tN.multiply(tA).multiply(tP).add(tZ.multiply(tB)).add(tA.multiply(tB).divide(tC)).add(tY.multiply(tC))

    //val equation1 = Equation(tX,leftSide1)
    //val equation2 = Equation(tY, leftSide2)
    //val equation3 = Equation(tZ, leftSide3)
    val equation4 = Equation(tX, leftSide4)
    val equation5 = Equation(tY, leftSide5)
    val equation6 = Equation(tZ, leftside6)
    val equations = arrayListOf<Equation>(equation4, equation5, equation6)

    val solved1 = solve(arrayListOf(equation4))

    val solved2 = solve(arrayListOf(equation5))
    val solved3 = solve(arrayListOf(equation6))


    val solved4 = solve(equations)
    equations.forEach { println("${it.toAnnotatedString()}") }
    solved1.forEach {equ ->
        println("${equ.toAnnotatedString()}")
    }
    solved2.forEach {equ ->
        println("${equ.toAnnotatedString()}")
    }
    solved3.forEach {equ ->
        println("${equ.toAnnotatedString()}")
    }

    solved4.forEach { equ ->
        println("${equ.toAnnotatedString()}")
    }


    /*val expr1 = Number(3.0).multiply(tA.divide(tB))
    val expr2 = expr1.multiply(tC)
    val expr3 = tC.add(expr2)
    println("expr1 = ${expr1.toAnnotatedString()}, expr2 = ${expr2.toAnnotatedString()}  expr3 = ${expr3.toAnnotatedString()}")
    println("**********************************************************************************")*/
}

fun testCases4() {

    val tX = Token("X", powerVar = false)
    val tY = Token("Y", powerVar = false)
    val tZ = Token("Z", powerVar = false)
    val tA = Token("A")
    val tB = Token("B")
    val tC = Token("C")
    val tP = Token("P", powerVar = true)
    val tD = Token("D", powerVar = true)
    val sum1  = Number(0.0).subtract(tA.multiply(tB).multiply(tP))
    val sum2 = Number(0.0).subtract(tX.multiply(tY))
    var expr1: Expr
    var expr2: Expr

    val term1 = sum1.multiply(sum2)
    val term2 = Term()
    //val term1 = divide(tX,tZ)
    val term3 = Term()
    val term4 = Term()
    val term5 = Term()
    val term6 = Term()
    println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    println("sum1 = ${sum1.toAnnotatedString()}, sum2 = ${sum2.toAnnotatedString()}, term1 = ${term1.toAnnotatedString()}")

}

fun testCases5() {

    val tX = Token("X", powerVar = false)
    val tY = Token("Y", powerVar = false)
    val tZ = Token("Z", powerVar = false)
    val tM = Token("M")
    val tN = Token("N")
    val tO = Token("O")
    val tA = Token("A")
    val tB = Token("B")
    val tQ = Token("Q")
    val tR = Token("R")
    val tS = Token("S")
    val tC = Token("C")
    val tP = Token("P", powerVar = true)
    val tD = Token("D", powerVar = true)

    var term1: Expr
    var term2: Expr
    var term3: Expr
    var term4: Expr
    var term5: Expr
    var expr1: Expr
    var expr2: Expr
    var expr3: Expr
    var expr4: Expr
    var expr5: Expr

    expr1 = Number(0.0).add(tM).add(tA)
    term1= tP.multiply(tB).divide(negate(expr1 as Sum)).add(tD.multiply(tQ).divide(tR)).divide(tS)
    term2 = term1.multiply(tQ.add(tR))
    expr2 = term2.multiply(expr1)
    println("expr1 = ${expr1.toAnnotatedString()},  term1 = ${term1.toAnnotatedString()}, term2 = ${term2.toAnnotatedString()}, expr2 = ${expr2.toAnnotatedString()}")

}


/*
A state variable is any token that has the powerVar or energyVar flags set. A
state variable expression is either a state token or a term of the form
(expr)(state token).  We try to keep expression containing state tokens in this
form because, when solving equations, it is often necessary to divide by the
coefficient of a state token. Examples:
    P (state token)
    2P (Number times state token)
    RP (Token times state token)
    R1R2/(R1+R2) x P (Term times state token)
    (R1+R2) x P (Sum times state token) etc.
This function tests the expression to see if it's a state variable expression
 */
fun isStateVariableExpr(expr: Expr): Boolean{

    fun isStateToken(expr: Expr): Boolean {
        if (expr !is Token) return false
        return (expr.powerVar || expr.energyVar)
    }

    when (expr) {
        is Token -> return isStateToken(expr)
        is Number -> return false
        is Term -> {
            if (expr.numerators.size == 0) return false
            if (expr.numerators.size == 1 && isStateToken(expr.numerators[0])) return true
            if (expr.numerators.size == 2 && isStateToken(expr.numerators[1])) return true
            return false
        }
        is Sum -> return false
    }

    //shouldn't get here
    return false
}
/*
Returns the state token from the state expression
 */
fun getTokenFromStateExpression(expr: Expr): Token {

    if ( ! isStateVariableExpr(expr)) {
        throw AlgebraException("Call to getTokenFromStateExpression with expression that is not a state expression. Expression = ${expr.toAnnotatedString()}")
    }

    if (expr is Token) return expr
    val term = expr as Term
    if (term.numerators.size == 1)return term.numerators[0] as Token
    return term.numerators[1] as Token
}

fun getTermFromStateExpression(expr: Expr): Expr {

    if ( ! isStateVariableExpr(expr)) {
        throw AlgebraException("Call to getTermFromStateExpression with expression that is not a state expression. Expression = ${expr.toAnnotatedString()}")
    }

    if (expr is Token) return Number(1.0)
    val term = expr as Term
    if (term.numerators.size == 1)return Number(1.0)
    return term.numerators[0]
}

fun sumContainsStateExpressions(sum: Sum): Boolean {
    sum.plusTerms.forEach {term ->
        if (isStateVariableExpr(term))
            return true
    }

    sum.minusTerms.forEach {term ->
        if (isStateVariableExpr(term))
            return true
    }

    return false
}

fun createStateExpression(expr: Expr, token: Token): Expr {

    val term = Term()
    when(expr){

        is Token -> {
            term.numerators.add(expr)
            term.numerators.add(token)
            return term
        }

        is Number -> {
            if (expr.value == 0.0) return Number(0.0)
            if (expr.value == 1.0) return token
            term.numerators.add(expr)
            term.numerators.add(token)
            return term
        }

        is Term -> {
            if (expr.numerators.size == 1 && expr.denominators.isEmpty()) {
                return createStateExpression(expr.numerators[0], token)
            }

            if (exprIsNegative(expr)) {
                val sum = Sum()
                sum.minusTerms.add(convertNegativeToPositive(expr))
                term.numerators.add(sum)
                term.numerators.add(token)
                return term
            }

            term.numerators.add(expr)
            term.numerators.add(token)
            return term
        }

        is Sum -> {
            if (expr.plusTerms.isEmpty() && expr.minusTerms.isEmpty()) return Number(0.0)
            if (expr.plusTerms.size == 1 && expr.minusTerms.isEmpty()) return createStateExpression(expr.plusTerms[0], token)

            if (exprIsNegative(expr)) {
                val sum = Sum()
                sum.minusTerms.add(convertNegativeToPositive(expr))
                term.numerators.add(sum)
                term.numerators.add(token)
                return term
            }

            term.numerators.add(expr)
            term.numerators.add(token)
            return term
        }
    }
    return Term()
}

fun expressionContainsFractions(expr: Expr): Boolean {
    when (expr) {
        is Token -> return false
        is Number -> return false
        is Term -> return expr.denominators.isNotEmpty()
        is Sum -> {
            expr.plusTerms.forEach { e ->
                if (e is Term && e.denominators.isNotEmpty()) {
                    return true
                }
            }
            expr.minusTerms.forEach { e ->
                if (e is Term && e.denominators.isNotEmpty()) {
                    return true
                }
            }
            return false
        }
    }
    return false
}

fun exprIsNegative(expr: Expr): Boolean {

    var workingExpr: Expr

    if (isStateVariableExpr(expr)) {
        workingExpr = getTermFromStateExpression(expr)
    } else {
        workingExpr = expr
    }

    when (workingExpr) {
        is Token -> return false
        is Number -> return false
        is Term -> {
            var cnt = 0
            workingExpr.numerators.forEach { e ->
                if (e is Sum && e.plusTerms.isEmpty()) cnt++
            }
            workingExpr.denominators.forEach { e ->
                if (e is Sum && e.plusTerms.isEmpty()) cnt++
            }
            return (cnt % 2 != 0)  // odd cnt
        }
        is Sum -> return (workingExpr.plusTerms.isEmpty())
    }
    return false
}

fun convertNegativeToPositive(expr: Expr): Expr {
    if ( ! exprIsNegative(expr)) {
        throw AlgebraException("convertNegativeToPositive(expr) called with positive expression.  expr = ${expr.toAnnotatedString()}")
    }

    var isStateExpr = false
    var token = Token()
    var workingExpr: Expr

    if (isStateVariableExpr(expr)) {
        workingExpr = getTermFromStateExpression(expr)
        token = getTokenFromStateExpression(expr)
        isStateExpr = true
    } else {
        workingExpr = expr
    }

    when(workingExpr) {

        is Term -> {
            val newTerm = Term()
            workingExpr.numerators.forEach { e ->
                if (e is Sum && e.plusTerms.isEmpty()) {
                   newTerm.numerators.add(negate(e))
                } else {
                    newTerm.numerators.add(e)
                }
            }
            workingExpr.denominators.forEach { e ->
                if (e is Sum && e.plusTerms.isEmpty()) {
                    newTerm.denominators.add(negate(e))
                } else {
                    newTerm.denominators.add(e)
                }
            }
            println("convertNegativeToPositive(expr) isStateExpr = $isStateExpr, token = ${token.toAnnotatedString()}, newTerm = ${newTerm.toAnnotatedString()}")
            if (isStateExpr) {
                return createStateExpression(newTerm, token)
            } else {
                return newTerm
            }
        }

        is Sum -> {
            println("convertNegativeToPositive(expr) isStateExpr = $isStateExpr, token = ${token.toAnnotatedString()}, workingExpr = ${workingExpr.toAnnotatedString()} negqte workingExpr = ${(negate(workingExpr).toAnnotatedString())}")
            if (isStateExpr) {
                return createStateExpression(negate(workingExpr) ,token)
            } else {
                return negate(workingExpr)
            }
        }
    }

    return expr
}

fun createNegativeExpression(expr: Expr): Expr {

    if (isStateVariableExpr(expr)){
        val sum = Sum()
        sum.minusTerms.add(getTermFromStateExpression(expr))
        return createStateExpression(sum, getTokenFromStateExpression(expr))
    }

    val sum = Sum()
    sum.minusTerms.add(expr)
    return sum
}
fun List<Expr>.containsExpr(expr: Expr): Boolean {
    return this.any{expr.equals(it)}
}
/*
This function replaces the energy token in a expression, with its matching dot token,
i.e. the token that represents the time derivative of the energy token.  The function
is given an expression and a map of energy tokens mapped to their corresponding
dot tokens.  We throw an error if the expression is a Sum.  Calling code should use
this function on individual terms of a sum. If the expression is not a state variable
expression it is simply returned.  If the token of a state variable expression doesn't
match any token in the map, then the original expression is returned.  Otherwise, the
energy token in the expression is replaced with its corresponding dot token from the
map, and the new expression is returned.
 */
fun replaceTokenWithDotToken(expr: Expr, replacementMap: Map<Token, Token>): Expr {

    println("replaceTokenWithDotToken(expr, map) expr = ${expr.toAnnotatedString()}")
    if (expr is Sum) {
        // shouldn't happen so throw an error
        throw AlgebraException("replaceTokenWithDotToken (expr, map) This function can't handle Sums.  Calling function should be passing in individual terms of a Sum.  expr = ${expr.toAnnotatedString()}")
    }

    if ( ! isStateVariableExpr(expr)){
        return expr
    }

    when (expr) {

        is Token -> {
            val dotToken = replacementMap[expr]
            if (dotToken == null){
                return expr
            } else {
                return dotToken
            }
        }

        is Term -> {
            val eToken = getTokenFromStateExpression(expr)
            val dotToken = replacementMap[eToken]
            println("replaceTokenWithDotToken token = ${eToken.toAnnotatedString()}")
            if (dotToken == null) {
                println("replaceTokenWithDotToken dotToken = null")
                return expr
            } else {
                println("replaceTokenWithDotToken dotToken = ${dotToken.toAnnotatedString()}")
                val term = Term()
                term.numerators.add(getTermFromStateExpression(expr))
                term.numerators.add(dotToken)
                println("replaceTokenWithDotToken returning term = ${term.toAnnotatedString()}")
                return term
            }
        }
    }

    // should fall through to here
    return expr
}

/*
The idea behind this function is to pass in an equation (probably just the right side) and replace
the energy tokens with their corresponding dot tokens.  If the input is a sum, then check every term
in  the sum.
 */
fun replaceTokensInExpressionWithDotTokens(expr: Expr, replacementMap: Map<Token, Token>): Expr {

    println("replaceTokensInExpressionWithDotTokens(expr, map) expr = ${expr.toAnnotatedString()}: ${expr::class.simpleName}")
    if (expr is Sum) {
        val sum = Sum()

        expr.plusTerms.forEach { term ->
            println("replaceTokensInExpressionWithDotTokens replacing token for ${term.toAnnotatedString()})")
            sum.plusTerms.add(replaceTokenWithDotToken(term, replacementMap))
        }

        expr.minusTerms.forEach { term ->
            println("replaceTokensInExpressionWithDotTokens replacing token for ${term.toAnnotatedString()})")
            sum.minusTerms.add(replaceTokenWithDotToken(term, replacementMap))
        }
        return sum
    } else {
        return replaceTokenWithDotToken(expr, replacementMap)
    }
}

fun matchingStateExpressions(expr1: Expr, expr2: Expr): Boolean {

    if ( ! (isStateVariableExpr(expr1) && isStateVariableExpr(expr2))){
        return false
    }

    val expr1 = getTokenFromStateExpression(expr1)
    val expr2 = getTokenFromStateExpression(expr2)
    return expr1.equals(expr2)
}

class ExpressionAndSign(var expr: Expr, var sign: Sign)

fun getExpressionAndSign(expr: Expr): ExpressionAndSign {

    when(expr) {

        is Token -> return ExpressionAndSign(expr, POSITIVE)

        is Number -> return ExpressionAndSign(expr, POSITIVE)

        is Term -> {

            if (isStateVariableExpr (expr)) {
                val expressionAndSign = getExpressionAndSign(expr.numerators[0])
                if (expressionAndSign.sign == NEGATIVE) {
                    val term = Term()
                    term.numerators.add(expressionAndSign.expr)
                    term.numerators.add(expr.numerators[1])
                    return ExpressionAndSign(term, NEGATIVE)
                }
            }

            if (expr.numerators.size == 1 && expr.numerators[0] is Sum) {
                if ((expr.numerators[0] as Sum).plusTerms.size == 0) {
                    val newExpr: Expr
                    if ((expr.numerators[0] as Sum).minusTerms.size == 1) {
                        newExpr = (expr.numerators[0] as Sum).minusTerms[0]
                    } else {
                        val sum = Sum()
                        sum.plusTerms.addAll((expr.numerators[0] as Sum).minusTerms)
                        newExpr = sum
                    }
                    if (expr.denominators.size == 0){
                        return ExpressionAndSign(newExpr, NEGATIVE)
                    } else {
                        val term = Term()
                        term.numerators.add(newExpr)
                        term.denominators.addAll(expr.denominators)
                        return ExpressionAndSign(term, NEGATIVE)
                    }
                }
            }

            if (expr.denominators.size == 1 && expr.denominators[0] is Sum) {
                if ((expr.denominators[0] as Sum).plusTerms.size == 0) {
                    val newExpr: Expr
                    if ((expr.denominators[0] as Sum).minusTerms.size == 1) {
                        newExpr = (expr.denominators[0] as Sum).minusTerms[0]
                    } else {
                        val sum = Sum()
                        sum.plusTerms.addAll((expr.denominators[0] as Sum).minusTerms)
                        newExpr = sum
                    }
                    val term = Term()
                    term.numerators.addAll(expr.numerators)
                    term.denominators.add(newExpr)
                    return ExpressionAndSign(term, NEGATIVE)
                }
            }
            return ExpressionAndSign(expr, POSITIVE)
        }

        is Sum -> {
            if (expr.plusTerms.size == 0) {
                if (expr.minusTerms.size == 1){
                    return ExpressionAndSign(expr.minusTerms[0], NEGATIVE)
                } else {
                    val sum = Sum()
                    sum.plusTerms.addAll(sum.minusTerms)
                    return ExpressionAndSign(sum, NEGATIVE)
                }
            }
            return ExpressionAndSign(expr, POSITIVE)
        }
    }

    return ExpressionAndSign(Term(), POSITIVE) // hopefully an unreachable statement
}

class TokenAndTerm(var token: Token? = null, var term: Expr = Term())

fun getTokenAndTerm(expr: Expr): TokenAndTerm {

    when (expr) {

        is Token -> {
            if (expr.energyVar || expr.powerVar) {
                return TokenAndTerm(expr, Number(1.0))
            } else {
                return(TokenAndTerm(null, expr))
            }
        }

        is Number -> {
            return TokenAndTerm(null, expr)
        }

        is Term -> {
            val term = Term()
            val tokenAndTerm = TokenAndTerm()

            expr.numerators.forEach {
                if (it is Token && (it.powerVar || it.energyVar)) {
                    tokenAndTerm.token = it
                } else {
                    term.numerators.add(it)
                }
            }

            term.denominators.addAll(expr.denominators)

            tokenAndTerm.term = rationalizeTerm(term)
            return tokenAndTerm
        }

        is Sum -> {
            if (expr.plusTerms.size == 0 && expr.minusTerms.size == 1) {
                val tokenAndTerm = getTokenAndTerm(expr.minusTerms[0])
                val sum = Sum()
                sum.minusTerms.add(tokenAndTerm.term)
                return TokenAndTerm(tokenAndTerm.token, sum)
            } else {
                return TokenAndTerm(null, expr)
            }
        }
    }

    return TokenAndTerm(null, expr)
}

fun printExpr(expr: Expr, printStars: Boolean = true){
    if (printStars) {
        println("*** expr = ${expr.toAnnotatedString()}: ${expr::class.simpleName}")
    } else {
        println("    expr = ${expr.toAnnotatedString()}: ${expr::class.simpleName}")
    }
    if (expr is Term){
        expr.numerators.forEach { printExpr(it, false) }
        expr.denominators.forEach { printExpr(it, false) }
    }

    if (expr is Sum){
        expr.plusTerms.forEach { printExpr(it, false) }
        expr.minusTerms.forEach { printExpr(it, false) }
    }
}

class CoefficientAndExpr(val coefficient: Double, val expr: Expr)
/*
A Term may be made up of other Terms.  For example a Term could consist of a Token and another Term.
If the Token represents x and the other Term represents yz the whole Term represents xyz.  Most of
the time this is fine but causes a problem when we want to cancel like factors in the numerator and
denominator of a fraction (also a Term) If the numerator is made up of the above Term and the denominator
was say abz all made up of all Tokens, then we can't see the z in the numerator to cancel it. So this
function takes a Term (that is not a fraction) and expands it to an arraylist of Tokens and Sums.

Note: A Sum can also mask a single Token.  I call these hanging sums. But we don't deal with that here
since a Sum can also be controlling the sign of the whole Term.  These Sums crop up when simplifying
a Sum like (a + b -a). After simplifying we are left with a Sum with just one Token b. The simplifySums
function resolves these to single Tokens.  But as this code evolves, such sums may crop up in other areas.
 */

/*fun stripCoefficientFromList(source: ArrayList<Expr>, dest: ArrayList<Expr>, startNum: Double, operation: (Double, Double) -> Double): Double {
    var num = startNum
    source.forEach {
        var expr = it
        if (it is Sum && it.plusTerms.size == 1 && it.minusTerms.size == 0){
            expr = it.plusTerms[0]
        }

        if (it is Sum && it.plusTerms.size == 0 && it.minusTerms.size == 1){
            expr = it.minusTerms[0]
            num *= -1.0
        }
        if (it is Term && it.numerators.size == 1 && it.denominators.size == 0) {
            expr = it.numerators[0]
        }
        when (expr) {
            is Token -> dest.add(expr)
            is Number -> num = operation(num, expr.value)
            is Term -> {
                if (expr.numerators.size != 0 || expr.denominators.size != 0) {
                    val coefficientAndTerm = stripCoefficient(expr)
                    dest.add(coefficientAndTerm.expr)
                    num = operation(num, coefficientAndTerm.coefficient)
                }
            }
            is Sum -> dest.add(expr)
        }
    }
    return num
}*/

fun stripCoefficientFromList(source: ArrayList<Expr>, dest1: ArrayList<Expr>, dest2: ArrayList<Expr>, startNum: Double,
                             operation1: (Double, Double) -> Double, operation2: (Double, Double) -> Double): Double {
    var num = startNum
    source.forEach {
        var expr = it
        if (it is Sum && it.plusTerms.size == 1 && it.minusTerms.size == 0){
            expr = it.plusTerms[0]
        }

        if (it is Sum && it.plusTerms.size == 0 && it.minusTerms.size == 1){
            expr = it.minusTerms[0]
            num *= -1.0
        }
        if (it is Term && it.numerators.size == 1 && it.denominators.size == 0) {
            expr = it.numerators[0]
        }
        when (expr) {
            is Token -> dest1.add(expr)
            is Number -> num = operation1(num, expr.value)
            is Term -> {
                /*if (expr.numerators.size != 0 || expr.denominators.size != 0) {
                    val coefficientAndTerm = stripCoefficient(expr)
                    dest.add(coefficientAndTerm.expr)
                    num = operation(num, coefficientAndTerm.coefficient)
                }*/
                num = stripCoefficientFromList(expr.numerators, dest1, dest2, num, operation1, operation2)
                num = stripCoefficientFromList(expr.denominators, dest2, dest1, num, operation2, operation1)
            }
            is Sum -> dest1.add(expr)
        }
    }
    return num
}

fun getCoefficientAndExpr(term: Term): CoefficientAndExpr {

    val numerators = arrayListOf<Expr>()
    val denominators = arrayListOf<Expr>()
    var num = 1.0
    //println("getCoeefficientAndExpr term = ${term.toAnnotatedString()}")

    num = stripCoefficientFromList(term.numerators, numerators, denominators, num, Double::times, Double::div)
    num = stripCoefficientFromList(term.denominators, denominators, numerators, num, Double::div, Double::times)
    //println("numeratrs")

    //numerators.forEach { println ("${it.toAnnotatedString()}: ${it::class.simpleName}") }
    //println("denominators")
    //denominators.forEach { println ("${it.toAnnotatedString()}: ${it::class.simpleName}") }

    eliminateCommonTerms(numerators, denominators, num)

    //println("numeratrs")
    //numerators.forEach { println ("${it.toAnnotatedString()}: ${it::class.simpleName}") }
    //println("denominators")
    //denominators.forEach { println ("${it.toAnnotatedString()}: ${it::class.simpleName}") }

    if (numerators.size == 1 && denominators.isEmpty()){
        return CoefficientAndExpr(num,numerators[0])
    }

    val newTerm = Term()
    /*if (num != 1.0) {
        newTerm.numerators.add(Number(num))
    }*/
    newTerm.numerators.addAll(numerators)
    newTerm.denominators.addAll(denominators)
    //println("num = $num,  newTerm= ${newTerm.toAnnotatedString()}: ${newTerm::class.simpleName}")
    return CoefficientAndExpr(num, newTerm)
}

/*
    Take a term and strip off the number coefficient and return a CoefficientAndTerm containing the value
    of the coefficient and the rest of the term.  If the rest of the program is working correctly,
    the coefficient should be the first item in the numerator.  But, this function searches all
    items in both the numerator and denominator just in case.  In addition, if more than one
    number is found we calculate a new number by multiplying/dividing the numbers together.
 */
fun stripCoefficient(term: Term): CoefficientAndExpr{

    val numerators = arrayListOf<Expr>()
    val denominators = arrayListOf<Expr>()
    var num = 1.0


    num = stripCoefficientFromList(term.numerators, numerators, denominators, 1.0, Double::times, Double::div)
    num = stripCoefficientFromList(term.denominators, denominators, numerators, num, Double::div, Double::times)
    num = eliminateCommonTerms(numerators, denominators, num)

    val term = Term()
    term.numerators.addAll(numerators)
    term.denominators.addAll(denominators)
    if (term.numerators.size == 1 && term.denominators.size == 0){
        return CoefficientAndExpr(num, term.numerators[0])
    }
    return CoefficientAndExpr(num, term)
}

/*fun getExprFromCoefficientAndExpr_save(coefficientAndTerm: CoefficientAndExpr): Expr{

    val expr = coefficientAndTerm.expr
    val num = coefficientAndTerm.coefficient


    if ((expr is Term && expr.numerators.size + expr.denominators.size == 0) ||
        (expr is Sum && expr.plusTerms.size + expr.minusTerms.size == 0)){
        if (num >= 0){
            return Number(num)
        } else {
            val sum = Sum()
            sum.minusTerms.add(Number(-num))
            return sum
        }
    }

    val term = Term()
    term.numerators.add(Number(num.absoluteValue))
    term.numerators.add(expr)
    if (num < 0){
        val sum = Sum()
        sum.minusTerms.add(term)
        return sum
    }

    return term
}*/

fun getExprFromCoefficientAndExpr(coefficientAndExpr: CoefficientAndExpr): Expr {
    val expr = coefficientAndExpr.expr
    val num = coefficientAndExpr.coefficient

    if (num == 0.0) return Number(0.0)

    when (expr) {
        is Number -> {return Term()} //won't be a Number

        is Token -> {
            if (num == 1.0) return expr
            val term = Term()
            term.numerators.add(Number(num.absoluteValue))
            term.numerators.add(expr)
            if (num < 0){
                /*val sum = Sum()
                sum.minusTerms.add(term)
                return sum*/
                return createNegativeExpression(term)
            }
            return term
        }

        is Term -> {

            if (expr.numerators.size + expr.denominators.size == 0){
                if (num > 0.0) {
                    return Number(num)
                } else {
                    /*val sum = Sum()
                    sum.minusTerms.add(Number(num.absoluteValue))
                    return sum*/
                    return createNegativeExpression(Number(num.absoluteValue))
                }
            }
            //if (num == 1.0) return expr
            if (expr.numerators.size == 0){
                expr.numerators.add(Number(num.absoluteValue))
            } else {
                if (num.absoluteValue != 1.0) {
                    expr.numerators.add(0,Number(num.absoluteValue))
                }
            }

            if (num < 0){
                /*val sum = Sum()
                sum.minusTerms.add(expr)
                return sum*/
                return createNegativeExpression(expr)
            }
            return expr
        }

        is Sum -> {
            if (expr.plusTerms.size + expr.minusTerms.size == 0){
                if (num > 0.0) {
                    return Number(num)
                } else {
                   /* val sum = Sum()
                    sum.minusTerms.add(Number(num.absoluteValue))
                    return sum*/
                    return createNegativeExpression(Number(num.absoluteValue))
                }
            }
            if (num == 1.0) return expr
            val sum = Sum()
            if (num >= 0) {
                sum.plusTerms.addAll(expr.plusTerms)
                sum.minusTerms.addAll(expr.minusTerms)
            } else {
                sum.plusTerms.addAll(expr.minusTerms)
                sum.minusTerms.addAll(expr.plusTerms)
            }
            val term = Term()
            term.numerators.add(Number(num.absoluteValue))
            term.numerators.add(sum)
            return term
        }
    }
    return Term()
}

fun negate(sum: Sum): Expr {
    /*
    Switch the plusterms and the minusterms.  Effectively multiplies the sum by -1.
     */
    val newSum = Sum()
    newSum.plusTerms.addAll(sum.minusTerms)
    newSum.minusTerms.addAll(sum.plusTerms)

    if (newSum.plusTerms.size == 1 && newSum.minusTerms.isEmpty()){
        return newSum.plusTerms[0]
    }
    return newSum
}

/*
Take the reciprocal of the expression
 */

fun reciprocal (expr: Expr): Expr {
    val term = Term()

    when (expr) {
        is Token -> term.denominators.add(expr)
        is Number -> term.denominators.add(expr)
        is Term -> {
            term.numerators.addAll(expr.denominators)
            term.denominators.addAll(expr.numerators)
        }
        is Sum -> {
            val newExpr = convertSumToCommonDenominator(expr)
            return reciprocal(newExpr)
        }
    }

    return term
}

fun checkForNegativeTerm(term: Term): Expr {

    /*
    If this term is a negative sum divided by a negative sum then return a positive term.
    If the term has a negative sum in either the numerator or denominator, then return a negative sum of the term.
    I.e. -a/-b becomes a/b  and a/-b would become -(a/b)
     */

    val newTerm = Term()
    val sum = Sum()

    if ( term.numerators.size == 1 && term.numerators[0] is Sum) {
        if ((term.numerators[0] as Sum).plusTerms.size == 0){
            if (term.denominators.size ==1 && term.denominators[0] is Sum) {
                if ((term.denominators[0] as Sum).plusTerms.size ==0) {
                    newTerm.numerators.add(negate(term.numerators[0] as Sum))
                    newTerm.denominators.add(negate(term.denominators[0] as Sum))
                    return newTerm
                }
            } else {
                newTerm.numerators.add(negate(term.numerators[0] as Sum))
                newTerm.denominators.addAll(term.denominators)
                sum.minusTerms.add(newTerm)
                return sum
            }
        }
    }

    if (term.denominators.size ==1 && term.denominators[0] is Sum) {
        if ((term.denominators[0] as Sum).plusTerms.size == 0) {
            newTerm.numerators.addAll(term.numerators)
            newTerm.denominators.add(negate(term.denominators[0] as Sum))
            sum.minusTerms.add(newTerm)
            return sum
        }
    }

    newTerm.numerators.addAll(term.numerators)
    newTerm.denominators.addAll(term.denominators)
    return newTerm
}

fun rationalizeTerm (term: Expr): Expr {
    if (term !is Term) {
        return term
    }
    val coefficientAndExpr = getCoefficientAndExpr(term)
    val expr = getExprFromCoefficientAndExpr(coefficientAndExpr)
    println("rationalizeTerm(term) term = ${term.toAnnotatedString()}, expr = ${coefficientAndExpr.expr.toAnnotatedString()}, coeff = ${coefficientAndExpr.coefficient}, new expression = ${expr.toAnnotatedString()}")
    if (expr is Term) {
        return checkForNegativeTerm(expr)
    }

    return expr
}

/*
    Go through the sum and combine like terms, including numbers.  So (a + b + a + 3 -2b -5 ) becomes (2a - b - 2)
 */
fun combineTerms(sum: Sum): Expr {

    val plusTerms = arrayListOf<Expr>()
    val minusTerms = arrayListOf<Expr>()
    val termValueMap = hashMapOf<Expr, Double>()
    var number: Double
    var foundOne = false

    /*
    Check every expression in the start list.  If the expression is a Number, add/subtract it (depending on the operation
    parameter) from a running total that starts with startNum.  Make sure to check Sums, and Terms to see if they might
    actually be a single Number. If the expression is not a Number, add it to the dest list. The end result is a new
    list of expressions and one number that is the total. I.e. change (x + 2 - R - 3) to (-1 + x - R)
     */
    fun checkForNumbers(start: ArrayList<Expr>, dest: ArrayList<Expr>,  startNum: Double, operation: (Double, Double) -> Double): Double {

        var num = startNum
        start.forEach {
            when (it){
                is Token -> dest.add(it)

                is Number -> num = operation(num, it.value)

                is Term -> {
                    if (it.numerators.size == 1 && it.denominators.size == 0 && it.numerators[0] is Number) {
                        num = operation(num, (it.numerators[0] as Number).value)
                    } else {
                        if (it.numerators.size == 0 && it.denominators.size == 1 && it.denominators[0] is Number) {
                            num = operation (num, 1.0/((it.denominators[0] as Number).value))
                        } else {
                            if (it.numerators.size + it.denominators.size == 0){
                                num = operation(num, 1.0)
                            } else {
                                dest.add(it)
                            }
                        }
                    }
                }

                is Sum -> {
                    if (it.plusTerms.size == 1 && it.minusTerms.size == 0 && it.plusTerms[0] is Number){
                        num = operation(num, (it.plusTerms[0] as Number).value)
                    } else {
                        if (it.plusTerms.size == 0 && it.minusTerms.size == 1 && it.minusTerms[0] is Number) {
                            num = operation(num, ( - (it.minusTerms[0] as Number).value))
                        } else {
                            dest.add(it)
                        }
                    }
                }
            }
        }

        return num
    }
    /*
    For every expression in the list, calculate a single coefficient for that expression, depending on
    the operation parameter.  for list (a, c, b, 1.5a, c, -2b) and operation is plus we would get map
    key  value
    a     2.5
    b     -1
    c      2
     */
    fun processTerms(exprs: ArrayList<Expr>,operation: (Double, Double) -> Double){

        var value1: Double

        for (expr in exprs) {
            var expr1 = expr
            value1 = 1.0
            if (expr is Term && ! isStateVariableExpr(expr)){
                val coefficientAndTerm = stripCoefficient(expr)
                expr1 = coefficientAndTerm.expr
                value1 = coefficientAndTerm.coefficient
            }
            foundOne = false
            for (expr2 in termValueMap.keys) {
                if (expr1.equals(expr2)) {

                    foundOne = true
                    val value2 = termValueMap[expr2]
                    termValueMap[expr2] = operation(value2!!, value1)
                }

            }
            if (!foundOne) {
                println("processTerms foundone = $foundOne, expr1 = ${expr1.toAnnotatedString()}")
                termValueMap[expr1] = operation(0.0, value1)
            }
        }
    }

    println("combineTerm(sum) sum = ${sum.toAnnotatedString()}")

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return Number(0.0)
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.size == 0){
        return sum.plusTerms[0]
    }

    if (sum.plusTerms.size == 0 && sum.minusTerms.size == 1){
        return sum
    }

    number = checkForNumbers(sum.plusTerms, plusTerms, 0.0, Double::plus)
    number = checkForNumbers(sum.minusTerms, minusTerms, number, Double::minus)
    if (plusTerms.size + minusTerms.size == 0){
        // Sum reduced to single number, so return that.
        if (number >= 0) {
            return Number(number)
        } else {
            /*val sum = Sum()
            sum.minusTerms.add(Number(-number))
            return sum*/
            return createNegativeExpression(Number(-number))
        }
    }

    if (plusTerms.size == 1 && minusTerms.size == 0 && number == 0.0){
        // Sum reduced to to single plusTerm so return that.
        return plusTerms[0]
    }

    if (plusTerms.size == 0 && minusTerms.size == 1 && number == 0.0){
        // Sum reduced to to single minusTerm so return that.
        val sum = Sum()
        sum.minusTerms.add(minusTerms[0])
        return sum
    }

    processTerms(plusTerms, Double::plus)
    processTerms(minusTerms, Double::minus)

    /*
    Go through the map and create terms based on the key value pair. Add the term to Sum. I.e.
    key a and value 3 becomes Term 3a.
     */
    val sum = Sum()
    termValueMap.forEach{(key, value) ->
        //println("key = ${key.toAnnotatedString()}  value = $value")
        if (value != 0.0) {
            var expr: Expr
            if (value == 1.0 || value == -1.0) {
                expr = key
            } else {
                expr = Term()
                expr.numerators.add(Number(value.absoluteValue))
                if (key is Term){
                    (expr as Term).numerators.addAll(key.numerators)
                    (expr as Term).denominators.addAll(key.denominators)
                } else {
                    expr.numerators.add(key)
                }
            }
            //println("expr = ${expr.toAnnotatedString()}: ${expr::class.simpleName}")
            if (value < 0) {
                sum.minusTerms.add(expr)
            } else {
                sum.plusTerms.add(expr)
            }
        }
    }

    // Add the number to the Sum.
    when {
        number == 0.0 -> {}
        number > 0.0 -> sum.plusTerms.add(Number(number))
        number < 0.0 -> sum.minusTerms.add(Number(-number))
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.size == 0){
        return sum.plusTerms[0]
    }

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return Number(0.0)
    }
    return sum
}
fun expandTerm(term: Term): ArrayList<Expr> {

    val newTerms = arrayListOf<Expr>()

    if (term.denominators.size > 0 ){
        return newTerms
    }

    // Add everything in the numerators to the new list. If you find another Term expand it too.
    term.numerators.forEach {
        if (it is Term ) {
            // If we find another Term call ourself recursively to expand it also.
            newTerms.addAll(expandTerm(it))
        } else {
            newTerms.add(it)
        }
    }
    return newTerms
}

fun eliminateCommonTerms(list1: ArrayList<Expr>, list2: ArrayList<Expr>, startNum: Double ): Double{

    val copy = arrayListOf<Expr>()
    var num = startNum



    copy.addAll(list1)

    copy.forEach { expr ->
        var term = list2.find { t -> expr.equals(t) }

        if (term != null) {
            println("eliminateCommonTerms(list, list) found common term = ${term.toAnnotatedString()}")
            list1.remove(expr)
            list2.remove(term)
        } else {
            if (expr is Sum) {
                val negExpr = negate(expr)
                term = list2.find{ t -> negExpr.equals(t)}
                if (term != null) {
                    println("eliminateCommonTerms(list, list) found negative common term = ${term.toAnnotatedString()}")
                    list1.remove(expr)
                    list2.remove(term)
                    num *= -1.0
                }
            }
        }
    }
    return num
}
/*
This function cancels like factors in the numerator and denominator of a fraction. Basically, create
lists of all the Tokens and Sums in the numerator and denominator and then get rid of any that occur
in both lists.
 */
fun cancel(term: Expr): Expr{
    val numerators = arrayListOf<Expr>()
    val denominators = arrayListOf<Expr>()
    val copyOfNumerators = arrayListOf<Expr>()

    //println("cancel on ${term.toAnnotatedString()}")
    if (term !is Term) {
        return term
    }
/*
 Go through items in the numerator and denominators of the Term. Store all the Tokens and Sums
 in separate lists.  Expand any Terms that are found and add their Tokens and Sums to the lists.
 */
    term.numerators.forEach {
        //println("n* ${it.toAnnotatedString()}: ${it::class.simpleName}")
        if (it is Term) {
            numerators.addAll(expandTerm(it))
        } else {
            numerators.add(it)
        }
    }

    term.denominators.forEach {
        //println("d* ${it.toAnnotatedString()}: ${it::class.simpleName}")
        if (it is Term) {
            denominators.addAll(expandTerm(it))
        } else {
            denominators.add(it)
        }
    }
    //println("numerators=")
    //numerators.forEach { println("${it.toAnnotatedString()}") }
    //println("denominators=")
    //denominators.forEach { println("${it.toAnnotatedString()}") }

    copyOfNumerators.addAll(numerators)


    /*
    Iterate over copyOfNumerators because you can't modify a list you are iterating over. For every
    expression in numerators see if it exists in denominators. If so delete it from both lists. Use
    our own .equals() functions because different objects may be equal as far as we are concerned
    i.e. (a + b) would equal (b + a).  Make sure to delete the correct object from the appropriate list.
     */
    /*copyOfNumerators.forEach {expr ->
        val denominator = denominators.find{d -> expr.equals(d)}
        if (denominator != null) {
            numerators.remove(expr)
            denominators.remove(denominator)
        }
    }*/

    val num = eliminateCommonTerms(numerators, denominators, 1.0)
    // Create a new Term from the left over numerators and denominators.
    val newTerm = Term()
    newTerm.numerators.addAll(numerators)
    newTerm.denominators.addAll(denominators)

    if (num < 0){
        val sum = Sum()
        sum.minusTerms.add(newTerm)
        return sum
    }
    return newTerm
}
/*
 Iterate over a given list of Expr, and create a new list of the Exprs where cancel has be called on every Expr
 that is a Term
 */
fun callCancelOnList(source: ArrayList<Expr>): ArrayList<Expr>{

    val newList = arrayListOf<Expr>()

    for (expr in source) {
        if (expr is Term) {
            newList.add(cancel(expr))
        } else {
            newList.add(expr)
        }
    }
    return newList
}
/*
The right side of an equation is either a Term or a Sum of Terms. Return a new equation where cancel
has been called on every Term on the right side.
 */
fun cancel(equation: Equation): Equation {
    val newPlusTerms = arrayListOf<Expr>()
    val newMinusTerms = arrayListOf<Expr>()

    if (equation.rightSide is Token || equation.rightSide is Number){
        return equation
    }

    if (equation.rightSide is Term) {
        return Equation(equation.leftSide, cancel(equation.rightSide as Term))
    }
    // Call cancel on all the plus terms and then on all the minus terms.
    newPlusTerms.addAll(callCancelOnList((equation.rightSide as Sum).plusTerms))
    newMinusTerms.addAll(callCancelOnList((equation.rightSide as Sum).minusTerms))

    // create new Sum with the canceled terms.
    val sum = Sum()
    sum.plusTerms.addAll(newPlusTerms)
    sum.minusTerms.addAll(newMinusTerms)

    return Equation(equation.leftSide, sum)
}

/*
The following function simplifies the Sums in all the terms of the right side
of the given Equation. Simplifying a Sum means taking something like this
(a + b  -a + c) and turning it into something like this (b + c)
 */
fun simplifySums(equation: Equation): Equation {

    val plusTerms = arrayListOf<Expr>()
    val minusTerms = arrayListOf<Expr>()
    val newPlusTerms = arrayListOf<Expr>()
    val newMinusTerms = arrayListOf<Expr>()

    // No Sum to simplify.
    if (equation.rightSide is Token || equation.rightSide is Number){
        return equation
    }

    // Build lists of plus and minus terms.
    if (equation.rightSide is Term){
        plusTerms.add(equation.rightSide)
    } else {
        plusTerms.addAll((equation.rightSide as Sum).plusTerms)
        minusTerms.addAll((equation.rightSide as Sum).minusTerms)
    }

    /*
     Call simplifySum an all the Terms in our lists, building new list of
     the simplified terms.  A couple of things to note.

     The checkForHangingSums function modifies its input lists.

     The lists are reversed in the call in the second loop.  This is because
     a plus factor from the minus terms stays in the minus terms.  But a negative
     factor from the minus terms -(-a) belongs in the plus terms.
     */
    for (term in plusTerms){
        resolveHangingSums(simplifySum(term), newPlusTerms, newMinusTerms)
    }
    for (term in minusTerms){
        resolveHangingSums(simplifySum(term), newMinusTerms, newPlusTerms)
    }

    // Create a new Sum from the new lists.
    val sum = Sum()
    sum.plusTerms.addAll(newPlusTerms)
    sum.minusTerms.addAll(newMinusTerms)

    // If sum is a positive hanging sum then resolve it.
    if (sum.plusTerms.size == 1 && sum.minusTerms.size == 0){
        val term = Term()
        term.numerators.add(sum.plusTerms[0])
        return Equation(equation.leftSide, term)
    }

    return Equation(equation.leftSide, sum)
}

/*
    The next two functions deal with what I call hanging sums.  When you simplify a Sum like Sum(a + b - a)
    you are left with a Sum that has just one factor in it Sum(b).  Internally a hanging Sum is a Sum that
    contains just one Term or Token in either its plusTerms list or its minusTerms list and nothing it the
    other list. This sum should be reduced to a Term or a Token.  The reason hanging sums are a problem is
    if you have an expression ab/cb you would like to be able to cancel the b.  But if the expression is
    constructed as aSum(b)/cb you can't see the b in the numerator to do the cancelling. Simplifying
    expressions with hanging sums is tricky because you may have a situation like Sum(a - b - a) that was
    reduced to Sum(-b).  This Sum may now be setting the sign for an entire expression. If you have an
    expression aSum(-b)/xy when you simplify this to ab/xy you need to move the entire expression into
    the minusTerms of whatever Sum it is part of or create a new Sum(-(ab/xy)).

    This first resolveHangingSums function takes a source list of Expressions and builds a new list that is
    the same as the source list except any hanging sums have been resolved to their base Term or Token. The
    source list will be the numerators or denominators of some other expression.  The caller needs
    to keep track if the sign of the expression has changed because of negative Sum.  So the caller
    provides an initial value for the isPlusTerm flag.  Every time this function finds a negative hanging
    sum it toggles this flag and returns it when it is finished.
 */
fun resloveHangingSums(source: ArrayList<Expr>, dest: ArrayList<Expr>, isPlusTerm: Boolean): Boolean {

    var localIsPlusTerm = isPlusTerm


    // Check every expression in the list to see if it is a hanging sum
    for (expr in source) {
        if (expr is Sum) {
            when {
                expr.plusTerms.size == 1 && expr.minusTerms.size == 0 -> {
                    // positive hanging sum
                    dest.add(expr.plusTerms[0])
                }
                expr.plusTerms.size == 0 && expr.minusTerms.size == 1 -> {
                    // negative hanging sum
                    dest.add(expr.minusTerms[0])
                    localIsPlusTerm = ! localIsPlusTerm
                }
                else -> {
                    // normal sum
                    dest.add(expr)
                }
            }
        } else {
            // an expression that is not a sum
            dest.add(expr)
        }
    }
    return localIsPlusTerm
}

/*
    Examine the given expression and resolve any hanging terms.  If the Expression is a fractions then
    resolve all hanging sums in both the numerator and denominator.  If the resolved expression is
    positive add it to the newPlusTerms list, otherwise add it to the newMinusTerms list.
 */
fun resolveHangingSums(expr: Expr, newPlusTerms: ArrayList<Expr>, newMinusTerms: ArrayList<Expr>) {

    val numerators = arrayListOf<Expr>()
    val denominators = arrayListOf<Expr>()
    val newNumerators = arrayListOf<Expr>()
    val newDenominators = arrayListOf<Expr>()
    var isPlusTerm = true

    fun addIt(expr:Expr){
        if (isPlusTerm) {
            newPlusTerms.add(expr)
        } else {
            newMinusTerms.add(expr)
        }
    }
    //Build lists for the terms in the numerator and denominator.
    if (expr !is Term) {
        // no denominator, just one expression to add
        numerators.add(expr)
    } else {
        numerators.addAll((expr as Term).numerators)
        denominators.addAll(expr.denominators)
    }

    // Resolve hanging sums in both list keeping track of the sign of the expression.
    isPlusTerm = resloveHangingSums(numerators, newNumerators, true)
    isPlusTerm = resloveHangingSums(denominators,newDenominators, isPlusTerm)


    // Create a new term from the new resolved numerators and denominators and add it to
    // either the newPlusTerms list or the new minusTermsList.
    var term = Term()
    term.numerators.addAll(newNumerators)
    term.denominators.addAll(newDenominators)
    val coefficientAndExpr = getCoefficientAndExpr(term)
    if (coefficientAndExpr.coefficient < 0){
        isPlusTerm = ! isPlusTerm
    }
    val newExpr = coefficientAndExpr.expr
    val num = coefficientAndExpr.coefficient

    if (newExpr is Term){
        if (coefficientAndExpr.coefficient == 1.0){
            if (newExpr.numerators.size == 0){
                newExpr.numerators.add(Number(1.0))
            }
        } else {
            newExpr.numerators.add(Number(coefficientAndExpr.coefficient.absoluteValue))
        }
        addIt(newExpr)
    } else {
        if (num == 1.0){
            addIt(newExpr)
        } else {
            term = Term()
            term.numerators.add(newExpr)
            term.add(Number(num))
            addIt(term)
        }
    }

}


/*
    The next two functions are used for expanding Sums. This means to get rid of nested Sums in a Sum. For
    example Sum( a + Sum( b + c) will be expanded to Sum(a + b + c)
    In the following functions the Sum class is used as a convenient data class to hold two lists, one
    of plus terms, and one of minus terms.
 */

/*
    This functions expands a Sum by looking for Sums in its plus term list and in its minus term list.
    Note that expandSum(Sum) calls expandSum(List) which may call expandSum(Sum) recursively.
 */
fun expandSum(sum: Sum): Sum {

    val plusTerms = arrayListOf<Expr>()
    val minusTerms = arrayListOf<Expr>()

    var newSum: Sum

    /*
    Expand the plus terms and the minus terms.
    Note that expanding the plus terms may produce plus terms and minus terms.
    Same with the minus terms.
    Note: the plus terms list produced from the minus terms should be added to
    our minus terms list and vice versa.
    */
    newSum = expandSum(sum.plusTerms)
    plusTerms.addAll(newSum.plusTerms)
    minusTerms.addAll(newSum.minusTerms)

    newSum = expandSum(sum.minusTerms)
    plusTerms.addAll(newSum.minusTerms)
    minusTerms.addAll(newSum.plusTerms)

    newSum = Sum()
    newSum.plusTerms.addAll(plusTerms)
    newSum.minusTerms.addAll(minusTerms)

    return newSum
}

/*
    Check every expression in the given list and expand any Sums found.  Since expanding a
    Sum will produce two list, one for plus terms and one for minus terms we use a Sum instance
    to return the two lists.
 */
fun expandSum (source: ArrayList<Expr>): Sum {

    val plusTerms = arrayListOf<Expr>()
    val minusTerms = arrayListOf<Expr>()

    for (expr in source){
        if (expr is Sum){
            // Recursive call to expandSum(Sum)
            var sum : Sum = expandSum(expr)
            plusTerms.addAll(sum.plusTerms)
            minusTerms.addAll(sum.minusTerms)

        } else {
            plusTerms.add(expr)
        }
    }

    val sum = Sum()
    sum.plusTerms.addAll(plusTerms)
    sum.minusTerms.addAll(minusTerms)
    return sum
}

/*
    The following function simplifies a Sum.  To simplify a sum means to take something
    like (a + b + c -a) and change it to (b + c).  If the given expression is fraction
    with a single Sum in the numerator then return the fraction with the numerator simplified.
 */
fun simplifySum(expr: Expr ): Expr {

    val copyOfPlusTerms = arrayListOf<Expr>()
    val copyOfMinusTerms = arrayListOf<Expr>()
    var sum: Sum

    if (expr is Token || expr is Number) {
        // nothing to simplify
        return expr
    }


    // before we can simplify we must expand the Sum.
    if (expr is Term) {
        if (expr.numerators.size == 1 && expr.numerators[0] is Sum) {
            sum = expandSum(expr.numerators[0] as Sum)
        } else {
            // We may want to extend this function to handle Term x Sum
            // i.e ab(a + b  + c -a) could be ab(b + c) But hanging sums
            // would complicate this.
            return expr
        }
    } else {
        sum = expandSum(expr as Sum)

    }

    if (sum.plusTerms.size == 0  || sum.minusTerms.size == 0){
        // Nothing to add or subtract
        return expr
    }

    // Need copy because we can't modify list we are iterating over.
    copyOfPlusTerms.addAll(sum.plusTerms)

    /*
    For each term in the plus terms list see if there is a matching term
    in the minus term list.  If there is, remove the term from both lists.
    Make a new copy of the minus terms list on each iteration because a
    term might occur twice in the plus terms but only once in the minus
    terms. Use our Expr.equals() function for comparisons.  Be sure to
    remove the correct objects from the correct list since they may not
    be equal from an object point of view.
    */
    for (e1 in copyOfPlusTerms){
        copyOfMinusTerms.clear()
        copyOfMinusTerms.addAll(sum.minusTerms)
        for (e2 in copyOfMinusTerms){
            if (e1.equals(e2)) {
                sum.plusTerms.remove(e1)
                sum.minusTerms.remove(e2)
            }
        }
    }

    if (expr is Term) {
        val term = Term()
        term.numerators.add(sum)
        term.denominators.addAll(expr.denominators)
        return term
    }

    return sum
}

/*
    This function checks to see if the Token occurs anywhere in the denominator
    of the expression.  This program solves equations for certain Tokens.  It
    currently can't solve an equation if the Token occurs in a denominator.
 */
fun isTokenInDenominator(token: Token, expr: Expr): Boolean {

    /*
    If the expression is a term then check every expression in its denominator.  If
    this new expression is a token, and it matches the input token then return true.
    If it not a token then call ourself recursively.
     */

    if (expr is Term) {
        for (ex in expr.denominators){
            if (ex is Token && ex === token) {
                return true
            }
            if ( isTokenInDenominator(token, ex)) {
                return true
            }
        }
    }

    // If expression is a Sum then call ourself recursively on every term  in the Sum.
     if (expr is Sum){
         val exprsList = expr.getAllExpressions()
         for (ex in exprsList) {
             if (isTokenInDenominator(token, ex)) {
                 return true
             }
         }
     }

    return false
}

/*
    Check t see if the token occurs in the numerator of the expression.
 */
fun contains(token: Token, expr: Expr): Boolean {
    return if (expr is Term) expr.getNumeratorTokens().contains(token) else false
}

/*
    The following function expands the product or a Term and a Sum.
    i.e ab(x + y)  to (abx + aby)
    If expr is a fraction, the expand the numerator.
 */
fun expandProductOfSumAndTerm(expr: Expr): Expr {

    val termList = arrayListOf<Expr>()
    val sumList = arrayListOf<Expr>()
    val plusNumerators = arrayListOf<Expr>()
    val minusNumerators = arrayListOf<Expr>()

    // create a Term that is the product of expr and all the Terms in the termList
    fun productTerm(expr: Expr): Expr{
        val term = Term()
        term.numerators.addAll(termList)
        term.numerators.add(expr)
        return term
    }


    if (expr !is Term){
        // nothing to expand
        return expr
    }



    // break expr apart into a list of Terms and a list o Sums.
    for (e in (expr).numerators){
        if (e is Sum) {
            sumList.add(e)
        } else {
            termList.add(e)
        }
    }

    if (sumList.size != 1) {
        // This function can't handle multiplying two or more sums together.
        return expr
    }

    // Expand any nested sums
    var sum = expandSum(sumList[0] as Sum)

    // multiply each Term in the Sum by the Terms outside the Sum and
    // store the new terms in the plusNumerators and minusNumerators lists.
    sum.plusTerms.forEach { plusNumerators.add(productTerm(it)) }
    sum.minusTerms.forEach { minusNumerators.add(productTerm(it)) }

    // Create a new expanded Sum from the plus and minus lists.
    sum = Sum()
    sum.plusTerms.addAll(plusNumerators)
    sum.minusTerms.addAll(minusNumerators)

    // If the original expression was a fraction return a new fraction
    // with the expanded Sum over the original denominator.
    if (expr is Term && expr.denominators.size > 0) {
        val term = Term()
        term.numerators.add(sum)
        term.denominators.addAll(expr.denominators)
        return term
    }
    // Otherwise return the expanded Sum.
    return sum
}

/*
    Take the expression and convert it to a equivalent expression with a denominator
    equal to the common denominator.  This is done by multiplying the numerator by all
    the terms in the common denominator that are not in the expression's denominator.
    example expression ab/xy  common denominator xymn  new expression abmn/xymn. This
    function just calculates and returns the numerator part 
 */
fun convertExpressionNumeratorToCommonDenominator(expr: Expr, commonDenominator: List<Expr>): Expr {

    val copyOfCommonDenominator = arrayListOf<Expr>()

    if (expr !is Term) {
        // no denominator. Multiply the entire expression by the common denominator
        val term = Term()
        term.numerators.add(expr)
        term.numerators.addAll(commonDenominator)
        return expandProductOfSumAndTerm(term)
    }
    
    // Make a copy of the common denominator.  Then remove each term in the expression's 
    // denominator from the copy of the common denominator.  What's left is what we need
    // to multiply the numerator by.    
    copyOfCommonDenominator.addAll(commonDenominator)
    for (term in (expr as Term).denominators) {
        if (copyOfCommonDenominator.containsExpr(term)) {
            copyOfCommonDenominator.remove(term)
        }
    }

    // Create the new numerator and return the expanded form of it.
    val term = Term()
    term.numerators.addAll(expr.numerators)
    term.numerators.addAll(copyOfCommonDenominator)

    return expandProductOfSumAndTerm(term)
}
/*
The two input lists represent expressions whose product for a denominator of a fraction.  The
output list would represent the common denominator of the two fractions.  A common denominator
consists of every expression in one list plus expressions from the second list that aren't in
the first list.  Ex: {xyz}  {xxya} common list {xxyza} y only appears once since it is in both lists.
x must appear twice because it appears twice in the second list.
 */
fun commonDenominator(list1: List<Expr>, list2: List<Expr>): List<Expr> {

    val newList = ArrayList<Expr>()
    val copyList = ArrayList<Expr>() //we need another copy that can be altered as we loop through the second list

    newList.addAll(list1) // start with everything in list1
    copyList.addAll(list1) // we need another copy that can be altered as we loop through the second list

    // Loop through the expressions in the second list. If the expression is already in copyList then
    // delete it from copyList. Otherwise, add it to newList. By deleting expressions from copyList we make
    // sure any expressions that occur in list2 more often than they appear in list1 get added appropriately.
    list2.forEach { expr ->
        if (copyList.contains(expr)) {
            copyList.remove(expr)
        } else {
            newList.add(expr)
        }
    }

    return newList
}

/*
When you add two fractions, you must first find a common denominator. Then for each fraction you must multiply
the numerator by the amount of the common denominator that is not part of its own denominator.
Ex:  y/ab + x/bc  common denominator is abc.  First fraction numerator must be multiplied c. Second numerator
must be multiplied by a. So we get (yc + xa)/abc
This function takes a list of expressions that represents a denominator of a fraction and a list that represents
a common denominator, wnd returns an expression that can be used multiply the numerator of the fraction.
 */
fun getNumeratorFactor(denominator: List<Expr>, commonDenominator: List<Expr>): Expr {

    fun errorMessage (expr: Expr): String {
        val msg = StringBuilder()
            .append("Attempt to calculate a numerator factor from a denominator that contains a term not in the common denominator. Denominator = .")
        denominator.forEach {expr ->
            msg.append(expr.toAnnotatedString())
            msg.append(", ")
        }
        msg.append("common denominator = ")
        commonDenominator.forEach { expr ->
            msg.append(expr.toAnnotatedString())
            msg.append(", ")
        }
        msg.append("missing term = ${expr.toAnnotatedString()}")
        return msg.toString()
    }

    val leftOverTerms = ArrayList<Expr>()

    leftOverTerms.addAll(commonDenominator)  //start with all the expressions in the common denominator.

     denominator.forEach {expr ->

          // Throw an error if we find an expression that is not part of the common denominator
         if ( ! leftOverTerms.contains(expr)) {
             throw (AlgebraException(errorMessage(expr)))
         }
         // Delete the expression from the leftOverTerms list.
         leftOverTerms.remove(expr)
     }

    // If leftover terms is empty, then the denominator is the same as the common denominator. So there
    // is no reason multiply the numerator, the same as multiplying the numerator by 1.
    if (leftOverTerms.size == 0){
        return Number(1.0)
    }

    // if there is just one expression left in the list then just return that expression
    if (leftOverTerms.size == 1){
        return leftOverTerms[0]
    }

    // Return a term that is a product of all the expressions left in the list.
    val term = Term()
    term.numerators.addAll(leftOverTerms)
    return term
}

/*
The purpose of the function is to return a term that is the numerator of a fraction.
We keep it general by accepting an expression as input.  This way we can use it in
general algorithms without having extra logic to check the input.  So the numerator
of x is the same as the numerator of x/1 or just x.  The numerator of xy/ab is xy.
*/
fun getNumerator(expr: Expr): Expr {

    when(expr) {
        is Token -> return expr.clone()
        is Number -> return expr.clone()
        is Term -> {
            if (expr.numerators.size == 0) return Number(1.0)  // something like 1/x
            if (expr.numerators.size == 1) return expr.numerators[0].clone() //don't return a term with just one expression in it.
            val term = Term()
            term.numerators.addAll(expr.numerators)
            return term
        }
        is Sum -> return expr.clone()
    }
    // hopefully doesn't get here
    return Term()
}

/*
Take a list of expressions return a Term that is a product of the expressions.
 */
fun generateTermFromList(exprList: List<Expr>): Expr{
    if (exprList.isEmpty()) return Number(1.0)
    if (exprList.size == 1) return exprList[0].clone()
    val term = Term()
    term.numerators.addAll(exprList)
    return term
}
/*
Return a list of the expressions that make up the product in the denominator of a fraction.
If the expression doesn't have denominator, then return an empty list.
 */
fun getDenominatorList(expr: Expr): List<Expr>{
    val list = ArrayList<Expr>()
    if (expr is Term){
        list.addAll(expr.denominators)
    }
    return list
}
/*
Calculates a common denominator for all the terms in the sum.  Then replaces the sum
by an equivalent term whose denominator is the common denominator. The common
denominator is made by multiplying the terms in the denominators together.  If a term
appears in more than one denominator it only needs to appear once in the common denominator.
But if it appears say twice in a particular denominator then it needs to appear twice in
the common denominator. Example  a/xy + b/xef + c/zeef  common denominator = xyefze x and f
only need to be included once, but e must be included twice.  The new term would be
(aefze + byze + cxy)/xyefze
 */
fun commonDenominator(sum: Sum): Expr {
    val commonDenominator = arrayListOf<Expr>()
    val copyOfCommonDenominator = arrayListOf<Expr>()
    val allTerms = arrayListOf<Expr>()

    allTerms.addAll(sum.plusTerms)
    allTerms.addAll(sum.minusTerms)


    /*
    For each term in the sum add the terms in its denominator to the common denominator.
    To make sure a denominator term is only added once, make a copy of the common denominator each
    time through the loop.  Check to see if the denominator term in is in the copy before adding it
    to the common denominator.  But if it is already in the copy, then remove it from the
    copy.  Then if the denominator term comes up again in the same sum term it will get added.
     */
    for (term in allTerms){

        copyOfCommonDenominator.clear()
        copyOfCommonDenominator.addAll(commonDenominator)

        if ( term is Term){
            for (dTerm in (term).denominators) {
                if (copyOfCommonDenominator.containsExpr(dTerm)) {
                    copyOfCommonDenominator.remove(dTerm)
                } else {
                    commonDenominator.add(dTerm)
                }
            }
        }
    }

    if (commonDenominator.size == 0) {
        return sum
    }

    // Calculate new numerators for each term in the original sum based on the common denominator. Place
    // them all in a new sum.
    var newSum:Expr  = Sum()
    for (term in sum.plusTerms) {
        newSum = newSum.add(convertExpressionNumeratorToCommonDenominator(term, commonDenominator))
    }
    for (term in sum.minusTerms){
        newSum = newSum.subtract(convertExpressionNumeratorToCommonDenominator(term, commonDenominator))
    }

    // Return a term that is the new sum divided by the common denominator.
    val term = Term()
    term.numerators.add(newSum)
    term.denominators.addAll(commonDenominator)

    return term
}

/*
    Remove the token from each term in the sum
    Example: factor a from (am + asm + ax) yields (m + sm + x)
 */
fun factorSum(token: Token, sum: Sum): Expr  {

    val plusTerms = sum.plusTerms
    val minusTerms = sum.minusTerms
    val newPlusTerms = arrayListOf<Expr>()
    val newMinusTerms = arrayListOf<Expr>()

    // remove the token from the terms in the source list, and put the
    // new terms in the dest list.
    fun removeToken(source: ArrayList<Expr>, dest: ArrayList<Expr>) {
        for (term in source) {
            if (term is Token && token.equals(term)) {
                dest.add(Number(1.0))
            } else {
                if (term is Term) {
                    if (term.numerators.contains(token)) {
                        dest.add(term.removeToken(token))

                    } else {
                        throw AlgebraException(
                            "Attempt to factor token out of a term that doesn't contain the token." +
                                    "  token = ${token.toAnnotatedString()}  term = ${sum.toAnnotatedString()}"
                        )
                    }
                } else {
                    throw AlgebraException("Attempt to factor token = ${token.toAnnotatedString()} out of a sum containing the term = ${term.toAnnotatedString()}")
                }
            }
        }
    }

    // remove the token from the plus and minus terms.
    removeToken(plusTerms, newPlusTerms)
    removeToken(minusTerms, newMinusTerms)

    val newSum = Sum()
    newSum.plusTerms.addAll(newPlusTerms)
    newSum.minusTerms.addAll(newMinusTerms)
    return combineTerms(newSum)
}

fun factor  (token: Token, expr: Expr): Expr {

    if (expr is Token)throw AlgebraException ("Error: Attempt to factor a single token = ${token.toAnnotatedString()}")

    if (expr is Term) {
        if (expr.numerators.contains(token)) {
            val e = expr.removeToken(token)
            return e
        } else {
            if (expr.numerators.size == 1 && expr.numerators[0] is Sum){
                // Single sum in numerator
                val fact = factorSum(token, expr.numerators[0] as Sum)
                val term = Term()
                term.numerators.add(fact)
                term.denominators.addAll(expr.denominators)
                return term
            }
        }
    }

    if (expr is Sum) {
        return factorSum(token, expr)
    }
     throw AlgebraException("Error don't know how to factor token = ${token.toAnnotatedString()} out of expression = ${expr.toAnnotatedString()}")

}

/*
    The state token represents the sate variable in the term, a
    source variable or displacement on a capacitor, a momentum on
    and inertia or the derivative of a displacement or momentum.
    Everything else in the term is coefficient of the state
    variable.  So this function looks at term and figures out
    which token is the state variable.
 */
fun getStateToken(expr: Expr): Token {

    if (expr is Token){
        if (expr.energyVar || expr.powerVar) {
            return expr
        }
    }

    if (expr is Term) {
        for (e in expr.numerators) {
            if (e is Token && (e.energyVar || e.powerVar)) {
                return e
            }
        }
    }

    throw AlgebraException("Error: getStateToken called on term with no power or energy variable = ${expr.toAnnotatedString()}")
}

fun replaceToken(token: Token, newToken: Token, expr: Expr): Expr{
    val numerators = arrayListOf<Expr>()
    val denominators = arrayListOf<Expr>()

    if (expr is Token){
        if (token === expr) {
            return newToken
        } else {
            return expr
        }
    }

    if (expr is Sum) {
        return expr
    }

    numerators.addAll((expr as Term).numerators)
    denominators.addAll(expr.denominators)

    if (numerators.contains(token)){
        numerators.remove(token)
        numerators.add(newToken)
    }

    if (denominators.contains(token)){
        denominators.remove(token)
        denominators.add(newToken)
    }

    val term = Term()
    term.numerators.addAll(numerators)
    term.denominators.addAll(denominators)

    return term
}

/*fun replaceTokens(equation: Equation, replacementMap: Map<Token, Token>): Equation{

    var token: Token
    val leftSidePlusTerms = arrayListOf<Expr>()
    val rightSidePlusTerms = arrayListOf<Expr>()
    val leftSideMinusTerms = arrayListOf<Expr>()
    val rightSideMinusTerms = arrayListOf<Expr>()
    val newLeftSidePlusTerms = arrayListOf<Expr>()
    val newRightSidePlusTerms = arrayListOf<Expr>()
    val newLeftSideMinusTerms = arrayListOf<Expr>()
    val newRightSideMinusTerms = arrayListOf<Expr>()

    fun createSum(expr: Expr, plusTerms: ArrayList<Expr>, minusTerms: ArrayList<Expr>, newPlusTerms: ArrayList<Expr>) {

        when (expr) {

            is Token -> {
                if (expr in replacementMap.keys) {
                    newPlusTerms.add(replacementMap[expr]!!)
                } else {
                    newPlusTerms.add(expr)
                }
            }

            is Number -> {
                newPlusTerms.add(expr)
            }

            is Term -> {
                plusTerms.add(expr)
            }

            is Sum -> {
                plusTerms.addAll(expr.plusTerms)
                minusTerms.addAll(expr.minusTerms)
            }
        }
    }

    fun checkTerm(term: Term): Expr{
        val token = getStateToken(term)
        if (token in replacementMap.keys) {
            return replaceToken(token, replacementMap[token]!!, term)
        }

        return term
    }

    createSum(equation.leftSide, leftSidePlusTerms, leftSideMinusTerms, newLeftSidePlusTerms)
    createSum(equation.rightSide, rightSidePlusTerms, rightSideMinusTerms, newRightSidePlusTerms)

    leftSidePlusTerms.forEach { newLeftSidePlusTerms.add(checkTerm(it as Term)) }
    leftSideMinusTerms.forEach { newLeftSideMinusTerms.add(checkTerm(it as Term)) }
    rightSidePlusTerms.forEach { newRightSidePlusTerms.add(checkTerm(it as Term)) }
    rightSideMinusTerms.forEach { newRightSideMinusTerms.add(checkTerm(it as Term)) }

    var leftSide: Expr
    var rightSide: Expr

    if (newLeftSidePlusTerms.size == 1 && newLeftSideMinusTerms.size == 0) {
        leftSide = newLeftSidePlusTerms[0]
    } else {
        val sum = Sum()
        sum.plusTerms.addAll(newLeftSidePlusTerms)
        sum.minusTerms.addAll(newLeftSideMinusTerms)
        leftSide = sum
    }
    if (newRightSidePlusTerms.size == 1 && newRightSideMinusTerms.size == 0) {
        rightSide = newRightSidePlusTerms[0]
    } else {
        val sum = Sum()
        sum.plusTerms.addAll(newRightSidePlusTerms)
        sum.minusTerms.addAll(newRightSideMinusTerms)
        rightSide = sum
    }
     return Equation(leftSide, rightSide)
}*/

fun replaceTokens(equation: Equation, replacementMap: Map<Token, Token>): Equation{

    fun processToken(token: Token): Token {
        val newToken = replacementMap[token]
        if (newToken != null) {
            return newToken
        }
    return token
}

    fun processTerm(term: Term, processList: (sourceList: ArrayList<Expr>, destList: ArrayList<Expr>) -> Unit): Term {

        val newTerm = Term()
        processList(term.numerators, newTerm.numerators)
        processList(term.denominators, newTerm.denominators)
        return newTerm
    }

    fun processSum(sum: Sum, processList: (sourceList: ArrayList<Expr>, destList: ArrayList<Expr>) -> Unit): Sum {
        val newSum = Sum()
        processList(sum.plusTerms, newSum.plusTerms)
        processList(sum.minusTerms, newSum.minusTerms)
        return newSum
    }

    fun processList(sourceList: ArrayList<Expr>, destList: ArrayList<Expr>) {

        sourceList.forEach {
            when (it) {

                is Token -> destList.add(processToken(it))
                is Number -> destList.add(it)
                is Term -> destList.add(processTerm(it, ::processList))
                is Sum -> destList.add(processSum(it, ::processList))
            }
        }
    }

    fun processExpr(expr: Expr): Expr {
        return when(expr) {
            is Token -> processToken(expr)
            is Number -> expr
            is Term -> processTerm(expr, ::processList)
            is Sum -> processSum(expr,::processList)
            else -> throw IllegalArgumentException ("Unknown Expr, expr = ${expr.toAnnotatedString()} : ${expr::class.simpleName}")
        }
    }

    return Equation(processExpr(equation.leftSide), processExpr(equation.rightSide))
}
/*
    Each term in the sum is basically a coefficient times a state variable.  There may be
    several terms for any given state variable.  This function creates a new sum where there
    is just one term for each state variable.  This involves finding all the terms for a
    particular state variable, calculating a common denominator for the terms and then
    creating new numerators based on the common denominator.
    Example  p1R1/R2 + q7R3/(R1 + R2) + p1(R5)/R3 - q7R4)/R6 becomes
             (P1R1R3 + P1R5R2)/R2R3 + (q7R3R6 -q7R4(R1 = R2))/R6(R1 + R2)
    This function does not factor out the state variable.  This is done in a separate step.
 */

/*fun gatherLikeTerms(expr: Expr): Expr {
    val termsMap = mutableMapOf<Token,Expr>()

    fun groupTerms(source: ArrayList<Expr>, isPlusTerm: Boolean) {
        //println("group terms terms= "); source.forEach { println(it.toAnnotatedString()) }
        for (expr in source){
            val token = getStateToken(expr)
            var newExpr: Expr
            if (expr is Token){
                newExpr = Number(1.0)
            } else {

                newExpr = expr.clone()
                (newExpr as Term).numerators.remove(token)
            }
            if (termsMap.containsKey(token)) {
                //key already exist so add/subtract this expr from the sum
                var mapExpr = termsMap[token]
                if (mapExpr != null) {
                    mapExpr = if (isPlusTerm) mapExpr.add(newExpr) else mapExpr.subtract(newExpr)
                    termsMap[token] = mapExpr
                }
            } else {
                // new key so create new entry.
                termsMap[token] = if (isPlusTerm) newExpr else Sum().subtract(newExpr)
            }
        }
    }

    when (expr) {

        is Number -> throw IllegalArgumentException("groupLikeTerm called with a Expr that is just a single Number, expr = ${expr.toAnnotatedString()}")

        is Token -> {
            if (expr.energyVar == false && expr.powerVar == false){
                throw IllegalArgumentException("groupLikeTerm called with a Expr that is just a single Token that is not a power or energy variable, expr = ${expr.toAnnotatedString()}")

            }
        }
    }
}*/
fun gatherLikeTerms_old(sum: Sum):Expr {
   val termsMap = mutableMapOf<Token,Expr>()

    // Add/subtract each term in the array list to the appropriate sum in
    // in the termsMap. If the isPlusTerm flag is true then add the
    // term to the sum otherwise subtract it.  See the comment below.
    fun groupTerms(source: ArrayList<Expr>, isPlusTerm: Boolean) {
        //println("group terms terms= "); source.forEach { println(it.toAnnotatedString()) }
        for (term in source){
            val token = getStateToken(term)
            if (termsMap.containsKey(token)) {
                //key already exist so add/subtract this term from the sum
                var expr = termsMap[token]
                if (expr != null) {
                    expr = if (isPlusTerm) expr.add(term) else expr.subtract(term)
                    termsMap[token] = expr
                }
            } else {
                // new key so create new entry.
                termsMap[token] = if (isPlusTerm) term else Sum().subtract(term)
            }
        }
    }
    // First create a map where the keys are the state tokens, and the
    // values are a sum of all the terms that that state variable occurs
    // in. Example key p  value (pR3/R2 + pR5/R3 )
    groupTerms(sum.plusTerms, true)
    groupTerms(sum.minusTerms, false)

    // Create the new sum.
    var localSum: Expr =  Sum()
    termsMap.values.forEach {
        if (it is Term) {
            // just one term for this state variable so just add it to the sum.
            localSum = localSum.add(it)
        } else {
            if (it is Sum && it.plusTerms.size + it.minusTerms.size > 1) {
                // sum with several terms.  Must create a new term with a common denominator
                localSum = localSum.add(commonDenominator(it))
            } else {
                // This is a sum with a single negative term.
                localSum = localSum.add(it)
            }
        }
    }
    return localSum
}

/*
    This function can solve a specific form of an algebraic equation for the variable represented by token.
    The form is as follows:
    1. The left side is a single term containing the token as one of the factors, usually just the token.
    2. The right side has one or more terms containing the token and at least one other term or constant.
    3. The token can not appear in the denominator of any term.
    4. The token cannot be a parameter of a function, such as log, sin or exp.
    5. The token can't appear in the product of two sums i.e. (x + a)(x + b) which is of course the same
       as appearing in an exp function since there would ba an x squared.
    This function follows the brute force method of solving such an equation.
    1. Add/subtract terms containing the token so that they are eliminated on the right side of the equation
       and appear on the left side.
    2. Calculate a common denominator for the terms on the left side.
    3. Calculate a sum based on the numerators of the left side and the common denominator creating a new fraction
       with this sum divided by the common denominator.  See comments in commonDenominator function.
    4. Factor the token out of the sum in the numerator of this fraction.
    5. Divide both sides of the equation by this fraction, leaving just the token on the left.
 */
/*
fun solve (token: Token, equation: Equation): Equation {

    var leftSide = equation.leftSide.clone()
    var rightSide = equation.rightSide.clone()

    if (equation.rightSide is Term){
        if ((equation.rightSide as Term).numerators.contains(token) || (equation.rightSide as Term).denominators.contains(token)) {
            throw(AlgebraException("Error: Can't solve equation for ${token.toAnnotatedString()} because there is just one "
                    + "term on each side of the equation and ${token.toAnnotatedString()} appears in both of them."
                    +"\nEquation is ${equation.toAnnotatedString()}"))
        }
        return equation
    }

    if (isTokenInDenominator(token, leftSide) || isTokenInDenominator(token, rightSide)) throw AlgebraException("Error: The token we are solving for occurs in the denominator of one of the terms.  " +
            "These algebra routines can't solve this")

    if (rightSide is Sum) {
        val plusTerms = rightSide.plusTerms
        val minusTerms = rightSide.minusTerms
        val matchingPlusTerms = plusTerms.filter { contains(token, it) }
        val matchingMinusTerms = minusTerms.filter{ contains(token, it) }

        if (matchingPlusTerms.size + matchingMinusTerms.size == 0){
            return equation
        }

        // Subtract plus terms from both sides of equation.
        matchingPlusTerms.forEach {
            leftSide = leftSide.subtract(it)
            plusTerms.remove(it)
        }

        // Add minus terms to both sides of equation
        matchingMinusTerms.forEach {
            leftSide = leftSide.add(it)
            minusTerms.remove(it)
        }

        //println("solve leftside = ${leftSide.toAnnotatedString()}: ${leftSide::class.simpleName}")

        if (leftSide is Term) {
            val term = Term()
            (leftSide as Term).numerators.forEach { if (it != token) term.numerators.add(it)}
            term.denominators.addAll((leftSide as Term).denominators)
            val expr = rationalizeTerm(term)
            rightSide = rightSide.divide(expr)
            return Equation(token, rightSide)
        }

        var commonFraction: Expr

        if (leftSide is Sum) {

            // Calculate common denominator for left side, and create
            // single term on left side.  See comments in commonDenominator function.
            commonFraction = commonDenominator(leftSide as Sum)

            // Factor token out of the numerator on the fraction
            val factored = factor(token, commonFraction)

            // Divide the right side by the fraction.  We don't bother dividing the left side
            // since we know only the token will be left.
            rightSide = rightSide.divide(factored)

            // Return new equation with token on the left side and the new right side.
            return Equation(token, rightSide)

        }


    }


    throw AlgebraException("Unknown error solving equation ${equation.toAnnotatedString()} for ${token.toAnnotatedString()}")
}
*/

/*
This functions takes a list of one or more equations and solves them (simultaneously if necessary).  The left side
of each equation is a token representing the variable to be solved for.  The right side is a sum of terms.  Some
of these term may contain the token to be solved for, and the others can be considered constants.  In the case
of multiple equations, some of the terms may contain other tokens to be solved for.  Variables to be solved for
must be in the numerators of the terms and not buried in any sums. A term may contain only one token to be solved for.
Single equation examples, solve for x
x = cx + ab
x = cx + ab - (n/m)x
x = c/x + ab -> illegal because x is in denominator of a term
x = (c+x)/d + ab -> illegal because x is hidden in a sum.  This function can't handle this. Could be re-written as c/d + x/d + ab
x = xcx + ab  -> illegal because a term contains multiple tokens to be solved for, essentially an x squared.

Multiple equations examples, solve for x and y

x = ax + cy + ab
y = (n/m)x + cd + ax + (r/s)y

x = ax + cy + ab + mn               -> illegal 4th term of second equation contains two tokens to be solved for
y = (n/m)x + cd + ax + (r/s)xy + qr

Equations of this form are typical of what is generated by a bondgraph when we need to generated expressions for efforts
or flows on resistors with arbitrarily assigned causality, or momentums and displacements for inertias and capacitors in
differential causality.

However, we will be using Cramer's method to solve equations.  Cramer's method deals with equations in the following form:
ax + by = d
cx + dy = f

The coefficients of the variables to be solved for are arranged in a matrix
|a b|
|c d|
The variables are arranged in a matrix
|x|
|y|
The constants are arranged in a matrix
|d|
|f|
Our equations typically ar fairly complex. The first example of two equations from above would need to be re-written as
(1 - a)x - cy = ab
 -(n + am)/m)x + ((ys - r)/s)y = cd + qr

This function will build the required coefficient matrix directly by subtracting any terms on the right side of the
equation from the term on the left side of the equation.  So given
x = ax + cy + ab
y = (n/m)x + cd - ax + (r/s)y
 the 0,0 element would be x - ax = x(1 - a).  0,1 would -c  1,0 would be ax - (n/m)x = x(am - n)m and so on
 |    1 - a       -c     |
 | (am - n)/m  (s - r)/s |

 */

fun solve (equations: ArrayList<Equation>): ArrayList<Equation>{
    val tokenToIndex = mutableStateMapOf<Token, Int>()
    val constantsList = arrayListOf<Expr>()
    val variables = arrayListOf<Token>()
    val order = equations.size
    val coeffMatrix = Matrix.zeroMatrix(order)
    var constSum:Expr = Number(0.0)  // Sum if the constant expressions

    /* The following internal function takes an expression, and row and adds/subtracts the
       expression from the appropriate spot in either the coefficient or constant matrix based on
       the value of the boolean variable plusTerm.
    */
    fun placeExpr(expr: Expr, row: Int, plusTerm: Boolean) {
        if (isStateVariableExpr(expr) && tokenToIndex.contains(getTokenFromStateExpression(expr))) {
            val col = tokenToIndex[getTokenFromStateExpression(expr)]
            if (col == null) {
                constSum = if (plusTerm) constSum.add(expr) else constSum.subtract(expr)
            } else {
                if (plusTerm) {
                    coeffMatrix.decrementElementByExpr(getTermFromStateExpression(expr), row, col)
                } else {
                    coeffMatrix.incrementElementByExpr(getTermFromStateExpression(expr), row, col)
                }
            }
        } else {
            constSum = if (plusTerm) constSum.add(expr) else constSum.subtract(expr)
        }
    }

    println("solve() order = $order")
    /*
    We are given a list of equations.  The left side of each of these equations is a token representing one
    of the variables to be solved for. The following loop builds or starts to build several data structures
    based on these tokens.
    1. a map mapping the token to the equation number. If token X came from the second equation it is
       mapped to 1 (zero relative).  Later given a token, we can index into the coefficient matrix.
    2. an array list of the tokens in the same order as the list of equations.  This list is passed directly
       to the Matrix.solveCramer() function.
    3. we start building the coefficient matrix starting with a zero matrix. We add 1 to the corresponding
       element for the equation (because the coefficient of a single token is 1).  The corresponding element
       is on the diangle of the matrix, i.e the element of the second equation would be row 1, col 1
       (zero relative). Later, terms from the right side of the equations will be subtracted from
       the coefficien matrix.

    We also check the equations for several possible error.
*/
    for (cnt in 0 until order){
        val token = equations[cnt].leftSide
        val rightSide = equations[cnt].rightSide

        if (isStateVariableExpr(rightSide) && getTokenFromStateExpression(rightSide).equals(token)) {
            throw AlgebraException("Solve(equations) given an equation of the form x = ax which doesn't make sense. equation = ${equations[cnt].toAnnotatedString() }}")
        }

        if (rightSide is Sum && rightSide.plusTerms.isEmpty() && rightSide.minusTerms.size == 1 &&  isStateVariableExpr(rightSide.minusTerms[0]) &&
            getTokenFromStateExpression(rightSide.minusTerms[0]).equals(token)) {
            throw AlgebraException("Solve(equations) given an equation of the form x = ax which doesn't make sense. equation = ${equations[cnt].toAnnotatedString() }}")

        }

        if (token !is Token)  {
            throw AlgebraException ("solve(equations) called with equation that doesn't have a single token for the left side.  equation = ${equations[cnt].toAnnotatedString()}")
        }

        if (tokenToIndex.contains(token)) {
            val i = tokenToIndex[token]
            throw AlgebraException("solve(equations) called with two equations for the same token.  equation1 = ${equations[i!!].toAnnotatedString()}, equation2 = ${equations[cnt].toAnnotatedString()}")
        }

        tokenToIndex[token] = cnt
        variables.add(token)
        coeffMatrix.incrementElementByExpr(Number(1.0), cnt, cnt)
    }

    /*
            Loop through the equations adding/subtracting terms from the right sides to the appropriate
            elements in the coefficient and constant matrices.
     */
    for (row in 0 until order) {  // one row for each equation

        constSum = Number(0.0)  // Sum if the constant expressions

        if (equations[row].rightSide is Sum) { // check all plus and minus terms

            (equations[row].rightSide as Sum).plusTerms.forEach { expr ->
                placeExpr(expr, row, true)
            }

            (equations[row].rightSide as Sum).minusTerms.forEach { expr ->
                placeExpr(expr, row, false)
            }

        } else { // right side is single term so check it
            placeExpr(equations[row].rightSide, row, true)
        }

        constantsList.add(constSum)

    }

    coeffMatrix.printOut()
    println("#################  Variables  ########################")
    variables.forEach { println("${it.toAnnotatedString()}") }
    println("@@@@@@@@@@@@@@  Constants  @@@@@@@@@@@@@@@@@@@@@@@@@@")
    constantsList.forEach { println("${it.toAnnotatedString()}") }
    val solvedEquations = Matrix.solveCramer(coeffMatrix, variables, constantsList)
    return solvedEquations
}

/*
fun solveSimultaneousEquations(equations: Map<Element, Equation>): LinkedHashMap<Element, Equation> {

    val singleSolvedEquations = linkedMapOf<Element, Equation>()
    val simutanelouselySolvedEquations = linkedMapOf<Element, Equation>()
    val tokensToElementsMap = hashMapOf<Token, Element>()

    equations.forEach {key, value ->
        if (value.leftSide is Token) {
            tokensToElementsMap[value.leftSide as Token] = key
        } else {
            throw IllegalArgumentException("Left side of equation must be a token. Equation = ${value.toAnnotatedString()}")
        }
    }

    equations.forEach{key, value ->
        val token = equations[key]?.rightSide as Token
        if (token != null) {
            singleSolvedEquations[key] = equations[key]?.let { solve(token, it) }!!
        }
    }

}*/
