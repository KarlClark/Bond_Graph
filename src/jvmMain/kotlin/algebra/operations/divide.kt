package algebra.operations

import algebra.*
import algebra.Number
import androidx.compose.animation.Animatable
import bondgraph.AlgebraException

fun divide (expr1: Expr, expr2: Expr): Expr {
    //println("divide expr1 = ${expr1.toAnnotatedString()}: ${expr1::class.simpleName}  expr2 = ${expr2.toAnnotatedString()}: ${expr2::class.simpleName}")
    when (expr1) {

        is Token -> when (expr2) {
            is Token -> return divide (expr1, expr2)
            is Number -> return divide (expr1, expr2)
            is Term -> return divide (expr1, expr2)
            is Sum -> return divide (expr1, expr2)
        }

        is Number -> when (expr2) {
            is Token -> return divide (expr1, expr2)
            is Number -> return divide (expr1, expr2)
            is Term -> return divide (expr1, expr2)
            is Sum -> return divide (expr1, expr2)
        }

        is Term -> when (expr2) {
            is Token -> return divide (expr1, expr2)
            is Number -> return divide (expr1, expr2)
            is Term -> return divide (expr1, expr2)
            is Sum -> return divide (expr1, expr2)
        }

        is Sum -> when (expr2) {
            is Token -> return divide (expr1, expr2)
            is Number -> return divide (expr1, expr2)
            is Term -> return divide (expr1, expr2)
            is Sum -> return divide (expr1, expr2)
        }
    }

    return Token("ERROR Divide")
}

// ***********************  Token   ****************************************************

fun divide (token1: Token, token2: Token): Expr {
    val term = Term()

    if (isStateVariableExpr(token2)) {
        throw AlgebraException ("divide(token, token) attempt to divide by a state token.  token2 = ${token2.toAnnotatedString()}")
    }

    if (token1.equals(token2)) {
        return Number(1.0)
    }

    if (isStateVariableExpr(token1)){
       return divideStateExpressionByExpression(token1, token2)
    }



    term.numerators.add(token1)
    term.denominators.add(token2)
    return term
}

fun divide (token: Token, number: Number): Expr {

    if (number.value == 0.0){
        throw AlgebraException("divide(token, number) divide by zero")
    }

    if (number.value == 1.0) {
        return token
    }

    if (isStateVariableExpr(token)){
        return divideStateExpressionByExpression(token,number)
    }

    val term = Term()
    term.numerators.add(Number(1.0/number.value)) //Keep numbers out of denominators
    term.numerators.add(token)
    return term
}

fun divide (token: Token, term: Term): Expr {

    if (isStateVariableExpr(term)) {
        throw AlgebraException("divide(token, term) attempt to divide by state variable expression. term = ${term.toAnnotatedString()}")
    }

    val expr = reduce(term)

    println("divide (token, term) term = ${term.toAnnotatedString()}, expr = ${expr.toAnnotatedString()}, token = ${token.toAnnotatedString()}")

    if (expr !is Term){
        return divide(token, expr)
    }

    if (isStateVariableExpr(token)){
        return divideStateExpressionByExpression(token, expr)
    }

    val newTerm = Term()
    newTerm.numerators.add(token)
    newTerm.numerators.addAll(expr.denominators)
    newTerm.denominators.addAll(expr.numerators)
    return rationalizeTerm(newTerm)
}

