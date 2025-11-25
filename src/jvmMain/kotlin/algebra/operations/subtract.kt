package algebra.operations


import algebra.*
import algebra.Number
import bondgraph.Operation.*
import kotlin.math.absoluteValue

fun subtract(expr1: Expr, expr2: Expr): Expr {
    //println("subtract expr1 = ${expr1.toAnnotatedString()}: ${expr1::class.simpleName}  expr2 = ${expr2.toAnnotatedString()}: ${expr2::class.simpleName}")
    when (expr1) {

        is Token -> when (expr2) {
            is Token -> return subtract (expr1, expr2)
            is Number -> return subtract (expr1, expr2)
            is Term -> return subtract (expr1, expr2)
            is Sum -> return subtract (expr1, expr2)
        }

        is Number -> when (expr2) {
            is Token -> return subtract (expr1, expr2)
            is Number -> return subtract (expr1, expr2)
            is Term -> return subtract (expr1, expr2)
            is Sum -> return subtract (expr1, expr2)
        }

        is Term -> when (expr2) {
            is Token -> return subtract (expr1, expr2)
            is Number -> return subtract (expr1, expr2)
            is Term -> return subtract (expr1, expr2)
            is Sum -> return subtract (expr1, expr2)
        }

        is Sum -> when (expr2) {
            is Token -> return subtract (expr1, expr2)
            is Number -> return subtract (expr1, expr2)
            is Term -> return subtract (expr1, expr2)
            is Sum -> return subtract (expr1, expr2)
        }
    }

    return Token("ERROR Subtract")
}

// ***********************  Token   ****************************************************

/*fun subtract(token: Token, expr: Expr): Expr {
    when (expr) {
        is Token -> return subtract(token, expr)
        is Number -> return subtract(token, expr)
        is Term -> return subtract(token, expr)
        is Sum -> return subtract(token, expr)
    }
    return Token("ERROR")
}*/

fun subtract (token1: Token, token2: Token): Expr {

    if (token1.equals(token2)){
        return Number(0.0)
    }

    val sum = Sum()
    sum.plusTerms.add(token1)
    sum.minusTerms.add(token2)
    return sum
}

fun subtract (token: Token, number: Number): Expr {

    if (number.value == 0.0) {
        return token
    }

    val sum = Sum()
    sum.plusTerms.add(token)
    sum.minusTerms.add(number)
    return sum
}

fun subtract (token: Token, term: Term): Expr{


    val expr = reduce(term)

    if (expr !is Term) {
        return subtract(token, expr)
    }

    if (isStateVariableExpr(expr) && token.equals(getTokenFromStateExpression(expr))) {
        println("subtract(token, term) calling addSubtractStateExpression token = ${token.toAnnotatedString()}, term = ${term.toAnnotatedString()}, expr = ${expr.toAnnotatedString()} " )
        return addSubtractStateExpressions(token, expr, SUBTRACT)
    }

    val sum = Sum()
    sum.plusTerms.add(token)
    sum.minusTerms.add(expr)
    return sum
}

fun subtract (token: Token, sum: Sum): Expr {

    //println ("subtract(Token, Sum token = ${token.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")
    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return token
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return subtract(token, sum.plusTerms[0])
    }

    return add(token, negate(sum))

    /*if (isStateVariableExpr(token)){
        return subtractStateExpressionFromSum(token, sum)
    }

    val newSum = Sum()
    newSum.minusTerms.addAll(sum.plusTerms)
    newSum.plusTerms.addAll(sum.minusTerms)
    newSum.plusTerms.add(token)
    return combineTerms(newSum)*/
}

// ***********************  Number   ****************************************************
/*fun subtract (number: Number, expr: Expr): Expr {
    //println("add number, expr expr = ${expr.toAnnotatedString()}: ${expr::class.simpleName}")
    when (expr){
        is Token -> return subtract (number, expr)
        is Number -> return subtract (number, expr)
        is Term -> return subtract (number, expr)
        is Sum -> return subtract (number, expr)
    }
    return Token("ERROR")
}*/

fun subtract (number: Number, token: Token):Expr {
    val sum = Sum()

    if (number.value != 0.0) {
        sum.plusTerms.add(number)
    }
    sum.minusTerms.add(token)
    return sum
}
fun subtract (number1: Number, number2: Number): Expr {
    val num = number1.value - number2.value
    if (num >= 0) {
        return Number(num)
    } else {
        val sum = Sum()
        sum.minusTerms.add(Number(num.absoluteValue))
        return sum
    }
}

fun subtract (number: Number, term: Term): Expr {
    val sum = Sum()

    val expr = reduce(term)

    if (expr !is Term) {
        return subtract(number, expr)
    }

    if (number.value != 0.0) {
        sum.plusTerms.add(number)
    }
    sum.minusTerms.add(expr)
    return sum
}

fun subtract(number: Number, sum: Sum): Expr {

    if (sum.plusTerms.size + sum.minusTerms.size == 0) {
        return number
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()) {
        return subtract(number, sum.plusTerms[0])
    }

    val newSum = Sum()
    newSum.minusTerms.addAll(sum.plusTerms)
    newSum.plusTerms.addAll(sum.minusTerms)
    if (number.value != 0.0){
        newSum.plusTerms.add(number)
    }
    return combineTerms(newSum)
}

