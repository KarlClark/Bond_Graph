package algebra

import androidx.compose.ui.text.AnnotatedString
import bondgraph.AlgebraException
import kotlin.math.absoluteValue
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

    val term1 = tX.divide(tY).divide(tZ).multiply(tP)
    val term2 = Number(0.0).subtract(tD.multiply(tC).multiply(tB).divide(tA))
    val sum3 = sum1.add(term2)
    //val term1 = divide(tX,tZ)
    val term3 = Term()
    val term4 = Term()
    val term5 = Term()
    val term6 = Term()
    expr1 = subtractStateExpressionFromSum(term1, sum3 as Sum)
    println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    println("term1 = ${term1.toAnnotatedString()}, sum3 = ${sum3.toAnnotatedString()}, expr1 = ${expr1.toAnnotatedString()}")
    printExpr(expr1)
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

    if (expr is Token) return Number(1.0)  // a token T is the same as (1)T
    val term = expr as Term
    if (term.numerators.size == 1)return Number(1.0)
    return term.numerators[0]
}
/*
Return true if any terms in the sum are state variable expressions.
 */
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
/*
return true is the expression is a term and contains denominators, or the expression is
a sum and any of the terms in the sum denominators.
 */
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
/*
A sum is negative if it doesn't have any plusTerms.  A term is negative if it contains an odd
number of negative sums.  i.e.
(-a)/(x-y) is negative same as a/(y-x)
(-a)/(-x-y) is positive same as (-a)/(-(x+y)) same as a/(x+y)
 */
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

/*
Convert a negative expression into a positive one. i.e. 0 + (original expression) would be the same as 0 - (new expression)
 */
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
            // Negate all the negative sums in the term. Because of the error check above, we
            // know we will be negating an odd number of sums, thus reversing the sign of the term.
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
            //return negated sum
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
/*
Basically create a negative expression by putting the expression in the minusTerms of a sum, accounting
for the possibility that the expression is a state variable expression.
 */
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
/*
return true if the list contains an expression = to expr.
 */
fun List<Expr>.containsExpr(expr: Expr): Boolean {
    return this.any{ expr == it }
}
/*
This function replaces the energy token in an expression, with its matching dot token,
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

/*
Returns true if each expression is a state variable expression and they both have the same state token.
 */
fun matchingStateExpressions(expr1: Expr, expr2: Expr): Boolean {

    if ( ! (isStateVariableExpr(expr1) && isStateVariableExpr(expr2))){
        return false
    }

    val expr1 = getTokenFromStateExpression(expr1)
    val expr2 = getTokenFromStateExpression(expr2)
    return expr1.equals(expr2)
}

/*
print out an expression in its individual parts so you can see how it is made up.
 */
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

/*
A class that hold a numerical coefficient and an expression,
so something like 2xy/z split up as 2 and xy/z
 */
class CoefficientAndExpr(val coefficient: Double, val expr: Expr)

/*
The following functions were written early on in the project and are complicated because they are designed
to deal with terms made up of other terms like (ab)/(cd/xy).  Some are designed to be called recursively
to unpack complicated terms. Since then the math routines have been re-written to largely avoid creating
terms like this in the first place.  But I haven't gone through and re-written these functions because
it would be a lot of work, they work fine on simple terms, and a complicated term might still crop up.
 */

/*
This function serves two purposes.  It searches the source list (which will be the numerators or denominators of some term)
and finds all the numbers.  These numbers are multiplied or divided together to produce one number. The other tokens are
add to the dest1 list.  If another term is found then this function is called recursively on the new term and the dest2
list might be needed to hold tokens from the denominator of the sub-term.  i.e. a(b/c)/xy could be formed as
Token(a) X Term(b/c) / Term(xy) . This function would be called recursively to handle the Term(a/b) found in the
numerators of the original term.  The end result is a numerical coefficient and two lists that could be used to
form one term with no sub-terms.  It takes two calls, one for the numerators and one for the denominators to build up
two lists that could be used to form the numerator and denominator of a new term with no sub-terms. In the above example
you would eventually get back list(a, b) and list(c, x, y) and you could create ab/cxy
 */
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
    eliminateCommonTerms(numerators, denominators, num)
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

/*
put together a coefficient and an expression to form a new combined expression. Check for
things like the expression being equal to zero or one.  Return a negative expression if the
coefficient is less than zero.
 */
fun createNewExprFromCoefficientAndExpr(coefficientAndExpr: CoefficientAndExpr): Expr {
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
                return createNegativeExpression(term)
            }
            return term
        }

        is Term -> {

            if (expr.numerators.size + expr.denominators.size == 0){
                if (num > 0.0) {
                    return Number(num)
                } else {
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
                return createNegativeExpression(expr)
            }
            return expr
        }

        is Sum -> {
            if (expr.plusTerms.size + expr.minusTerms.size == 0){
                return Number(0.0)
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

            if (sum.plusTerms.isEmpty()){
                term.numerators.add(negate(sum))
               return createNegativeExpression(term)
            }

            term.numerators.add(sum)
            return term
        }
    }
    return Term() // shouldn't get here
}

