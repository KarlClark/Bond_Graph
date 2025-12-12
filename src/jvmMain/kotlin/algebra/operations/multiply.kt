package algebra.operations

import algebra.*
import algebra.Number
import bondgraph.AlgebraException

fun multiply (expr1: Expr, expr2: Expr): Expr {
    //println("multiply expr1 = ${expr1.toAnnotatedString()}: ${expr1::class.simpleName}  expr2 = ${expr2.toAnnotatedString()}: ${expr2::class.simpleName}")
    when (expr1) {

        is Token -> when (expr2) {
            is Token -> return multiply (expr1, expr2)
            is Number -> return multiply (expr1, expr2)
            is Term -> return multiply (expr1, expr2)
            is Sum -> return multiply (expr1, expr2)
        }

        is Number -> when (expr2) {
            is Token -> return multiply (expr1, expr2)
            is Number -> return multiply (expr1, expr2)
            is Term -> return multiply (expr1, expr2)
            is Sum -> return multiply (expr1, expr2)
        }

        is Term -> when (expr2) {
            is Token -> return multiply (expr1, expr2)
            is Number -> return multiply (expr1, expr2)
            is Term -> return multiply (expr1, expr2)
            is Sum -> return multiply (expr1, expr2)
        }

        is Sum -> when (expr2) {
            is Token -> return multiply (expr1, expr2)
            is Number -> return multiply (expr1, expr2)
            is Term -> return multiply (expr1, expr2)
            is Sum -> return multiply  (expr1, expr2)
        }
    }

    return Token("ERROR Multiply")
}

fun multiplyList(expr: Expr, exprList:ArrayList<Expr>): ArrayList<Expr> {

    if (expr is Sum)throw IllegalArgumentException("function multiplyList can't handle a Sum as the first argument. Argumetment was ${expr.toAnnotatedString()}")

    val newList = arrayListOf<Expr>()

    exprList.forEach {
        val term = Term()
        if (it is Term){
            term.numerators.addAll(it.numerators)
            term.denominators.addAll(it.denominators)
        } else {
            term.numerators.add(it)
        }
        if (expr is Term){
            term.numerators.addAll(expr.numerators)
            term.denominators.addAll(expr.denominators)
        } else {
            term.numerators.add(expr)
        }

        //println("calling rationalizeTerm on ${term.toAnnotatedString()}")
        newList.add(rationalizeTerm(term))
    }

    return newList
}
// ***********************  Token   ****************************************************

fun multiply(token1: Token, token2: Token ): Expr {
    val term = Term()

    if (isStateVariableExpr(token1)) {
        term.numerators.add(token2)
        term.numerators.add(token1)
    } else {
        term.numerators.add(token1)
        term.numerators.add(token2)
    }
    return term
}

fun multiply(token: Token, number: Number ): Expr {
    val term = Term()

    if (number.value == 0.0) {
        return number
    }

    if (number.value == 1.0) {
        return token
    }

    term.numerators.add(number)
    term.numerators.add(token)

    return term
}

fun multiply(token: Token, term: Term): Expr {
    val expr = reduce(term)

    println("multply(token, term) token = ${token.toAnnotatedString()}, term = ${term.toAnnotatedString()},  expr = ${expr.toAnnotatedString()}")

    if (expr !is Term){
        return multiply(token, expr)
    }

    if (isStateVariableExpr(expr) && isStateVariableExpr(token)){
        throw AlgebraException("multiply(term, token) with two state variable expressions. token = ${token.toAnnotatedString()}, term = ${term.toAnnotatedString()}")
        }

    if (isStateVariableExpr(token)){
        val term = Term()
        term.numerators.add(expr)
        term.numerators.add(token)
        return term
    }

    if (isStateVariableExpr(expr)){
        val term = Term()
        term.numerators.add(multiply (getTermFromStateExpression(expr), token))
        term.numerators.add(getTokenFromStateExpression(expr))
        return term
    }

    val newTerm = Term()
    newTerm.numerators.add(token)
    newTerm.numerators.addAll(expr.numerators)
    newTerm.denominators.addAll(expr.denominators)
    val newExpr = rationalizeTerm(newTerm)
    println ("multiply(token, term) newTerm = ${newTerm.toAnnotatedString()}, rationalize term = ${newExpr.toAnnotatedString()}")
    printExpr(newTerm)
    printExpr(newExpr)
    return newExpr
}