fun divide(token: Token, sum: Sum): Expr {

    //println("divide(token, sum) token= ${token.name}  sum= ${sum.toAnnotatedString()}")

   /* if (sum.plusTerms.size + sum.minusTerms.size == 0){
        throw IllegalArgumentException("Divide by zero in divide(Token, Sum)  sum = ${sum.toAnnotatedString()} ")
    }
    val comDemExpr = commonDenominator(sum)
    val term = Term()
    term.numerators.add(token)
    if (comDemExpr is Term) {
        term.numerators.addAll(comDemExpr.denominators)
        term.denominators.addAll(comDemExpr.numerators)
    } else {
        term.denominators.add(comDemExpr)
    }
    return rationalizeTerm(term)*/

    if (sum.plusTerms.isEmpty() && sum.minusTerms.isEmpty()){
        throw AlgebraException("divide (token, sum)  divide by 0 empty sum.")
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return divide(token, sum.plusTerms[0])
    }

    val expr = convertSumToCommonDenominator(sum)


    if (isStateVariableExpr(expr)) {
        throw AlgebraException("divide (token, sum) attempt to divide by a sum that contains a state expression.  sum = ${sum.toAnnotatedString()}")
    }
    //If expr is Sum, then there were no fractions in the sum and there
    //really isn't a common denominator, or you could say the common
    //denominator is 1.  At any rate we must handle this case locally, or
    //we will loop recalling this function recursively.
    if (expr is Sum) {
        val term = Term()
        term.numerators.add(token)
        term.denominators.add(sum)
        return term
    }

    return divide(token, expr)
}
// ***********************  Number   ****************************************************

fun divide(number: Number, token: Token): Expr {

    if (isStateVariableExpr(token)) {
        throw AlgebraException("divide(number, token) attempt to divide by state token.  token = ${token.toAnnotatedString()}")
    }

    if (number.value == 0.0){
        return number
    }

    val term = Term()
    term.numerators.add(number)
    term.denominators.add(token)
    return term
}

fun divide (number1: Number, number2: Number): Expr {

    return Number(number1.value / number2.value)
}

fun divide(number: Number, term: Term): Expr {

    if (number.value == 0.0) {
        return number
    }

    if (isStateVariableExpr(term)) {
        throw AlgebraException("divide(number, term) attempt to divide by state token.  token = ${term.toAnnotatedString()}")
    }

    val expr = reduce(term)

    if (expr !is Term) {
        return divide(number, expr)
    }

    val newTerm = Term()
    newTerm.numerators.add(number)
    newTerm.numerators.addAll(term.denominators)
    newTerm.denominators.addAll(term.numerators)
    return rationalizeTerm(newTerm)
}

fun divide (number: Number, sum: Sum): Expr {

    if (number.value == 0.0) {
        return number
    }

    if (sum.plusTerms.size + sum.minusTerms.size == 0) {
        throw IllegalArgumentException("Divide by zero. sum passed to divide(Number, Sum) is zero")
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return divide(number, sum.plusTerms[0])
    }

    if (sum.plusTerms.isEmpty() && sum.minusTerms.size == 1){
        val expr = divide(number, sum.minusTerms[0])
       /* val newSum = Sum()
        newSum.minusTerms.add(expr)
        return newSum*/
        return createNegativeExpression(expr)
    }

   /* val comDemExpr = commonDenominator(sum)
    val term = Term()
    term.numerators.add(number)
    if (comDemExpr is Term) {
        term.numerators.addAll(comDemExpr.denominators)
        term.denominators.addAll(comDemExpr.numerators)
    } else {
        term.denominators.add(comDemExpr)
    }

    return rationalizeTerm(term)*/

    val expr = convertSumToCommonDenominator(sum)

    if (isStateVariableExpr(expr)) {
        throw AlgebraException("divide (number, sum) attempt to divide by a sum that contains a state expression.  sum = ${sum.toAnnotatedString()}")
    }

    //If expr is Sum, then there were no fractions in the sum and there
    //really isn't a common denominator, or you could say the common
    //denominator is 1.  At any rate we must handle this case locally, or
    //we will loop recalling this function recursively.
    if (expr is Sum) {
        val term = Term()
        term.numerators.add(number)
        term.denominators.add(sum)
        return term
    }

    return divide(number, expr)
}

// ***********************  Term   ****************************************************

