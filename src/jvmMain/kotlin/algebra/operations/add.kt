package algebra.operations

import algebra.*
import algebra.Number
import bondgraph.Operation.*

fun add(expr1: Expr, expr2: Expr): Expr {
    //println("add expr1 = ${expr1.toAnnotatedString()}: ${expr1::class.simpleName}  expr2 = ${expr2.toAnnotatedString()}: ${expr2::class.simpleName}")
    when (expr1) {

        is Token -> when (expr2) {
            is Token -> return add(expr1, expr2)
            is Number -> return add(expr1, expr2)
            is Term -> return add(expr1, expr2)
            is Sum -> return add(expr1, expr2)
        }

        is Number -> when (expr2) {
            is Token -> return add(expr1, expr2)
            is Number -> return add(expr1, expr2)
            is Term -> return add(expr1, expr2)
            is Sum -> return add(expr1, expr2)
        }

        is Term -> when (expr2) {
            is Token -> return add(expr1, expr2)
            is Number -> return add(expr1, expr2)
            is Term -> return add(expr1, expr2)
            is Sum -> return add(expr1, expr2)
        }

        is Sum -> when (expr2) {
            is Token -> return add(expr1, expr2)
            is Number -> return add(expr1, expr2)
            is Term -> return add(expr1, expr2)
            is Sum -> return add(expr1, expr2)
        }
    }

    return Token("ERROR Add")
}


// ***********************  Token   ****************************************************

fun add (token1: Token, token2: Token): Expr {

    if (token1.equals(token2)) {
        val term =Term()
        term.numerators.add(Number(2.0))
        term.numerators.add(token1)
        return term
    }

    val sum = Sum()
    sum.plusTerms.add(token1)
    sum.plusTerms.add(token2)
    return sum
}

fun add(token: Token, number: Number): Expr {

    if (number.value == 0.0) {
        return token
    }

    val sum = Sum()
    sum.plusTerms.add(number)
    sum.plusTerms.add(token)
    return sum
    }

fun add(token: Token, term: Term): Expr{

    val expr = reduce(term)

    if (expr !is Term){
        return add(token, expr)
    }

    if (isStateVariableExpr(expr) && token.equals(getTokenFromStateExpression(expr))) {
       return addSubtractStateExpressions(token, expr, ADD)
    }

    val sum = Sum()
    sum.plusTerms.add(token)
    sum.plusTerms.add(expr)
    return sum
}

fun add(token: Token, sum: Sum): Expr {

    //println ("add(Token, Sum token = ${token.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")
    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return token
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.size == 0){
        return add(token, sum.plusTerms[0])
    }

    println("add (Token, Sum) token = ${token.toAnnotatedString()}, sum= ${sum.toAnnotatedString()}")
    if (isStateVariableExpr(token)){
        return addStateExpressionToSum(token, sum)
    }

    val newSum = Sum()
    newSum.plusTerms.addAll(sum.plusTerms)
    newSum.minusTerms.addAll(sum.minusTerms)
    newSum.plusTerms.add(token)
    println("add (token, sum) newSum = ${newSum.toAnnotatedString()}" )
    val expr = combineTerms(newSum)
    println("add (token, sum) combined = ${expr.toAnnotatedString()}" )
    return expr
}

// ***********************    Number   ****************************************************

fun add (number: Number, token: Token):Expr {
    return add(token, number)
}
fun add(number1: Number, number2: Number): Expr {
    return Number(number1.value + number2.value)
}

fun add (number: Number, term: Term): Expr {
    val sum = Sum()

    val expr = reduce(term)

    if (expr !is Term) {
        return add(number, expr)
    }

    if (number.value == 0.0){
        return term
    }

    sum.plusTerms.add(number)
    sum.plusTerms.add(term)
    return sum
}


fun add(number: Number, sum: Sum): Expr {

    println ("add(number , sum) number = ${number.toAnnotatedString()}  sum = ${sum.toAnnotatedString()}")

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return number
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.size == 0){
        return add(number, sum.plusTerms[0])
    }

    if (number.value == 0.0) {
        return sum
    }

    val newSum = Sum()
    newSum.plusTerms.addAll(sum.plusTerms)
    newSum.minusTerms.addAll(sum.minusTerms)
    newSum.plusTerms.add(number)
    val combined = combineTerms(newSum)
    println ("add(number , sum) newSum = ${newSum.toAnnotatedString()} returning combined = ${combined.toAnnotatedString()}")
    return combined
}