fun multiply(token: Token, sum: Sum): Expr {
    println("multiply(Token, Sum)  token = ${token.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")
    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return Number(0.0)
    }

    if (sum.plusTerms.isEmpty() && sum.minusTerms.size == 1){
        val term = token.multiply(negate(sum))
        return createNegativeExpression(term)
    }

    if (isStateVariableExpr(token)){
       return multiplySumByStateExpression(token, sum)
    }

    val newSum = Sum()

 /*   newSum.plusTerms.addAll(multiplyList(token, sum.plusTerms))
    newSum.minusTerms.addAll(multiplyList(token, sum.minusTerms))
    return combineTerms(newSum)*/

    return multiplySumByExpression(token, sum)
}
// ***********************  Number   ****************************************************

fun multiply(number: Number, token: Token ): Expr {
   return multiply(token, number)
}

fun multiply(number1: Number, number2: Number ): Expr {
    return Number(number1.value * number2.value)
}


fun multiply(number: Number, term: Term): Expr {

    println("multiply(number, term) number = ${number.toAnnotatedString()},  term = ${term.toAnnotatedString()}")

    if (number.value == 0.0){
        return number
    }

    if (number.value == 1.0) {
        return reduce(term)
    }

    val expr = reduce(term)
    println("multiply(number, term) reduced term  = ${expr.toAnnotatedString()}")

    if (expr !is Term){
        return multiply(number, expr)
    }

    if (isStateVariableExpr(term)){
        val newTerm = Term()
        newTerm.numerators.add( multiply (number, getTermFromStateExpression(term)))
        newTerm.numerators.add(getTokenFromStateExpression(term))
        return newTerm
    }

    val newTerm = Term()

    newTerm.numerators.add(number)
    newTerm.numerators.addAll(expr.numerators)
    newTerm.denominators.addAll(expr.denominators)
    return rationalizeTerm(newTerm)
}

fun multiply(number: Number, sum: Sum): Expr {

    if (number.value == 0.0){
        return number
    }

    if (number.value ==  1.0) {
        return sum
    }

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return Number(0.0)
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return multiply(number, sum.plusTerms[0])
    }

    if (sum.plusTerms.isEmpty() && sum.minusTerms.size == 1){
        val expr = number.multiply(negate(sum))
        return createNegativeExpression(expr)
    }

    val newSum = Sum()

    /*newSum.plusTerms.addAll(multiplyList(number, sum.plusTerms))
    newSum.minusTerms.addAll(multiplyList(number, sum.minusTerms))
    return combineTerms(newSum)*/

    return multiplySumByExpression(number, sum)
}

// ***********************  Term   ****************************************************

fun multiply (term: Term, token: Token): Expr{
    return multiply(token, term)
}

fun multiply(term: Term, number: Number): Expr {
    //println("multiply (term,number) term = ${term.toAnnotatedString()}, number = ${number.toAnnotatedString()}")
    return multiply(number, term)
}

fun multiply(term1: Term, term2: Term): Expr{
    val expr1 = reduce(term1)
    val expr2 = reduce(term2)

    println("multiply(term, term) term1 = ${term1.toAnnotatedString()}, expr1 = ${expr1.toAnnotatedString()},  term2 = {${term2.toAnnotatedString()}, expr2 = ${expr2.toAnnotatedString()}")

    if (expr1 !is Term || expr2 !is Term){
        return multiply(expr1, expr2)
    }

    val term1IsState = isStateVariableExpr(term1)
    val term2IsState = isStateVariableExpr(term2)

    if (term1IsState && term2IsState) {
        throw AlgebraException("multiply(term, term) called with two state expressions.  term1 = ${term1.toAnnotatedString()},  term2 = ${term2.toAnnotatedString()}")
    }

    if (term1IsState || term2IsState){
        val stateTerm = if (term1IsState) term1 else term2
        val normalTerm = if (term1IsState) term2 else term1
        val expr = getTermFromStateExpression(stateTerm)
        val token = getTokenFromStateExpression(stateTerm)
        val term = Term()
        println("multiply(terml, term) state term, token = ${token.toAnnotatedString()}, expr = ${expr.toAnnotatedString()}, normalTerm = ${normalTerm.toAnnotatedString()}")
        term.numerators.add(multiply(expr, normalTerm))
        term.numerators.add(token)
        return term
    }

    val term = Term()
    term.numerators.addAll(expr1.numerators)
    term.numerators.addAll(expr2.numerators)
    term.denominators.addAll(expr1.denominators)
    term.denominators.addAll(expr2.denominators)

    return rationalizeTerm(term)
}