/*
basically swap the plusTerms with the minusTerms
 */
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

/*fun reciprocal (expr: Expr): Expr {
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
}*/

fun checkForNegativeTerm(term: Term): Expr {

    val newTerm = Term()
    var isNegative = false

    term.numerators.forEach { expr ->
        if (expr is Sum && expr.plusTerms.isEmpty()){
            newTerm.numerators.add(negate(expr))
            isNegative = ! isNegative
        } else {
            newTerm.numerators.add(expr)
        }
    }

    term.denominators.forEach { expr ->
        if (expr is Sum && expr.plusTerms.isEmpty()){
            newTerm.denominators.add(negate(expr))
            isNegative = ! isNegative
        } else {
            newTerm.denominators.add(expr)
        }
    }

    if (isNegative){
        return createNegativeExpression(newTerm)
    }
     return newTerm
}
/*fun checkForNegativeTerm(term: Term): Expr {

    *//*
    If this term is a negative sum divided by a negative sum then return a positive term.
    If the term has a negative sum in either the numerator or denominator, then return a negative sum of the term.
    I.e. -a/-b becomes a/b  and a/-b would become -(a/b)
     *//*

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
}*/

/*
Does a lot.  Recursively breaks down term into two lists, one for numerators and one for denominators,
basically flattening the term if it is made up of multiple terms, and sums.
In the process it multiplies/divides any numbers to produce a single numeric coefficient. Sign of
expression is adjusted depending on negative numbers and sums.  A cancel operation is performed
between the two list.  Then the lists are put back together to form a new expression.
 */
fun rationalizeTerm (term: Expr): Expr {
    if (isStateVariableExpr(term)){
        throw AlgebraException("rationalizeTerm(term) called with state variable expression, which it can't handle. term = ${term.toAnnotatedString()}")
    }
    if (term !is Term) {
        return term
    }
    val coefficientAndExpr = getCoefficientAndExpr(term)
    val expr = createNewExprFromCoefficientAndExpr(coefficientAndExpr)
    println("rationalizeTerm(term) term = ${term.toAnnotatedString()}, expr = ${coefficientAndExpr.expr.toAnnotatedString()}, coeff = ${coefficientAndExpr.coefficient}, new expression = ${expr.toAnnotatedString()}")
    if (expr is Term) {
        return checkForNegativeTerm(expr)  // this step is probably unnecessary
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

/*
Eliminate expressions that are common to both lists. If it finds expressions that are negatives of
each other, eliminate them too, and toggle num between 1 and -1 starting with startNum to keep
tract of sign.
 */
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
/*fun cancel(term: Expr): Expr{
    val numerators = arrayListOf<Expr>()
    val denominators = arrayListOf<Expr>()

    //println("cancel on ${term.toAnnotatedString()}")
    if (term !is Term) {
        return term
    }
*//*
 Go through items in the numerator and denominators of the Term. Store all the Tokens and Sums
 in separate lists.  Expand any Terms that are found and add their Tokens and Sums to the lists.
 *//*
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
}*/





/*
    The following function simplifies a Sum.  To simplify a sum means to take something
    like (a + b + c -a) and change it to (b + c).  If the given expression is fraction
    with a single Sum in the numerator then return the fraction with the numerator simplified.
 */

/*
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

    */
/*
    For each term in the plus terms list see if there is a matching term
    in the minus term list.  If there is, remove the term from both lists.
    Make a new copy of the minus terms list on each iteration because a
    term might occur twice in the plus terms but only once in the minus
    terms. Use our Expr.equals() function for comparisons.  Be sure to
    remove the correct objects from the correct list since they may not
    be equal from an object point of view.
    *//*

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
*/



/*
    Check t see if the token occurs in the numerator of the expression.
 */
fun contains(token: Token, expr: Expr): Boolean {
    return if (expr is Term) expr.getNumeratorTokens().contains(token) else false
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
This functions takes a list of one or more equations and solves them (simultaneously if necessary).  The left side
of each equation is a token representing the variable to be solved for.  The right side is a sum of terms.  Some
of these terms may contain the token to be solved for, and the others can be considered constants.  In the case
of multiple equations, some of the terms may contain other tokens to be solved for.  Variables to be solved for
must be in the numerators of the terms and not buried in any sums. A term may contain only one token to be solved for.
Single equation examples, solve for x
x = cx + ab
x = cx + ab - (n/m)x
x = c/x + ab -> illegal because x is in denominator of a term
x = (c+x)/d + ab -> illegal because x is hidden in a sum.  This function can't handle this. Could be re-written as c/d + x/d + ab
x = xcx + ab  -> illegal because a term contains a multiple of tokens to be solved for, essentially an x squared.

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
 the (0,0) element would be x - ax = x(1 - a).  (0,1) would -c  (1,0) would be ax - (n/m)x = x(am - n)m and so on
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
       the appropriate elements of the coefficient matrix.

    We also check the equations for several possible errors.
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

