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

    if (exprIsNegative(workingExpr1)) {
        isNegative = true
        workingExpr1 = convertNegativeToPositive(workingExpr1)
    }

    if (exprIsNegative(workingExpr2)) {
        isNegative = ! isNegative
        workingExpr2 = convertNegativeToPositive(workingExpr2)
    }

    if (workingExpr1 !is Sum && workingExpr2 !is Sum){
        newExpr = multiply(workingExpr1, workingExpr2)
    } else {
        if (workingExpr1 is Sum) {
            newExpr = multiplyExpressionAndSum(workingExpr2, workingExpr1)
        } else {
            newExpr = multiplyExpressionAndSum(workingExpr1, workingExpr2 as Sum)
        }
    }

    if (isNegative) {
        newExpr = createNegativeExpression(newExpr)
    }

    if (isStateExpression){
        return createStateExpression(newExpr, stateToken)
    }

    return newExpr
}

/*fun multiply_f_old(expr1: Expr, expr2: Expr): Expr {

    var negative = false
    println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
    println("multiply_f(expr1, expr2)  expr1 = ${expr1.toAnnotatedString()}, expr2 = ${expr2.toAnnotatedString()}")

    fun multiplyExprToTerm(expr: Expr, term: Term): Term {
        var newTerm = Term()

       *//* var newExpr: Expr
        var stateToken:Expr = Token()
        var isStateTerm = false*//*

        newTerm.numerators.addAll(term.numerators)
        newTerm.denominators.addAll(term.denominators)

        *//*if (isStateVariableExpr(expr)) {
            newExpr = getTermFromStateExpression(expr)
            stateToken = getTokenFromStateExpression(expr)
            isStateTerm = true
        } else {
            newExpr = expr
        }*//*

        println("addExprToTerm(expr,term) expr = ${expr.toAnnotatedString()},  term = ${term.toAnnotatedString()}")
        when (expr) {
            is Token -> newTerm.numerators.add(expr)
            is Number -> newTerm.numerators.add(expr)
            is Term -> {
                newTerm.numerators.addAll(expr.numerators)
                newTerm.denominators.addAll(expr.denominators)
            }
            is Sum -> {
                if (expr.plusTerms.isEmpty()){
                    val sum = Sum()
                    sum.plusTerms.addAll(expr.minusTerms)
                    newTerm.numerators.add(sum)
                    negative = ! negative
                } else {
                    newTerm.numerators.add(expr)
                }
            }
        }
        *//*if (isStateTerm){
            val term = Term()
            term.add(newTerm)
            term.add(stateToken)
            return term
        }*//*
        if (newTerm.denominators.isEmpty() && newTerm.numerators.size == 1){
            return newTerm.numerators[0]
        }
        return newTerm
    }

    if (expr1.equals(Number(0.0)) || expr2.equals(Number(0.0))) {
        return Number(0.0)
    }

    if (expr1.equals(Number(1.0))){
        return expr2
    }

    if (expr2.equals(Number(1.0))) {
        return expr1
    }

    if (expr1 !is Sum && expr2 !is Sum) {
        return multiply(expr1, expr2)
    }

    if (expr1 !is Sum && expr2 is Sum && expr2.plusTerms.isEmpty() && expr2.minusTerms.size == 1){
        return multiply(expr1, expr2)
    }

    if (expr2 !is Sum && expr1 is Sum && expr1.plusTerms.isEmpty() && expr1.minusTerms.size == 1){
        return multiply(expr1, expr2)
    }

    if (expr1 is Sum && expr1.plusTerms.isEmpty() && expr1.minusTerms.size == 1 &&
        expr2 is Sum && expr2.plusTerms.isEmpty() && expr2.minusTerms.size == 1 ) {
        return multiply(expr1, expr2)
    }



    var term = Term()
    var firstExpr: Expr
    var secondExpr: Expr
    var stateToken: Expr = Token()
    var isStateTerm = false

    if (isStateVariableExpr(expr1) && isStateVariableExpr(expr2)){
        throw AlgebraException("multiply_f(expr, expr) attempt to multiply two state variable expressions. expr1 = ${expr1.toAnnotatedString()}, expr2 = ${expr2.toAnnotatedString()}")
    }

    firstExpr = expr1
    secondExpr = expr2

    if (isStateVariableExpr(expr1)){
        firstExpr = getTermFromStateExpression(expr1)
        stateToken = getTokenFromStateExpression(expr1)
        isStateTerm = true
    }

    if (isStateVariableExpr(expr2)){
        secondExpr = getTermFromStateExpression(expr2)
        stateToken = getTokenFromStateExpression(expr2)
        isStateTerm = true
    }
    println("multiply_f firstExpr = ${firstExpr.toAnnotatedString()}")
    term = multiplyExprToTerm(firstExpr, term)
    println("multiply_f term = ${term.toAnnotatedString()}, secondExpr = ${secondExpr.toAnnotatedString()}")
    term = multiplyExprToTerm(secondExpr, term)
    println("multiply_f term = ${term.toAnnotatedString()}")

    val newTerm = cancel(term)

    var newExpr: Expr
    if (negative){
        newExpr = Sum()
        newExpr.minusTerms.add(newTerm)
    } else {
        newExpr = newTerm
    }

    if (isStateTerm){
        val newTerm2 = Term()
        newTerm2.numerators.add(newExpr)
        newTerm2.numerators.add(stateToken)
        println("multiply_f return state term, newTerm = ${newTerm2.toAnnotatedString()}")
        return newTerm2
    }

    return newExpr

}*/