fun divide (term: Term, token: Token): Expr {

    if (isStateVariableExpr(token)) {
        throw AlgebraException("divide(term,token) attempt to divide by state token.  token= ${token.toAnnotatedString()}")
    }

    var expr = reduce (term)

    println("divide (term, token) term = ${term.toAnnotatedString()}, expr = ${expr.toAnnotatedString()}, token = ${token.toAnnotatedString()}")
    if (expr !is Term) {
        return divide (expr, token)
    }

    if (isStateVariableExpr(expr)){
        return divideStateExpressionByExpression(expr, token)
    }

    val newTerm = Term()
    newTerm.numerators.addAll(expr.numerators)
    newTerm.denominators.addAll(expr.denominators)
    newTerm.denominators.add(token)
    return rationalizeTerm(newTerm)
}

fun divide (term: Term, number: Number): Expr {

    if (number.value == 0.0){
        throw AlgebraException("divide(term, number) divide by zero.")
    }

    if (number.value == 1.0) {
        return term
    }

    var expr = reduce (term)
    if (expr !is Term) {
        return divide (expr, number)
    }

    if (isStateVariableExpr(term)){
        return divideStateExpressionByExpression(term, number)
    }

    val newTerm = Term()
    newTerm.numerators.add(Number(1.0/number.value))  // Keep numbers out of denominators
    newTerm.numerators.addAll(expr.numerators)
    newTerm.denominators.addAll(expr.denominators)

    return rationalizeTerm(newTerm)
}

fun divide (term1: Term, term2: Term): Expr {

    if (isStateVariableExpr(term2)) {
        throw AlgebraException("divide(term, term)  attempt to divide by state expression.  term2 = ${term2.toAnnotatedString()}")
    }

    val expr1 = reduce(term1)
    val expr2 = reduce(term2)

    println("divide(term, term) term1 = ${term1.toAnnotatedString()}, term2 = ${term2.toAnnotatedString()} expr1 = ${expr1.toAnnotatedString()}, expr2 = ${expr2.toAnnotatedString()}" )

    if (term1.equals(term2)) {
        return Number(1.0)
    }

    if (expr1 !is Term || expr2 !is Term){
        return divide(expr1, expr2)
    }

    if (isStateVariableExpr(expr1)) {
        return divideStateExpressionByExpression(expr1, expr2)
    }

    val newTerm = Term()
    newTerm.numerators.addAll(expr1.numerators)
    newTerm.numerators.addAll(expr2.denominators)
    newTerm.denominators.addAll(expr1.denominators)
    newTerm.denominators.addAll(expr2.numerators)

    return rationalizeTerm(newTerm)
}

fun divide (term: Term, sum: Sum): Expr {
    val expr = reduce(term)

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        throw IllegalArgumentException("Divide by zero, Sum = ${sum.toAnnotatedString()}")
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return divide(term, sum.plusTerms[0])
    }

    if (expr !is Term){
        return divide(expr, sum)
    }

    if (isStateVariableExpr(expr)) {
        return divideStateExpressionByExpression(expr, sum)
    }

    /*val comDenomExpr = commonDenominator(sum)
    val newTerm = Term()
    newTerm.numerators.addAll(expr.numerators)
    newTerm.denominators.addAll(expr.denominators)
    if (comDenomExpr is Term){
        newTerm.numerators.addAll(comDenomExpr.denominators)
        newTerm.denominators.addAll(comDenomExpr.numerators)
    } else {
        newTerm.denominators.add(comDenomExpr)
    }

    return rationalizeTerm(newTerm)*/

    val commonExpr = convertSumToCommonDenominator(sum)

    if (isStateVariableExpr(commonExpr)) {
        throw AlgebraException("divide (term, sum)  attempt to divide by a sum that contains a state expression.  sum = ${sum.toAnnotatedString()}")
    }

    //If expr is Sum, then there were no fractions in the sum and there
    //really isn't a common denominator, or you could say the common
    //denominator is 1.  At any rate we must handle this case locally, or
    //we will loop recalling this function recursively.
    if (commonExpr is Sum) {
        val newTerm = Term()
        newTerm.numerators.addAll(term.numerators)
        newTerm.denominators.addAll(term.denominators)
        newTerm.denominators.add(sum)
        return newTerm
    }
    return divide(expr, commonExpr)
}
// ***********************  Sum   ****************************************************