// ***********************  Term   ****************************************************

fun add(term: Term, token: Token): Expr {
    return add(token, term)
}

fun add(term: Term, number: Number): Expr {
    return add(number, term)
}

fun add (term1: Term, term2: Term): Expr{

    if (isStateVariableExpr(term1) && isStateVariableExpr(term2) &&
        getTokenFromStateExpression(term1).equals(getTokenFromStateExpression(term2))) {
        return addSubtractStateExpressions(term1,term2, ADD)
    }

    val expr1 = reduce(term1)
    val expr2 = reduce(term2)

    if (expr1 !is Term || expr2 !is Term){
        return add(expr1, expr2)
    }

    val sum = Sum()
    sum.plusTerms.add(expr1)
    sum.plusTerms.add(expr2)
    return combineTerms(sum)
}

fun add (term: Term, sum: Sum): Expr {

    println("add (term, sum) term = ${term.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return term
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.size == 0){
        return add(term, sum.plusTerms[0])
    }

    if (isStateVariableExpr(term)){
        val expr = addStateExpressionToSum(term, sum)
        println("add(term, sum) returning expr = ${expr.toAnnotatedString()}")
        return expr
    }

    val expr = reduce(term)

    if (expr !is Term) {
        return add (expr, sum)
    }

    val newSum = Sum()
    newSum.plusTerms.addAll(sum.plusTerms)
    newSum.minusTerms.addAll(sum.minusTerms)
    newSum.plusTerms.add(term)
    println("add(term, sum) calling combine terms on newSum = ${newSum.toAnnotatedString()}")
    return combineTerms(newSum)

}

// ***********************  Sum   ****************************************************

fun add (sum: Sum, token: Token): Expr {

    /*if (isStateVariableExpr(token)) {
        return addSubtractStateExpressions(token, sum, ADD)
    }*/

    return add(token, sum)
}

fun add (sum: Sum, number: Number): Expr {
    return add(number, sum)
}

fun add(sum: Sum, term: Term): Expr {

   return add (term, sum)
}

/*fun add (sum1: Sum, sum2: Sum): Expr {

    val sum = Sum()
    sum.plusTerms.addAll(sum1.plusTerms)
    sum.minusTerms.addAll(sum1.minusTerms)
    sum.plusTerms.addAll(sum2.plusTerms)
    sum.minusTerms.addAll(sum2.minusTerms)
    return combineTerms(sum)
}*/

fun add(sum1: Sum, sum2: Sum): Expr {

    if (sum1.plusTerms.isEmpty() && sum1.minusTerms.isEmpty() && sum2.plusTerms.isEmpty() and sum2.minusTerms.isEmpty()){
        return Number(0.0)
    }

    if (sum1.plusTerms.isEmpty() && sum1.minusTerms.isEmpty()) {
        return sum2
    }

    if (sum2.plusTerms.isEmpty() && sum2.minusTerms.isEmpty()) {
        return sum1
    }

    if (sum1.plusTerms.size == 1 && sum1.minusTerms.isEmpty()){
        return add(sum1.plusTerms[0], sum2)
    }

    if (sum2.plusTerms.size == 1 && sum2.minusTerms.isEmpty()){
        return add(sum2.plusTerms[0], sum1)
    }

    var newSum = Sum()
    var expr:Expr
    var foundOne = false

    println("add(sum,sum) sum1 = ${sum1.toAnnotatedString()},  sum2 = ${sum2.toAnnotatedString()}")
    expr = addSubtractStateExpressionListFromSum(sum2.plusTerms, sum1, ADD)
    println("add (sum,sum) expr= ${expr.toAnnotatedString()}")
    if (expr is Sum){
        newSum = expr
    } else {
        newSum.plusTerms.add(expr)
    }

    expr = addSubtractStateExpressionListFromSum(sum2.minusTerms, newSum, SUBTRACT)

    if (expr is Sum){
        return combineTerms(expr)
    }

    return expr
}