/*
Add expr1 and expr2 returning a single term over a common denominator if necessary.
Steps:
    - Break the expressions apart.  Create expressions that represent the numerator of each expression.
    - Create a list of terms in the denominators of each expression.  List may be empty.
    - Pass the lists to the commonDenominator function to create a common denominator, also a list.
    - For each expression, pass its denominator list along with the common denominator to the getNumeratorFactor function
      to generate a factor.
    - Multiply the numerator of each expression by its corresponding factor.
    - Add these two products together.
    - Create a term from the common denominator list.
    - Divide the sum by the common denominator term.
    Ex:
    ab/mn + cd/xym
    numerators ab and cd.  list1 {m,m} list2 {x,y,m}
    common denominator list {x,y,m,n}
    factor1 xy  factor2 n
    product1 (ab)(xy) = abxy  product2 cdn
    sum abxy + cdn
    result (abxy + cdn)/xymn
 */
fun addSubtract_cd(expr1: Expr, expr2: Expr, operation: Operation): Expr {
    println("addSubtract_cd called expr1 = ${expr1.toAnnotatedString()},  expr2 = ${expr2.toAnnotatedString()}, operation = $operation")
    val numerator1 = getNumerator(expr1)
    val numerator2 = getNumerator(expr2)

    /*if (numerator1.equals(Number(0.0))) {
        return expr2
    }*/
    if (numerator2.equals(Number(0.0))) {
        return expr1
    }

    val denominators1 = getDenominatorList(expr1)
    val denominators2 = getDenominatorList(expr2)
    val commonDenominatorList = commonDenominator(denominators1, denominators2)
    val factor1 = getNumeratorFactor(denominators1, commonDenominatorList)
    val factor2 = getNumeratorFactor(denominators2, commonDenominatorList)
    /*val product1 = multiply_f(numerator1, factor1)
    val product2 = multiply_f(numerator2, factor2)*/
    val product1 = multiply(numerator1, factor1)
    val product2 = multiply(numerator2, factor2)
    println("numerator1 = ${numerator1.toAnnotatedString()} factor1 = ${factor1.toAnnotatedString()} product1 = ${product1.toAnnotatedString()}")
    println("numerator2 = ${numerator2.toAnnotatedString()} factor2 = ${factor2.toAnnotatedString()} product2 = ${product2.toAnnotatedString()}")
    var sum: Expr
    if (operation == ADD) {
        sum = add(product1, product2)
    } else {
        sum = subtract(product1, product2)
    }
    println("addSubtract_cd  sum = ${sum.toAnnotatedString()}")

    val newExpr =  divide_cd(sum, generateTermFromList(commonDenominatorList))
    println("addSubtract_cd returning newExpr = ${newExpr.toAnnotatedString()}")
    return newExpr
}

/*
This function adds two state expression. A state expression has the general
form term x state variable  or sum x state variable so x P  Term(xy)P (a + b)P
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
   /* if (newTerm is Sum && newTerm.plusTerms.isEmpty() && newTerm.minusTerms.size == 1){
        val sum = Sum()
        term3.numerators.add(newTerm.minusTerms[0])
        term3.numerators.add(token1)
        sum.minusTerms.add(term3)
        return sum
    }
    term3.numerators.add(newTerm)
    term3.numerators.add(token1)*/
    val term3 = createStateExpression(newTerm, token1)
    return term3
}