// ***********************  Term   ****************************************************

fun subtract(term: Term, token: Token): Expr {
    val expr = reduce(term)

    if (expr !is Term) {
        return subtract(expr, token)
    }

    if (isStateVariableExpr(expr) && token.equals(getTokenFromStateExpression(expr))) {
        println ("subtract(term, token) calling addSubtractStateExpression term = ${term.toAnnotatedString()}, expr = ${expr.toAnnotatedString()}, token = ${token.toAnnotatedString()}")
        return addSubtractStateExpressions(expr, token, SUBTRACT)
    }

    val sum = Sum()
    sum.plusTerms.add(expr)
    sum.minusTerms.add(token)
    return sum
}

fun subtract(term: Term, number: Number): Expr {

    val expr = reduce(term)

    if (expr !is Term) {
        return subtract(expr, number)
    }

    if (number.value == 0.0){
        return term
    }

    val sum = Sum()
    sum.plusTerms.add(expr)
    sum.minusTerms.add(number)
    return sum
}

fun subtract(term1: Term, term2: Term): Expr {

    val expr1 = reduce(term1)
    val expr2 = reduce(term2)

    if (expr1 !is Term || expr2 !is Term) {
        return subtract(expr1, expr2)
    }

    if (isStateVariableExpr(expr1) && isStateVariableExpr(expr2) &&
        getTokenFromStateExpression(expr1).equals(getTokenFromStateExpression(expr2))) {
        println("subtract(term,term) calling addSubtractStateExpression term1 = ${term1.toAnnotatedString()}, expr1 = ${expr1.toAnnotatedString()}, term2 = ${term2.toAnnotatedString()}, expr2 = ${expr2.toAnnotatedString()}")
        return addSubtractStateExpressions(term1,term2, SUBTRACT)
    }

    val sum = Sum()
    sum.plusTerms.add(expr1)
    sum.minusTerms.add(expr2)
    return combineTerms(sum)
}

fun subtract(term: Term, sum: Sum): Expr {

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return term
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return subtract(term, sum.plusTerms[0])
    }

    val expr = reduce(term)
    if (expr !is Term) {
        return subtract(expr,sum)
    }

    return add(term, negate(sum))

    /*if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return term
    }

    val expr = reduce(term)
    if (term !is Term){
        return subtract(expr, sum)
    }

    if (isStateVariableExpr(term)){
        val expr = addStateExpressionToSum(term, negate(sum))
        return expr
    }

    val newSum = Sum()
    newSum.minusTerms.addAll(sum.plusTerms)
    newSum.plusTerms.addAll(sum.minusTerms)
    newSum.plusTerms.add(expr)
    return combineTerms(newSum)*/
}
// ***********************  Sum   ****************************************************

fun subtract(sum: Sum, token: Token): Expr {

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        val newSum = Sum()
        newSum.minusTerms.add(token)
        return combineTerms(newSum)
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return subtract(sum.plusTerms[0], token)
    }

    if (isStateVariableExpr(token)) {
        return subtractStateExpressionFromSum(token, sum)
    }

    val newSum = Sum()
    newSum.plusTerms.addAll(sum.plusTerms)
    newSum.minusTerms.addAll(sum.minusTerms)
    newSum.minusTerms.add(token)
    return combineTerms(newSum)
}

fun subtract(sum: Sum, number: Number): Expr {

    println("subtract(sum, number) sum = ${sum.toAnnotatedString()}, number = ${number.toAnnotatedString()}")

    if (number.value == 0.0){
        return sum
    }

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        val newSum = Sum()
        newSum.minusTerms.add(number)
        return combineTerms(newSum)
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return subtract(sum.plusTerms[0], number)
    }

    val newSum = Sum()
    newSum.plusTerms.addAll(sum.plusTerms)
    newSum.minusTerms.addAll(sum.minusTerms)
    newSum.minusTerms.add(number)
    val combined = combineTerms(newSum)
    println("subtract(sum, number returning combined = ${combined.toAnnotatedString()}")
    return combined
}

fun subtract(sum: Sum, term: Term): Expr {
    val expr = reduce(term)

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        val newSum = Sum()
        newSum.minusTerms.add(term)
        return combineTerms(newSum)
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return subtract(sum.plusTerms[0], term)
    }

    if (expr !is Term){
        return subtract(sum, expr)
    }

    if (isStateVariableExpr(expr)) {
        return subtractStateExpressionFromSum(expr, sum)
    }

    val newSum = Sum()
    newSum.plusTerms.addAll(sum.plusTerms)
    newSum.minusTerms.addAll(sum.minusTerms)
    newSum.minusTerms.add(expr)
    return combineTerms(newSum)
}

fun subtract (sum1: Sum, sum2: Sum): Expr {
    val expr = negate(sum2)
    println("subtract (sum,sum) sum1 = ${sum1.toAnnotatedString()}, sum2 = ${sum2.toAnnotatedString()}  negated = ${expr.toAnnotatedString()}: ${expr::class.simpleName}")
   return add(sum1, negate(sum2))
}
