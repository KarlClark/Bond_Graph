package algebra.operations

/*import algebra.Expr
import algebra.Number
import algebra.Sum
import algebra.Term
import algebra.Token
import algebra.combineTerms
import algebra.commonDenominator
import algebra.generateTermFromList
import algebra.getDenominatorList
import algebra.getNumerator
import algebra.getNumeratorFactor
import algebra.getTermFromStateExpression
import algebra.getTokenFromStateExpression
import algebra.isStateVariableExpr
import algebra.matchingStateExpressions
import algebra.negate*/
import algebra.*
import algebra.Number

import bondgraph.AlgebraException
import bondgraph.Operation
import bondgraph.Operation.*


var count = 0
/*
multiply - factored.  Our normal multiply function multiplies each term in a sum.
So, x + y + z  times a becomes xa + ya + za.  This function returns (x + y + z)a
i.e. the factored form of the first expression. If neither expression is a sum, then
return the results of the normal multiply function.
 */
fun multiply_f(expr1: Expr, expr2: Expr): Expr {

    fun multiplyExpressionAndSum(expr: Expr, sum: Sum): Expr {
        val term = Term()
        when (expr) {

            is Token -> {
                term.numerators.add(expr)
                term.numerators.add(sum)
            }

            is Number -> {
                term.numerators.add(expr)
                term.numerators.add(sum)
            }

            is Term -> {
                term.numerators.addAll(expr.numerators)
                term.numerators.add(sum)
                term.denominators.addAll(expr.denominators)
            }

            is Sum -> {
                term.numerators.add(expr)
                term.numerators.add(sum)
            }
        }

        return cancel(term)
    }

    var workingExpr1: Expr
    var workingExpr2: Expr
    var newExpr: Expr
    var isStateExpression = false
    var isNegative = false
    var stateToken = Token()

    if ( (isStateVariableExpr(expr1) || (expr1 is Sum && sumContainsStateExpressions(expr1))) &&
         (isStateVariableExpr(expr2) || (expr2 is Sum && sumContainsStateExpressions(expr2)))) {
        throw AlgebraException("multiply_f(expr1, expr2) attempt to multiply two expressions that each contain state tokens.  expr1 = ${expr1.toAnnotatedString()}, expr2 = ${expr2.toAnnotatedString()}")
    }
    /*
    We want to work with the expression that is not a state expression, and the part of the state expression that doesn't
    include the state variable. From the check above, we know only one expression, if either, is state expression so we can
    just check both of them.
     */
    if (isStateVariableExpr(expr1)){
        workingExpr1 = getTermFromStateExpression(expr1)
        stateToken = getTokenFromStateExpression(expr1)
        isStateExpression = true
    } else {
        workingExpr1 = expr1
    }

    if (isStateVariableExpr(expr2)){
        workingExpr2 = getTermFromStateExpression(expr2)
        stateToken = getTokenFromStateExpression(expr2)
        isStateExpression = true
    } else {
        workingExpr2 = expr2
    }

    /*
    Check both expressions to see if they are negative. If they are, convert them to positive expressions, and
    keep track of the sign of the final product, i.e. positive X positive = positive, negative X positive = negative etc.
     */
    if (exprIsNegative(workingExpr1)) {
        isNegative = true
        workingExpr1 = convertNegativeToPositive(workingExpr1)
    }

    if (exprIsNegative(workingExpr2)) {
        isNegative = ! isNegative
        workingExpr2 = convertNegativeToPositive(workingExpr2)
    }

    /*
    Do the multiplication. If neither expression is a sum, we can use our normal multiply function. If
    either or both expressions are sums, then use our local multiplyExpressionAndSum() function to do
    a factored multiply.
     */
    if (workingExpr1 !is Sum && workingExpr2 !is Sum){
        newExpr = multiply(workingExpr1, workingExpr2)
    } else {
        if (workingExpr1 is Sum) {
            newExpr = multiplyExpressionAndSum(workingExpr2, workingExpr1)
        } else {
            newExpr = multiplyExpressionAndSum(workingExpr1, workingExpr2 as Sum)
        }
    }


    //if final result is negative, then convert newExpr to negative.
    if (isNegative) {
        newExpr = createNegativeExpression(newExpr)
    }

    //create state expression if needed.
    if (isStateExpression){
        return createStateExpression(newExpr, stateToken)
    }

    return newExpr
}