/*
The next set of functions deals with adding subtracting a state expression to/from a Sum.
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
 */
class ExpressionAndList(var expr: Expr?, val list: List<Expr>)

/*
This function searches the list for any state expressions that match expr,
that is they have the same state token. If it finds one, it performs the operation
using the addSubtractStateExpressions() function. Normally the order is
(expr) operation (expr from list) but if reverse is true we reverse the order
(expr from list ) operation (expr)  Then we put the result in the expr field
of an ExpressionAndList object. Expressions that don't match are added to
the list of the ExpressionAndList object.
The idea here is that the input list will be either the plusTerms or the minusTerms of
a Sum.  The expr is going to be added to the sum, and we want the sum to contain
just one term for each state token. Calling code must decide whether the new expression
goes into the plusTerms or the minusTerms of the new Sum which is a convoluted and
explained below. This is why the new expression is set aside in a separate field.
So the result is either a new expression and a copy of the rest of the list or
a null expression and a copy of the entire list plus the original expression.
 */
fun addStateExprToList (expr: Expr, list: ArrayList<Expr>, operation: Operation, reverse: Boolean): ExpressionAndList {
    var newExpr: Expr? = null
    var saveExpr: Expr = Term()
    val newList = ArrayList<Expr>()
    var foundOne: Boolean = false

    if ( ! isStateVariableExpr(expr)) {
        throw AlgebraException("addStateExprToList called with expression that is not a state expression.  expr = ${expr.toAnnotatedString()}")
    }

    list.forEach { loopExpr ->
        if (matchingStateExpressions(expr, loopExpr)) {

            if (foundOne) {
                throw AlgebraException("the addStateExprToList function found more than one matching term in the list. " +
                "expr= ${expr.toAnnotatedString()}, first match = ${saveExpr.toAnnotatedString()}, second match = ${loopExpr.toAnnotatedString()} ")
            }

            saveExpr = loopExpr.clone()
            foundOne = true
            if (reverse) {
                println("addStateExprToList(expr, list, operation, boolean), calling addSubtractStateExpression loopExpr = ${loopExpr.toAnnotatedString()}, expr = ${expr.toAnnotatedString()}, operation = $operation, reverse = $reverse ")
                newExpr = addSubtractStateExpressions(loopExpr, expr, operation)
            } else {
                println("addStateExprToList(expr, list, operation, boolean), calling addSubtractStateExpression loopExpr = ${loopExpr.toAnnotatedString()}, expr = ${expr.toAnnotatedString()}, operation = $operation, reverse = $reverse ")

                newExpr = addSubtractStateExpressions(expr, loopExpr, operation)
            }
        } else {
            newList.add(loopExpr)
        }
    }

    return ExpressionAndList(newExpr, newList)

}

fun createSumFromExprAndLists(plusTermsData: ExpressionAndList, minusTermsData: ExpressionAndList, originalExpr: Expr, operation: Operation): Expr{
    val sum = Sum()
    sum.plusTerms.addAll(plusTermsData.list)
    sum.minusTerms.addAll(minusTermsData.list)
    println("createSumFromExprAndLists sum = ${sum.toAnnotatedString()}")
    if (plusTermsData.expr != null){
        println("createSumFromExprAndLists plusTerms expr  = ${plusTermsData.expr!!.toAnnotatedString()}")
        sum.plusTerms.add(plusTermsData.expr!!)

    } else {
        if (minusTermsData.expr != null) {
            if (operation == ADD) {
                sum.plusTerms.add(minusTermsData.expr!!)
            } else {
                sum.minusTerms.add(minusTermsData.expr!!)
            }
        }else {
            if (operation == ADD) {
                sum.plusTerms.add(originalExpr)
            } else {
                sum.minusTerms.add(originalExpr)
            }
        }
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.size == 0){
        return sum.plusTerms[0]
    }
    println("createsumFromExprAndLists sum = ${sum.toAnnotatedString()}")
    return sum
}

fun notStateExpressionMsg(expr: Expr, functionName: String): String {
    return "$functionName called with expression that is not a state expression. expr = ${expr.toAnnotatedString()}"
}

fun moreThanOneMsg(expr1: Expr, expr2: Expr, expr3: Expr, functionName: String ): String {
    return "$functionName found two expression in the sum that match expr.  \" +\n" +
            "        \"expr = ${expr1.toAnnotatedString()},  first new expression = ${expr2.toAnnotatedString()}, \" +\n" +
            "        \"second new expression = ${expr3.toAnnotatedString()}"
}