fun divide (sum: Sum, token: Token): Expr {

    if (isStateVariableExpr(token)){
        throw AlgebraException("divide (sum, token) attempt to divide by state token.  token = ${token.toAnnotatedString()}")
    }

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return Number(0.0)
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return divide( sum.plusTerms[0], token)
    }

    val newSum = Sum()

    sum.plusTerms.forEach {
        newSum.plusTerms.add(divide(it, token))
    }
    sum.minusTerms.forEach {
        newSum.minusTerms.add(divide(it, token))
    }

    return combineTerms(newSum)
}

fun divide (sum: Sum, number: Number): Expr {


    if (number.value == 0.0) {
        throw AlgebraException("divide(sum, number) divide by zero")
    }

    if (number.value == 1.0) {
        return sum
    }

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return Number(0.0)
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return divide( sum.plusTerms[0], number)
    }

    val newSum = Sum()

    sum.plusTerms.forEach {
        newSum.plusTerms.add(divide(it, number))
    }
    sum.minusTerms.forEach {
        newSum.minusTerms.add(divide(it, number))
    }

    return combineTerms(newSum)
}

fun divide(sum: Sum, term: Term): Expr {

    if (isStateVariableExpr(term)){
        throw AlgebraException("divide (sum, term) attempt to divide by state expression.  term = ${term.toAnnotatedString()}")
    }

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return Number(0.0)
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return divide( sum.plusTerms[0], term)
    }

    val expr = reduce(term)
    if (expr !is Term) {
        return divide(sum, expr)
    }

    if (expr.numerators.contains(sum)){
        val newTerm = Term()
        newTerm.numerators.addAll(expr.denominators)
        newTerm.denominators.addAll(term.numerators - sum)
        return newTerm
    }

    val newSum = Sum()

    sum.plusTerms.forEach {
        newSum.plusTerms.add(divide(it, expr))
    }
    sum.minusTerms.forEach {
        newSum.minusTerms.add(divide(it, expr))
    }

    return combineTerms(newSum)

}

fun divide(sum1: Sum, sum2: Sum): Expr {

    println("divide(sum, sum) sum1 = ${sum1.toAnnotatedString()}, sum2 = ${sum2.toAnnotatedString()}")

    if (sum2.plusTerms.size + sum2.minusTerms.size == 0){
        throw IllegalArgumentException("Divide by zero, sum2 = ${sum2.toAnnotatedString()}")
    }

    if (sum1.plusTerms.size + sum1.minusTerms.size == 0){
        return Number(0.0)
    }

    if (sum1.plusTerms.size == 1 && sum1.minusTerms.isEmpty()){
        return divide( sum1.plusTerms[0], sum2)
    }

    if (sum2.plusTerms.size == 1 && sum2.minusTerms.isEmpty()){
        return divide( sum2.plusTerms[0], sum1)
    }

    if (sum1.plusTerms.isEmpty() && sum2.plusTerms.isEmpty() && sum1.minusTerms.size ==1 && sum2.minusTerms.size == 1){
        return divide(sum1.minusTerms[0], sum2.minusTerms[0])
    }

    if (sum1.equals(sum2)) {
        return Number(1.0)
    }

    if (sum1.equals(negate(sum2))) {
       /* val sum = Sum()
        sum.minusTerms.add(Number(1.0))
        return sum*/
        return createNegativeExpression(Number(1.0))
    }

    val comDenomExpr = convertSumToCommonDenominator(sum2)
    if (isStateVariableExpr(comDenomExpr)) {
        throw AlgebraException("divide (sum, sum)  attempt to divide by sum that contains a state expression.  sum2 = ${sum2.toAnnotatedString()}")
    }
    val newSum = Sum()

    sum1.plusTerms.forEach {
        newSum.plusTerms.add(divide(it, comDenomExpr))
    }

    sum1.minusTerms.forEach {
        newSum.minusTerms.add(divide(it, comDenomExpr))
    }

    return combineTerms(newSum)
}