/*
Add or subtract expr1 and expr2 returning a single term over a common denominator if necessary.
Steps:
    - Convert each expression to a positive expression. Set flags to keep track of the sign of each
      expression.  This affects whether to add or subtract the positive expressions, and how to
      interpret the sign of the result.
    - Break the expressions apart.  Create expressions that represent the numerator of each expression.
    - Create a list of terms in the denominators of each expression.  List may be empty.
    - Pass the lists to the commonDenominator function to create a common denominator, also a list.
    - For each expression, pass its denominator list along with the common denominator to the getNumeratorFactor function
      to generate a factor.
    - Multiply the numerator of each expression by its corresponding factor.
    - Add these two products together to create a sum.
    - Create a term from the common denominator list.
    - Divide the sum by the common denominator term.
    Ex:
    ab/mn + cd/xym
    numerators ab and cd.  list1 {m,n} list2 {x,y,m}
    common denominator list {x,y,m,n}
    factor1 xy  factor2 n
    product1 (ab)(xy) = abxy  product2 cdn
    sum abxy + cdn
    result (abxy + cdn)/xymn
 */
fun addSubtract_cd(expr1: Expr, expr2: Expr, operation: Operation): Expr {
    println("addSubtract_cd called expr1 = ${expr1.toAnnotatedString()},  expr2 = ${expr2.toAnnotatedString()}, operation = $operation")

    fun createNegativeTerm(numerator: Expr, divisor: Expr): Expr {
        /*
        We have negative numerator.  We want to create a fraction where the
        whole fraction is negative.  i.e convert something like  -ab/xy to -(ab/xy)
         */
        val newNumerator = convertNegativeToPositive(numerator)
        val sum = Sum()
        sum.minusTerms.add(divide_cd(newNumerator, divisor))
        return sum
    }
    var workingExpr1 = expr1
    var workingExpr2 = expr2
    var expr1_Positive = true
    var expr2_Positive = true
    var expr1_Negative = false
    var expr2_Negative = false

    if (expr1 is Sum && expr1.plusTerms.isEmpty() && expr1.minusTerms.size == 1){
        workingExpr1 = convertNegativeToPositive(expr1)
        expr1_Positive = false
        expr1_Negative = true
    }

    if (expr2 is Sum && expr2.plusTerms.isEmpty() && expr2.minusTerms.size == 1){
        workingExpr2 = convertNegativeToPositive(expr2)
        expr2_Positive = false
        expr2_Negative = true
    }

    val numerator1 = getNumerator(workingExpr1)
    val numerator2 = getNumerator(workingExpr2)


    /*if (numerator1.equals(Number(0.0))) {
        return expr2
    }*/
    if (numerator2.equals(Number(0.0))) {
        return expr1
    }

    val denominators1 = getDenominatorList(workingExpr1)
    val denominators2 = getDenominatorList(workingExpr2)
    val commonDenominatorList = commonDenominator(denominators1, denominators2)

    val factor1 = getNumeratorFactor(denominators1, commonDenominatorList)
    val factor2 = getNumeratorFactor(denominators2, commonDenominatorList)

    val product1 = multiply(numerator1, factor1)
    val product2 = multiply(numerator2, factor2)
    println("numerator1 = ${numerator1.toAnnotatedString()} factor1 = ${factor1.toAnnotatedString()} product1 = ${product1.toAnnotatedString()}")
    println("numerator2 = ${numerator2.toAnnotatedString()} factor2 = ${factor2.toAnnotatedString()} product2 = ${product2.toAnnotatedString()}")
    val divisor = generateTermFromList(commonDenominatorList)

    if (operation == ADD) {
        when {

            expr1_Positive && expr2_Positive -> {
                return divide_cd(product1.add(product2), divisor)
            }

            expr1_Positive && expr2_Negative -> {
                val numerator = product1.subtract(product2)
                if (exprIsNegative(numerator)){
                   return createNegativeTerm(numerator, divisor)
                } else {
                    return divide_cd(numerator, divisor)
                }
            }

            expr1_Negative && expr2_Positive -> {
                val numerator = product2.subtract(product1)
                if (exprIsNegative(numerator)){
                    return createNegativeTerm(numerator, divisor)
                } else {
                    return divide_cd(numerator, divisor)
                }
            }

            expr1_Negative && expr2_Negative -> {
                val numerator = product1.add(product2)
                val sum = Sum()
                sum.minusTerms.add(divide_cd(numerator, divisor))
                return sum
            }
        }
    } else { // operation is SUBTRACT
        when {

            expr1_Positive && expr2_Positive -> {
                val numerator = product1.subtract(product2)
                if (exprIsNegative(numerator)) {
                    return createNegativeTerm(numerator, divisor)
                } else {
                    return divide_cd(numerator, divisor)
                }
            }

            expr1_Positive && expr2_Negative -> {
                return divide_cd(product1.add(product2), divisor)
            }

            expr1_Negative && expr2_Positive -> {
                val numerator = product1.add(product2)
                val sum = Sum()
                sum.minusTerms.add(divide_cd(numerator, divisor))
                return sum
            }

            expr1_Negative && expr2_Negative -> {
                val numerator = product2.subtract(product1)
                if (exprIsNegative(numerator)) {
                    return createNegativeTerm(numerator, divisor)
                } else {
                    return divide_cd(numerator, divisor)
                }
            }
        }

    }
   return Term()  // shouldn't reach here
}