fun addStateExpressionToSum(expr: Expr, sum: Sum): Expr {

    println("addStateExpressionToSum  expr = ${expr.toAnnotatedString()},  sum = ${sum.toAnnotatedString()}")
    if ( ! isStateVariableExpr(expr)) {
        throw AlgebraException(notStateExpressionMsg(expr, "addStateExpressionToSum"))
    }
    println("addStateExpressionFromSum(expr, sum) expr = ${expr.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")

    val plusTermsExprAndList = addStateExprToList(expr, sum.plusTerms, ADD, false)
    if (plusTermsExprAndList.expr != null)println("addStateExpressionToSum  expr = ${plusTermsExprAndList.expr!!.toAnnotatedString()}")
    val minusTermsExprAndList = addStateExprToList(expr, sum.minusTerms, Operation.SUBTRACT, false)
    //println("addStateExpressionToSum  sum = ${sum.toAnnotatedString()}")

    if (plusTermsExprAndList.expr != null && minusTermsExprAndList.expr != null) {
        throw AlgebraException(moreThanOneMsg(expr, plusTermsExprAndList.expr!!, minusTermsExprAndList.expr!!,"addStateExpressionToSum"))
    }

    return createSumFromExprAndLists(plusTermsExprAndList, minusTermsExprAndList, expr,  ADD)
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

    if ( ! isStateVariableExpr(expr)) {
        throw AlgebraException(notStateExpressionMsg(expr, "subtractStateExpressionFromSum"))
    }

    println("subtractStateExpressionFromSum(expr, sum) expr = ${expr.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")
    val plusTermsExprAndList = addStateExprToList(expr, sum.plusTerms, SUBTRACT, true)
    val minusTermsExprAndList = addStateExprToList(expr, sum.minusTerms, ADD, false)

    if (plusTermsExprAndList.expr != null && minusTermsExprAndList.expr != null) {
        throw AlgebraException(moreThanOneMsg(expr, plusTermsExprAndList.expr!!, minusTermsExprAndList.expr!!,"subtractStateExpressionToSum"))
    }

    return createSumFromExprAndLists(plusTermsExprAndList, minusTermsExprAndList, expr, Operation.SUBTRACT)
}

/*
fun subtractSumFromStateExpression(expr: Expr, sum: Sum): Expr {

    if ( ! isStateVariableExpr(expr)) {
        throw AlgebraException(notStateExpressionMsg(expr, "subtractSumFromStateExpression"))
    }

    val plusTermsExprAndList = addStateExprToList(expr, sum.plusTerms, SUBTRACT, false)
    val minusTermsExprAndList = addStateExprToList(expr, sum.minusTerms, ADD, false)

    if (plusTermsExprAndList.expr != null && minusTermsExprAndList.expr != null) {
        throw AlgebraException(moreThanOneMsg(expr, plusTermsExprAndList.expr!!, minusTermsExprAndList.expr!!,"subtractSumFromStateExpression"))
    }

    return createSumFromExprAndLists(plusTermsExprAndList, minusTermsExprAndList, Operation.ADD)
}*/

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

   /* fun doOp(exp: Expr){
        if (operation == ADD){
            newSum.plusTerms.add(exp)
        } else {
            newSum.minusTerms.add(exp)
        }
    }*/

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
This function takes a sum and converts it to a term with a common denominator
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

fun multiplySumByStateExpression(expr: Expr, sum: Sum): Expr {

    println("multiplySumByStateExpression(expr, sum) expr = ${expr.toAnnotatedString()}, sum = ${sum.toAnnotatedString()}")
    if ( ! isStateVariableExpr(expr)){
        throw AlgebraException("multiplySumByStateExpression was called with non-state expression. expr = ${expr.toAnnotatedString()}")
    }

    val cdTerm = convertSumToCommonDenominator(sum)

    if (cdTerm is Sum){
        val term = Term()
        val stateTerm = getTermFromStateExpression(expr)
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

fun multiplySumByExpression(expr: Expr, sum: Sum): Expr{

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

    if (expr2 is Number && expr2.value == 1.0){
        return expr1.clone()
    }

    if (expr1.plusTerms.isEmpty() && expr1.minusTerms.size == 1){
        newExpr = expr1.minusTerms[0]
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

    if (negative){
        return createNegativeExpression(term)
    }

    println("divide_cd returning term = ${term.toAnnotatedString()}")
    return term

}

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