fun multiply(term: Term, sum: Sum): Expr {
    val expr = reduce(term)

    println("multiply(term, sum) term = ${term.toAnnotatedString()}, expr = ${expr.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")
    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return Number(0.0)
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.isEmpty()){
        return multiply(term, sum.plusTerms[0])
    }

    if (sum.plusTerms.isEmpty() && sum.minusTerms.size == 1){
        val expr = term.multiply(negate(sum))
        return createNegativeExpression(expr)
    }


    if (expr !is Term){
        return multiply(expr, sum)
    }

    if (isStateVariableExpr(expr)){
       val newExpr = multiplySumByStateExpression(expr, sum)
        println("multiply(term ,sum) new state expression = ${newExpr.toAnnotatedString()} ")
        return newExpr
    }

    val newSum = Sum()

    /*sum.plusTerms.forEach { newSum.plusTerms.add(multiply(expr, it)) }
    sum.minusTerms.forEach { newSum.minusTerms.add(multiply(expr, it))}

    return combineTerms(newSum)*/
    return multiplySumByExpression(term, sum)
}



// ***********************  Sum   ****************************************************

fun multiply(sum: Sum, token: Token): Expr {
    return multiply(token, sum)
}

fun multiply(sum: Sum, number: Number): Expr{
    return multiply(number, sum)
}

fun multiply(sum: Sum, term: Term): Expr {
    return multiply(term, sum)
}

fun multiply(sum1: Sum, sum2: Sum): Expr {

    fun multiplyTermsOfSumBySum(target: Sum, multiplier: Sum): Expr {
        var workerSum = Sum()
        val productSum = Sum()
        var negative = false
        if (multiplier.plusTerms.isEmpty()){
            workerSum.plusTerms.addAll(multiplier.minusTerms)
            negative = true
        } else {
            workerSum = multiplier
        }

        println("multiplyTermsOfSumBySum(sum, sum) target = ${target.toAnnotatedString()}, multiplier = ${multiplier.toAnnotatedString()}, workingExpr = ${workerSum.toAnnotatedString()}")

        target.plusTerms.forEach { expr ->
            val newExpr = multiply_f(expr, workerSum)
            if (exprIsNegative(newExpr)) {
                println("multiplyTermsOfSumBySum(sum, sum) newExpr is negative, newExpr = ${newExpr.toAnnotatedString()} convert to positive =${(convertNegativeToPositive(newExpr).toAnnotatedString())}")
                productSum.minusTerms.add(convertNegativeToPositive(newExpr))
            } else {
                productSum.plusTerms.add(newExpr)
            }
        }

        target.minusTerms.forEach { expr ->
            val newExpr = multiply_f(expr, workerSum)
            if (exprIsNegative(newExpr)) {
                productSum.plusTerms.add(convertNegativeToPositive(newExpr))
            } else {
                productSum.minusTerms.add(newExpr)
            }
        }

        if (negative){
            return negate(productSum)
        }

        return productSum
    }

    fun negativeMultiply(expr: Expr, sum: Sum): Expr {
        val newExpr = multiply(expr, sum)
        if (newExpr is Sum) {
            return negate(newExpr)
        }
        val newSum = Sum()
        newSum.minusTerms.add(newExpr)
        return newSum
    }

    if (sumContainsStateExpressions(sum1) && sumContainsStateExpressions(sum2)) {
        throw AlgebraException("multiply(sum1, sum2) attempt to multiply two sums that each have state expressions in them.  sum1 = ${sum1.toAnnotatedString()}, sume2 = ${sum2.toAnnotatedString()}")
    }

    if (sum1.plusTerms.size + sum1.minusTerms.size == 0){
        return Number(0.0)
    }

    if (sum2.plusTerms.size + sum2.minusTerms.size == 0){
        return Number(0.0)
    }

    /*if (sum1.plusTerms.size == 1 && sum1.minusTerms.isEmpty()) {
        return multiply(sum1.plusTerms[0], sum2)
    }

    if (sum2.plusTerms.size == 1 && sum2.minusTerms.isEmpty()) {
        return multiply(sum2.plusTerms[0], sum1)
    }*/

    if (sum1.plusTerms.isEmpty() && sum1.minusTerms.size ==1 &&
        sum2.plusTerms.isEmpty() && sum2.minusTerms.size == 1) {
        return multiply(sum1.minusTerms[0], sum2.minusTerms[0])
    }

    if (sum1.plusTerms.isEmpty() && sum1.minusTerms.size == 1){
        return negativeMultiply(sum1.minusTerms[0], sum2)
    }

    if (sum2.plusTerms.isEmpty() && sum2.minusTerms.size == 1){
        return negativeMultiply(sum2.minusTerms[0], sum1)
    }

    if (sumContainsStateExpressions(sum1)) {
        return multiplyTermsOfSumBySum(sum1, sum2)
    }

    if (sumContainsStateExpressions(sum2)) {
        return multiplyTermsOfSumBySum(sum2, sum1)
    }

    val term = Term()
    term.numerators.add(sum1)
    term.numerators.add(sum2)
    return rationalizeTerm(term)
}