/*
This function adds two state expressions. A state expression has the general
form term X state variable  or sum X state variable so, Term(xy)P (a + b)P
We want to add these expressions over a common denominator and create a new
state expression. So the steps are
    - get the terms for each expression
    - use add_cd function to add them together over a common denominator.
    - use that result as the term of the new state expression
 */
fun addSubtractStateExpressions(expr1: Expr, expr2: Expr, operation: Operation): Expr {

    if ( ! (isStateVariableExpr(expr1) && isStateVariableExpr(expr2)) ) {
        throw AlgebraException("Call to addStateExpressions with at least one expression that is not a state expression. expr1 = ${expr1.toAnnotatedString()}  expr2 = ${expr2.toAnnotatedString()}")
    }

    val token1 = getTokenFromStateExpression(expr1)
    val token2 = getTokenFromStateExpression(expr2)

    if ( ! token1.equals(token2)) {
        throw AlgebraException("Call to addStateExpressions with expression that have different state variables.  expr1 = ${expr1.toAnnotatedString()}  expr2 = ${expr2.toAnnotatedString()}")
    }

    val term1 = getTermFromStateExpression(expr1)
    val term2 = getTermFromStateExpression(expr2)
    val newTerm = addSubtract_cd (term1, term2, operation)
    println ("addSubtractStateExpressions(expr, expr) expr1 ${expr1.toAnnotatedString()}, expr2 = ${expr2.toAnnotatedString()}, operation = $operation, newTerm = ${newTerm.toAnnotatedString()}")

    val term3 = createStateExpression(newTerm, token1)
    return term3
}


/*
The next set of functions deals with adding/subtracting a state expression to/from a Sum.
If the sum contains a matching state expression we want to combine the expressions into
one term and use the new term in the new Sum.  This opens up a can of worms on whether to add or
subtract the two expressions and whether to put the new expression in the plusTerms or
the minusTerms of the new sum.  As an example if P is a state token, and we have
ax - bP, and we want to subtract cP we want to construct ax - (b + c)P.  We see that
we are actually adding the two expressions and then putting the new expression in the
minusTerms of the new sum.  And there are preferences.  We prefer things like
ax + (by - cd)P as being more readable than ax - (cd - by)P because of the double negative
sign.  Basically, there are four cases (expr + Sum) (expr - Sum) (Sum + expr) and (Sum - expr).
The following chart summerizes things.
Operation          Sum expression       what to do        where to put
                     came from                            in new sum

expr add sum        plusTerms           expr + sumExpr     plusTerms
                    minusTerms          expr - sumExpr     plusTerms
expr subtract sum   plusTerms           expr - sumExpr     plusTerms
                    minusTerms          expr + sumExpr     plusTerms
Sum + expr          plusTerms           expr + sumExpr     plusTerms
                    minusTerms          expr - sumExpr     plusTerms
Sum - expr          plusTerms           sumExpr - expr     plusTerms
                    minusTerms          expr + sumExpr     minusTerms

Building this chart surprised me after trying to think this through in my head.
We see that in almost every case, partially because of the preference mentioned above,
the new term is placed in the plusTerms of the new sum.  Also, the order of operation can
almost always be (expr op sumExpr) except for the one case of (sumExpr - expr).
Since we will be calling our functions from the corresponding add() or subtract()
functions, I think it makes sense to write a function for each case just to make
the code in the add() and subtract() functions more readable. But if we write the
first one addSumToExpression the next two are easy. addExpressionToSum can just call
addSumToExpression directly.  and subtractSumFromExpression can also use
addSumToExpression by passing in a negated value of the sum.

Then is turned out that the addExpressionToSum and subtractExpressionFromSum were almost
identical, so I wound up writing the addSubtractExpressionToFromSum function which handles
both cases.
 */


/*
The idea behind the following function, is to search the sum (if it is a sum) for a term
that has the same state token as the expression.  If it finds one, it removes it from the
sum and returns it as the first element in a pair, and the remining sum as the second element.
But there are other cases we want to handle, if the second parameter isn't really a sum.

If it is Token then if the token matches the state expression return Pair(token, null) else
create a new sum containing the token and return Pair(null, newSum)

If the second parameter is a term then check to see if it matches the state expression.  If
it does, return its term (expression minus the state token) in a Pair(term, null). Otherwise
create a new sum from the state term and return Pair(null, newSum)

Otherwise search the sum for a matching state term.  If the match is found in the minusTerms
of the sum, then create a new sum with a single minusTerm containing the match and return
that.

 */
fun separateMatchingStateExpressionFromSum(expr: Expr, sum: Expr): Pair<Expr?, Sum?>{

    if ( ! isStateVariableExpr(expr)) {
        throw AlgebraException("separateMatchingStateExpressionFromSum(expr, sum) called with expr that is not a state expression,  expr = ${expr.toAnnotatedString()}")
    }

    println("separateMatchingStateExpressionFromSum(expr, sum)  expr = ${expr.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")


    val targetToken = getTokenFromStateExpression(expr)

    when (sum) {

        is Token -> {
            if (targetToken.equals(sum)) {
                return Pair(targetToken, null)
            } else {
                val newSum = Sum()
                newSum.plusTerms.add(sum)
                return Pair(null, newSum)
            }
        }

        is Number -> {} // won't happen since we know sum is a state variable expression, which can't be a lone number

        is Term -> {
            if (getTokenFromStateExpression(sum) == targetToken) {
                return Pair(getTermFromStateExpression(sum), null)
            } else {
                val newSum = Sum()
                newSum.plusTerms.add(sum)
                return Pair(null, newSum)
            }
        }

        is Sum -> {
            var matchingExpression: Expr? = null
            var alreadyFoundOne = false;
            var newSum:Sum = Sum()

            sum.plusTerms.forEach { loopExpr ->
                if (matchingStateExpressions(loopExpr, expr)) {
                    if (alreadyFoundOne) {
                        throw AlgebraException("separateMatchingStateExpressionFromSum(expr, sum) has more than one term with the same state variable. State variable = ${targetToken.toAnnotatedString()},  sum = ${sum.toAnnotatedString()}")
                    } else {
                        matchingExpression = loopExpr
                        alreadyFoundOne = true
                    }
                } else {
                    newSum.plusTerms.add(loopExpr)
                }
            }

            sum.minusTerms.forEach {loopExpr ->
                if (matchingStateExpressions(loopExpr, expr)) {
                    if (alreadyFoundOne) {
                        throw AlgebraException("separateMatchingStateExpressionFromSum(expr, sum) has more than one term with the same state variable. State variable = ${targetToken.toAnnotatedString()},  sum = ${sum.toAnnotatedString()}")
                    } else {
                        matchingExpression = createNegativeExpression(loopExpr)
                        alreadyFoundOne = true
                    }
                } else {
                    newSum?.minusTerms?.add(loopExpr)
                }
            }

            if (newSum.plusTerms.size + newSum.minusTerms.size == 0){
                return Pair(matchingExpression, null)
            }
            return Pair(matchingExpression, newSum)
        }
    }

    return Pair(null, null)  // should never get here.
}

fun addSubtractStateExpressionToFromSum(expr: Expr, sum: Sum, operation: Operation): Expr {

    if ( ! isStateVariableExpr(expr)) {
        throw AlgebraException("addSubtractStateExpressionToFromSum(expr, sum) called with expression that is not a state expression.  expr = ${expr.toAnnotatedString()}")
    }
    println ("addStateExpressionToSum(expr, sum)  expr = ${expr.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")

    var finalSum = Sum()
    var newTerm: Expr
    val expressionPair = separateMatchingStateExpressionFromSum(expr, sum)

    // Start building the final sum from the remaining sum in Pair.second if it's not null
    if (expressionPair.second != null){
        finalSum = expressionPair.second!!
    }

    // If there is no matching state expression in the original sum, then just add or subtract the
    // expression to the original sum and return the new sum.  Otherwise, add/subtract the expression
    // from its matching expression in the sum to create a new term.
    if (expressionPair.first == null) {
        if (operation == ADD) {
            finalSum.plusTerms.add(expr)
        } else {
            finalSum.minusTerms.add(expr)
        }
        return finalSum
    } else {
        newTerm = addSubtractStateExpressions(expressionPair.first as Expr, expr, operation)
    }

    // The new state expression may be of the form (negative sum)Token. We don't want to add this
    // back to the sum.  We want to subtract the positive form of this expression from the sum.
    // i.e. we want aT1 - bT2 not aT1 + (-b)T2
    val newTermTermPart = getTermFromStateExpression(newTerm)
    if (exprIsNegative(newTermTermPart)) {
        val pos =  convertNegativeToPositive(newTermTermPart)
        val se = createStateExpression(pos, getTokenFromStateExpression(newTerm))
        finalSum.minusTerms.add(se)
    } else {
        finalSum.plusTerms.add(newTerm)
    }

    return finalSum
}

fun addStateExpressionToSum(expr: Expr, sum: Sum): Expr {
    return addSubtractStateExpressionToFromSum(expr, sum, ADD)
}

fun addSumToStateExpression (expr: Expr, sum: Sum): Expr {
    return addStateExpressionToSum(expr, sum)
}

fun subtractSumFromStateExpression(expr: Expr, sum: Sum): Expr {

    val negatedSum = negate(sum)
    if (negatedSum !is Sum){
        return subtract(expr, negatedSum)
    }
    return addStateExpressionToSum(expr, negatedSum)
}

fun subtractStateExpressionFromSum(expr: Expr, sum: Sum): Expr {
    return addSubtractStateExpressionToFromSum(expr, sum, SUBTRACT)
}

/*
When we add or subtract a sum from a sum,  we must take avery expression in one sum,
and if it is a state expression, we need to see if there is matching state expression
the other sum, and if there is we want to create one term for the new sum that combines
these two expressions over a common denominator.  Our normal add and subtract functions
already do this, and return a new expression (usually a sum).  So we use the new
expression in succeeding calls to add/subtract to build a new sum.
 */

fun addSubtractStateExpressionListFromSum(list: ArrayList<Expr>, sum: Sum, operation: Operation): Expr {
    //var newSum = Sum()
    var newExpr: Expr

    newExpr = Sum()
    newExpr.plusTerms.addAll(sum.plusTerms)
    newExpr.minusTerms.addAll(sum.minusTerms)

    list.forEach {expr ->
        println("start newExpr = ${newExpr.toAnnotatedString()}, expr = ${expr.toAnnotatedString()}")
        if (expr != Number(0.0)) {
            if (operation == ADD) {
                newExpr = add(newExpr, expr)
            } else {
                newExpr = subtract(newExpr, expr)
            }
        }
        println("finish newExpr = ${newExpr.toAnnotatedString()}, expr = ${expr.toAnnotatedString()}")
    }

    return newExpr
}


/*
This function takes a sum and converts it to a term with a common denominator. It does this by
making repeated calls to addSubtract_cd() building up the new fraction one term at a time.
 */
fun convertSumToCommonDenominator(sum: Sum): Expr{
    var newExpr: Expr = Number(0.0)

    sum.plusTerms.forEach { expr ->

        newExpr = addSubtract_cd(newExpr, expr, ADD)
        println ("convertSumToCommonDenominator(sum) plusTerm expr ${expr.toAnnotatedString()}, newExpr = ${newExpr.toAnnotatedString()}")
    }
    sum.minusTerms.forEach { expr ->
        newExpr = addSubtract_cd(newExpr, expr, SUBTRACT)
        println ("convertSumToCommonDenominator(sum) minusTerm expr ${expr.toAnnotatedString()}, newExpr = ${newExpr.toAnnotatedString()}")
    }

    return newExpr
}

/*
Convert the sum to a new term over a common denominator and then multiply the new term by expr.
 */
fun multiplySumByStateExpression(expr: Expr, sum: Sum): Expr {

    println("multiplySumByStateExpression(expr, sum) expr = ${expr.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")
    if ( ! isStateVariableExpr(expr)){
        throw AlgebraException("multiplySumByStateExpression was called with non-state expression. expr = ${expr.toAnnotatedString()}")
    }

    val cdTerm = convertSumToCommonDenominator(sum)

    if (cdTerm is Sum){
        // if we got back a sum, it means there were no terms in the sum that were fractions, so we
        // just got back the original sum. If we call multiply(expr, cdTerm) in this case, we will
        // recurse infinitely. So we must handle  this case here.
        val term = Term()
        val stateTerm = getTermFromStateExpression(expr)

        // Start building a term from the term in the original state expression.
        println("multiplySumByStateExpression(expr, sum) stateTerm = ${stateTerm.toAnnotatedString()}")
        if (stateTerm is Term) {
            term.numerators.addAll(stateTerm.numerators)
            term.denominators.addAll(stateTerm.denominators)
        } else {
            if ( ! (stateTerm is Number && stateTerm.equals(Number(1.0)))) {
                term.numerators.add(stateTerm)
            }
        }
        var newExpr: Expr

        // If cdTerm, which is a sum, is all negative, then we want to build an entirely negative
        // term, not a term with a negative expression as part of the numerator.
        if (cdTerm.plusTerms.isEmpty()){
            val sum = Sum()
            sum.plusTerms.addAll(cdTerm.minusTerms)
            term.numerators.add(sum)
            newExpr = Sum()
            newExpr.minusTerms.add(term)

        } else {
            term.numerators.add(cdTerm)
            newExpr = term
        }
        println("multiplySumByStateExpression newExpr = ${newExpr.toAnnotatedString()} stateToken = ${getTokenFromStateExpression(expr).toAnnotatedString()}")
        val newStateTerm = createStateExpression(newExpr, getTokenFromStateExpression(expr))
        return newStateTerm
    }

    return multiply(expr, cdTerm)
}

/*
Multiply every term in the sum by the expression.
 */
fun multiplySumByExpression(expr: Expr, sum: Sum): Expr{
    println("multiplySumByExpression(expr, sum) expr = ${expr.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")
    if (expr is Sum) {
        throw AlgebraException("multiplySumByExpression called with an expression that is a sum.  expr = ${expr.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")
    }

    var newExpr: Expr = Number(0.0)

    sum.plusTerms.forEach { sumExpr ->
        newExpr = newExpr.add(multiply(expr, sumExpr))
    }
    sum.minusTerms.forEach { sumExpr ->
        newExpr = newExpr.subtract(multiply(expr,sumExpr))
    }

    if (newExpr is Sum) {
        return combineTerms(newExpr as Sum)
    }

    return newExpr
}

/*
Divide - common denominator.  Our normal divide function divides each term of a sum.
So x + y + z  divide by a becomes x/a + y/a + x/a. This function will return (x + y + z)/a.
If the first expression is not a Sum then we just return the result of the normal divide function.
 */
fun divide_cd(expr1: Expr, expr2: Expr): Expr {

    println("divide_cd(expr1, expr2) expr1 = ${expr1.toAnnotatedString()}, expr2 = ${expr2.toAnnotatedString()}")

    var newExpr: Expr
    var negative = false
    val term = Term()

    if (expr1 !is Sum){
        return divide(expr1, expr2)
    }

    if (expr1.plusTerms.isEmpty() && expr1.minusTerms.isEmpty()){
        return Number(0.0)
    }

    if (expr2 is Number && expr2.value == 1.0){
        return expr1.clone()
    }


    if (expr1.plusTerms.isEmpty()){
        newExpr = negate(expr1)
        negative = true
    } else {
        newExpr = expr1
    }

    if (expr2 !is Term){
        term.numerators.add(newExpr)
        term.denominators.add(expr2)
    }  else {
        term.numerators.add(newExpr)
        term.numerators.addAll(expr2.denominators)
        term.denominators.addAll(expr2.numerators)
    }

    println("divide_cd newExpr = ${newExpr.toAnnotatedString()},  term = ${term.toAnnotatedString()}, negative = $negative")

    if (negative){
        return createNegativeExpression(term)
    }

    println("divide_cd returning term = ${term.toAnnotatedString()}")
    return term

}

/*
get the term from the state expression and divide it by expr and then create a new state expression from the quotient.
 */
fun divideStateExpressionByExpression(stateExpression: Expr, expr: Expr): Expr {

    if ( ! isStateVariableExpr(stateExpression)) {
        throw AlgebraException("divideStateExpressionByExpression called with expression that is not a state expression.  stateExpression = ${stateExpression.toAnnotatedString()}")
    }

    if (isStateVariableExpr(expr)){
        throw AlgebraException("divideStateExpressionByExpression(expr, expr) called with two state variable expressions. stateExpression = ${stateExpression.toAnnotatedString()}, expr = ${expr.toAnnotatedString()}")
    }


    val newExpr = divide(getTermFromStateExpression(stateExpression), expr)

    val term = createStateExpression(newExpr, getTokenFromStateExpression(stateExpression))
    println("divideStateExpressionByExpression(stateExpr, expr) stateExpr = ${stateExpression.toAnnotatedString()}, expr = ${expr.toAnnotatedString()}, newExpr = ${newExpr.toAnnotatedString()}, new state expr = ${term.toAnnotatedString()}")
    return term
}
