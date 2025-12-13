package algebra

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.unit.sp
import java.lang.IllegalStateException
import java.text.NumberFormat
import algebra.operations.*
import bondgraph.Operation.*

/*
    This program can perform a limited amount of symbolic algebra, enough to generate and solve basic
    equations produced from bond graphs.  This capability is made up of 5 classes, Equation, Token, Number, Term and Sum,
    the expression (Expr) interface and a bunch of functions. Briefly:

    Equation: is trivial class containing two expressions, one for the left side of the equation and one for the right side.

    Token: the basic building block of an expression. They represent the entities used in bond graph equations, such
           as momentum, capacitance, resistance etc. For example in the expression q1(R1 + R2) q1, R1 and R2 would
           all be represented by tokens. Tokens are created by the element classes.  For example a capacitor object
           would create tokens for its capacitance, the displacement on its bond and the derivative of the displacement.
           An element  object creates its tokens once and uses the same ones every time it is called on to supply them
           for equation generation. So tokens can be compared on the object level i.e. t1 === t2.

    Term:  A term keeps lists of expressions that are multiplied or divided into each other.  Example C1R2/R3(R4 + R5)

    Sum:   A sum keeps lists of expressions that are added or subtracted from each other. Example (C2 + I3R2 + I4/R3R3)

    From the examples we see that terms can contain sums, and sums may contain terms.

    The Token, Number, Term and Sum classes implement the Expr interface.  They each provide functions for how to add, subtract,
    multiply and divide itself by another expression.  Originally, I thought each Expr class would implement these functions
    internally but this didn't work out. It turned out that functions in different classes are too inter-related.  There
    were too many conditions to check, and problems with infinite recursive calls.  Also, there are decisions that
    need to be made at a higher level, such as should x times (b + c) be x(b + c) or xb + xc.  Sometimes you want one form,
    sometime the other.  So I finally decided to write a separate function for every combination of operation, i.e.
    Token + Token, Token + Number, Token - Term, Term - Number etc. Now these classes call these functions to implement
    their operation functions.

    Expr's must also implement equals(Expr).  This is because we want different objects to possibly be equal.
    Example  (a + b) = (b + a)  or ab = ba.  We need this for performing cancel operations on fractions, and for
    solving equations.

    The Expr interface also calls for a clone function for making copies, and a toAnnotatedString function that is
    used to create a string for the class, so we can print out expressions.
 */

enum class Sign {
    POSITIVE {
        override fun toString() = "positive"
    },
    NEGATIVE {
        override fun toString() = "negative"
    }
}

// Function for comparing two lists. Lists are assumed to be the same size. Return true if the lists are equal.
// The order doesn't matter.
// For each element in the first list see if it exists in the second list. Remove elements in the second list
// as they are found in case an element occurs twice in the first list but only once in the second list.
// This function is used by the equals() function in several classes, and since is also uses the equals function,
// it will indirectly call itself recursively.

fun compareLists(list1: ArrayList<Expr>, list2: ArrayList<Expr>): Boolean {
    val copyOfList2 = arrayListOf<Expr>()
    val loopCopy = arrayListOf<Expr>()
    var foundOne: Boolean

    copyOfList2.addAll(list2)

    for (e1 in list1){
        foundOne = false
        loopCopy.clear()
        loopCopy.addAll(copyOfList2)
        for (e2 in loopCopy) {
            if (e1.equals(e2)) {
                foundOne = true
                copyOfList2.remove(e2)
                break
            }
        }
        if (! foundOne) {
            return false
        }
    }
    return true
}



interface Expr{

    fun add(expr: Expr): Expr

    fun subtract(expr: Expr): Expr

    fun multiply(expr: Expr): Expr

    fun divide(expr: Expr): Expr

    fun toAnnotatedString(exp: Int = 0): AnnotatedString


    override fun equals(o: Any?): Boolean

    fun clone(): Expr
}

class Number(val value: Double = 0.0): Expr {

    fun buildTerm(numerators: ArrayList<Expr>, denominators: ArrayList<Expr>, num: Double): Expr {
        val newNumerators = arrayListOf<Expr>()
        val newDenominators = arrayListOf<Expr>()
        var newNum = num

        numerators.forEach {
            if (it is Number){
                newNum *= it.value
            } else {
                newNumerators.add(it)
            }
        }

        denominators.forEach {
            if (it is Number){
                newNum /= it.value
            } else {
                newDenominators.add(it)
            }
        }

        if (newNumerators.size + newDenominators.size == 0){
            return Number(newNum)
        }

        //newNumerators.add(Number(newNum))
        if (newNum == 1.0) {
            if (newNumerators.size == 0){
                newNumerators.add(Number(1.0))
            }
        } else {
            newNumerators.add(Number(newNum))
        }
        val term = Term()
        term.numerators.addAll(newNumerators)
        term.denominators.addAll(newDenominators)
        return term
    }
    override fun add(expr: Expr): Expr {
       return add(this, expr)
    }

    override fun subtract(expr: Expr): Expr {
        return subtract(this, expr)
    }

    override fun multiply(expr: Expr): Expr {
        return multiply(this, expr)
    }


    override fun divide(expr: Expr): Expr {
        return divide(this, expr)
    }

    override fun toAnnotatedString(exp: Int): AnnotatedString {
        val formatter = NumberFormat.getInstance()
        formatter.maximumFractionDigits = 6
        return AnnotatedString(formatter.format(value))
    }

    override fun equals(o: Any?): Boolean {
        /*
        equals function must
         */

        if (this === o){
            return true;
        }

        if (o !is Number){
            return false
        }

       /* val expr = o as Number
        if (expr is Number) {
            return expr.value == value
        }*/

        return o.value == value
    }

    override fun clone(): Expr {
        return Number(value)
    }
}

/*
    A token represents a bond graph element that would appear in an equation, such as momentum, resistance,
    capacitance etc.  A token is associated with at least one bond. The modulus on a transformer or
    gyrator is associated with two bonds. The class contains various flags to describe the nature of
    the token that are needed for generating and displaying equations.
 */
class Token(
    val bondId1: String = ""
    ,val bondId2: String = ""
    ,val name: AnnotatedString = AnnotatedString("")
    ,val powerVar: Boolean = false  // source of effort and source of flow tokens
    ,val energyVar: Boolean = false // Displacement on a capacitor or momentum on an inertia
    ,val independent: Boolean = false
    ,val differential: Boolean = false
    ): Expr {



    override fun add(expr: Expr): Expr {
        return add(this, expr)
    }

    override fun subtract(expr: Expr): Expr {
        return subtract(this, expr)
    }

    override fun multiply(expr: Expr): Expr {
       return multiply(this, expr)
    }

    override fun divide(expr: Expr): Expr {
        return divide(this, expr)
    }

    // An element object create only copy of each of its tokens so they can be compared at the object level.
    override fun equals(o: Any?): Boolean {

        return this === o
    }

    override fun toAnnotatedString(exp: Int): AnnotatedString {

        val normalStyle = SpanStyle(fontSize = 20.sp)

        val superScript = SpanStyle(
            baselineShift = BaselineShift.Superscript,
            fontSize = 15.sp,
        )

        val subscript = SpanStyle(
            baselineShift = BaselineShift.Subscript,
            fontSize = 12.sp,
        )

        return buildAnnotatedString {
            pushStyle(normalStyle)
            append(name)
            if (differential) {
                append("\u0307") // put a dot over the previous character.
            }

            pushStyle(subscript)
            append(bondId1)
            if ( ! bondId2.equals("")) {
                append(",")
                append(bondId2)
            }
            pop()
            if (powerVar) {
                append ("(t)")
            }
            if (exp > 0) {
                pushStyle(superScript)
                append(exp.toString())
            }
        }
    }

    override fun clone(): Expr {
        return this  // do not copy tokens. They need to be unique.
    }
}

/*
    A term is made up of numerator and a denominator.  The numerator and denominator are both made up of
    a list of expressions that are multiply together. So the numerator list R1, C1, and the denominator list
    I4, R2, R3 represents R1C1/I4R2R3
 */
class Term():Expr {

    val numerators = arrayListOf<Expr>()
    val denominators = arrayListOf<Expr>()


    override fun toAnnotatedString(exp: Int): AnnotatedString {
        return buildAnnotatedString {
            if (numerators.size == 0) {
                append ("1")
            } else {
                numerators.forEach {
                    when (it) {
                        is Token -> append(it.toAnnotatedString())
                        is Number -> append(it.toAnnotatedString())
                        is Term -> {
                            append("(")
                            append(it.toAnnotatedString())
                            append(")")
                        }
                        is Sum -> {
                            append("(")
                            append(it.toAnnotatedString())
                            append(")")
                        }
                    }
                }
            }
             if (denominators.size > 0){
                 append("/")
                 denominators.forEach {
                     when (it) {
                         is Token -> append(it.toAnnotatedString())
                         is Number -> append(it.toAnnotatedString()) // shouldn't be any numbers in denominators
                         is Term -> append(it.toAnnotatedString())
                         is Sum -> {
                             append("(")
                             append(it.toAnnotatedString())
                             append(")")
                        }
                    }
                 }
             }
        }
    }

    override fun add(expr: Expr): Expr {
        return add(this, expr)
    }

    override fun subtract(expr: Expr): Expr {
        return subtract(this, expr)
    }



    override fun multiply(expr: Expr): Expr {
        println("Term.multiply this = ${this.toAnnotatedString()}, expr = ${expr.toAnnotatedString()}")
        return multiply(this, expr)
    }

    override fun divide(expr: Expr): Expr {
        return divide (this, expr)
    }

    /*
     Check to see of this object is equal to expr.  This is a basic test.  We compare the expressions in the
     then numerator and denominator of both terms to see if they are the same. They don't have to be in the order.
     However, we don't expand any expressions.  If one term contains the two tokens 'ax' and the other term contains
     a term 'term(ax)' this function won't find it.
     */
    override fun equals(o: Any?): Boolean {

        val exprNumerators = arrayListOf<Expr>()
        val exprDenominators = arrayListOf<Expr>()


        if (this === o){
            // Try this first since it is quick
            return true
        }

        if (o !is Term) {
            // can't be = to this if it isn't the same type of object.
            return false
        }


        exprNumerators.addAll(o.numerators)
        exprDenominators.addAll(o.denominators)

        // If both expressions don't contain the same number of elements they can't be equal.
        if (exprNumerators.size != numerators.size || exprDenominators.size != denominators.size) {
            return false
        }

        // Done with quick easy checks.  Compare expressions term by term.
        if (compareLists(numerators, exprNumerators)) {
            if (compareLists(denominators, exprDenominators)) {
                return true
            }
        }

        return false
    }

    override fun clone(): Expr {

        val term = Term()
        term.numerators.addAll(numerators)
        term.denominators.addAll(denominators)
        return term
    }

    fun getNumeratorTokens(): List<Token> {
        return numerators.filter { it is Token }.map{ it as Token}
    }
}
/*
    The Sum class is used for adding and subtracting expressions.  A Sum maintains two lists, one of
    expressions that should be added to the Sum and one of expressions that should be subtracted
    from the Sum.
 */
class Sum(): Expr {

    val plusTerms = arrayListOf<Expr>()
    val minusTerms = arrayListOf<Expr>()


    override fun toAnnotatedString(exp: Int): AnnotatedString {
        //println("sum.toAnnotatedString called")
        val string = buildAnnotatedString {
            var cnt = 0
            //println("plusTerms size = ${plusTerms.size}")
            plusTerms.forEach {
                if (cnt > 0) {
                    append (" + ")
                }
                //println(" it ${it::class.simpleName}, cnt = $cnt")
                cnt++

                append (it.toAnnotatedString())
            }

            minusTerms.forEach{
                //println ("Sum.toAnnotatedString minusTerm ${it}: ${it::class.simpleName}")
                append (" - ")
                //println("append minus sign")
                if (it is Sum) append("(")
                append (it.toAnnotatedString())
                if (it is Sum) append(")")
            }
        }
        //println("string = $string")
        return string
    }

    override fun add(expr: Expr): Expr {
        return add (this, expr)
    }


    override fun subtract(expr: Expr): Expr {
        return subtract(this, expr)
    }

    override fun multiply(expr: Expr): Expr {
        return multiply(this, expr)
    }


    override fun divide(expr: Expr): Expr {
        return divide(this, expr)
    }

    /*
    Check to see of this object is equal to expr.  This is a basic test.  We compare the expressions in the
    plusTerms and minusTerms of both sums to see if they are the same. They don't have to be in the order.
    However, we don't expand any expressions.  If one sum contains the two  ( a = b + c) and the other term contains
    a term (a + sum(b + c) this function won't find it.
    */
    override fun equals(o: Any?): Boolean {

        //println("Sum.equals called")

        if (this === o) return true

        if (o !is Sum) return false

        //val expr = o as Sum
        //printExpr(this)
        //printExpr(o)
        val exprPlusTerms = arrayListOf<Expr>()
        val exprMinusTerms = arrayListOf<Expr>()
        val copy = arrayListOf<Expr>()
        var foundOne = false

        exprPlusTerms.addAll(o.plusTerms)
        exprMinusTerms.addAll(o.minusTerms)

        // If this and expression don't have the same number of terms, they can't be equal.
        if (plusTerms.size != exprPlusTerms.size || minusTerms.size != exprMinusTerms.size) {
            return false
        }

        // Done with easy check, have to compare term by term.
        if (compareLists(plusTerms, exprPlusTerms)) {
            if (compareLists(minusTerms, exprMinusTerms)) {
                return true
            }
        }

        return false
    }

    override fun clone(): Expr {
        val sum = Sum()
        sum.plusTerms.addAll(plusTerms)
        sum.minusTerms.addAll(minusTerms)
        return sum
    }
}

/*
Simple data class.  Holds one expression for the left side of the equation and another for the right side.
 */
class Equation(var leftSide: Expr, var rightSide: Expr) {

    companion object {
        fun empty(): Equation {
            return Equation(Term(), Term())
        }
    }

    fun toAnnotatedString (): AnnotatedString {
       return buildAnnotatedString {
            append (leftSide.toAnnotatedString())
            append(" = ")
            append (rightSide.toAnnotatedString())
        }
    }

    fun clone(): Equation {
        return Equation(leftSide.clone(), rightSide.clone())
    }

}

/*
The Matrix is a class used for creating, and storing matrices along with functions for using matrices
to do linear algebra to solve sets of simultaneous equations.
 */
class Matrix private constructor(val data: ArrayList<ArrayList<Expr>>) {

    companion object {
        /*
        The solveCramer function uses a method called Cramer's Rule to solve a set of simultaneous equations.
        It takes three inputs, a matrix of coefficients of the variables to be solved for, a list of the variables
        to be solved for (a one column matrix) and a list of constants from each equation.  The format of these
        matrices is discussed in more detail in the comments for the solve() function in the AlgebraFunctions.kt file.
        It returns a list of solved equations.
        Cramer's Rule basic steps:
            - Calculate the determinant of the coefficient matrix.
            - For each equation create a new matrix that consists of the coefficient matrix with the column
              associated with that equation replaced with the constant column.
            - Calculate the determinant of the new matrix.
            - divide the new determinant by the determinant of the coefficient matrix. This yields the solution
              for that equation.
         */
        fun solveCramer(coefficientMatrix: Matrix, variables: ArrayList<Token>, const: ArrayList<Expr>): ArrayList<Equation> {

            val equations = arrayListOf<Equation>()

            // Calculate the determinant of the coefficient matrix.
            val coefficientMatrixDet = coefficientMatrix.det(true)
            println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
            coefficientMatrix.printOut()
            println("coeffDet = ${coefficientMatrixDet.toAnnotatedString()}")
            val size = coefficientMatrix.data.size  // Also the number of equations

            // for each equation
            for (matrixIndex in 0 until size){

                // build a matrix coping the coefficient matrix but repacing the column for this
                // equation with the constant column.
                val builder = Builder(size)
                for (row in 0 until size){
                    for (col in 0 until size){
                        if (matrixIndex == col){
                            builder.add(const[row])
                        } else {
                            builder.add(coefficientMatrix.data[row][col])
                        }
                    }
                }
                val constMatrix = builder.build()

                val constMatrixDet = constMatrix.det(false)
                println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%5")
                coefficientMatrix.printOut()
                constMatrix.printOut()
                println("constMatrix.det = ${constMatrixDet.toAnnotatedString()} ${constMatrix::class.simpleName}")
                println("coeffDet = ${coefficientMatrixDet.toAnnotatedString()}: ${coefficientMatrixDet::class.simpleName}")

                if (coefficientMatrixDet is Sum) {
                    //println("common denominator = ${convertSumToCommonDenominator((coeffDet as Sum)).toAnnotatedString()}")
                }
                // Generate the solved equation by dividing the two determinants. Store the result in the equations list.
                val equation = Equation(variables[matrixIndex], constMatrixDet.divide(coefficientMatrixDet))
                println("solverCramer equation = ${equation.toAnnotatedString()}")
                equations.add(equation)
            }
            return equations
        }

        /*
        return a matrix of the specified order, with elements initialized to the Number(0.0)
         */
        fun zeroMatrix(order: Int): Matrix {
            val builder = Matrix.Builder(order)
            repeat (order * order) {
                builder.add(Number(0.0))
            }
            return builder.build()
        }

    }

    /*
    A builder class for building a matrix. Each call to add adds the next expression to the matrix building by row.
    A matrix of order 3 would require 9 calls to add() to build 3 rows, each with 3 elements.
     */
    class Builder (val order: Int){
        val data = arrayListOf<ArrayList<Expr>>()
        var cnt = 0

        fun add (expr: Expr): Builder{
            if (cnt >= order * order) throw IllegalArgumentException("This call to Matrix.Builder.add exceeds the amount of data this matrix (order $order) can hold.")
            if (cnt.div(order) + 1 > data.size ) {
                data.add(ArrayList())
            }
            data[cnt.div(order)].add(expr)
            cnt++
            return this
        }

        fun build(): Matrix {
            if (cnt != (order * order) ) throw IllegalStateException("Not enough data to build this matrix of order $order. Number of data points = $cnt")
            return Matrix (data)
        }
    }

    /*
    Build a cofactor of the matrix around the specified row and column. A cofactor matrix is a matrix formed
    by removing the specified row and column from the starting matrix. Cofactors can be used to help calculate
    determinants. Given

    a1 b1 c1
    a2 b2 c2
    a3 b3 c3

    Cofactor row 2, column 2 would be

    a1 c1
    a3 c3

    Of course the code below is zero relative.
     */
    fun cofactor(row: Int, column: Int): Matrix {
        val builder = Builder(data.size - 1)
        for (i in 0 until data.size){
            if (i != row){
                for (j in 0 until data.size){
                    if (j != column){
                        builder.add(data[i][j])
                    }
                }
            }
        }
        return builder.build()
    }

    /*
    Calculate the determinant of the matrix.  A determinant is a calculation that reduces a matrix to
    a single expression (single number in the case of numbers). Determinants can be used solve systems
    of simultaneous equations.  This function uses a procedure called cofactor expansion.
    Brief explanation of steps:
        - The determinant of a 1 x 1 matrix is just the one element stored in row 1, col 1.  This is
          redundant, but by including this case, we can use our solveCramer() function to solve a
          single equation in addition to a set of equations.
        - The determinant of a 2 x 2 matrix is (1, 1)(2, 2) - (1, 2)(2, 1) so given
          a b
          c d
          the determinant would be ad - bc
        - For 3 x 3 or higher order matrices we use cofactor expansion. In this method you pick a row in the
          matrix.  If you are doing this by hand using numbers, you would look for a row containing ones and
          zeros and other advantages things. In our case we just use the first row (row 0).  Then for each
          element in the row, multiply the element by the determinant of the cofactor for this element. These
          products are then alternately added and subtracted together to calculate the final answer.
    In the code below, we calculate the determinants for 1 x 1 and 2 x 2 matrices directly.  For higher
    order matrices we use cofactor expansion using row zero.  We start with an empty Sum.  Then we move down
    row 0 and calculate the product of each element and its cofactor determinant, and add/subtract the products
    to the Sum as we go along.
    For 4 x 4 or higher matrices the cofactors will still be higher order matrices, so this function will be
    called recursively until we get down to 2 x 2 cofactors.
    This function also takes a boolean parameter that says whether to use factored or non-factored multiplication
    in the calculations.  Given 'a times (x + Y)'  factored multiplication gives a(x + y) non-factored gives ax + ay.
    Calculations using the determinants can be made easier depending on how the products are calculated.
     */
    fun det(factored: Boolean): Expr {
        if (data.size == 1) {
            return data[0][0]
        } else {
            if (data.size == 2) {
                val product1 = if (factored) multiply_f(data[0][0],data[1][1]) else multiply(data[0][0],data[1][1])
                println("det() data1 = ${data[0][0].toAnnotatedString()}: ${data[0][0]::class.simpleName} data2 = ${data[1][1].toAnnotatedString()}: ${data[1][1]::class.simpleName}, product1 = ${product1.toAnnotatedString()}, factored = $factored")
                val product2 = if (factored) multiply_f(data[1][0],data[0][1]) else multiply (data[1][0],data[0][1])
                val difference = product1.subtract(product2)
                return difference
            } else {
                var det: Expr = Sum()
                var addIt = true
                for (column in 0 until data.size) {
                    println("det column = $column")
                    if (addIt) {
                        val cofactorDet = cofactor(0, column).det(factored)
                        println("|||||||||||||||  det  cofactor matrix is  |||||||||||||||||||||||||||||||")
                        cofactor(0,column).printOut()
                        val product = if (factored) multiply_f (data[0][column],(cofactorDet)) else multiply (data[0][column],(cofactorDet))
                        println("det = ${det.toAnnotatedString()}  element = ${data[0][column].toAnnotatedString()},  cofactorDet = ${cofactorDet.toAnnotatedString()}  add product = ${product.toAnnotatedString()},  factored = $factored")
                        det = det.add(product)
                        println("new det = ${det.toAnnotatedString()}")
                    } else {
                        val cofactorDet = cofactor(0, column).det(factored)
                        cofactor(0,column).printOut()
                        val product = if (factored) multiply_f (data[0][column], cofactorDet) else multiply (data[0][column], cofactorDet)
                        println("det = ${det.toAnnotatedString()}  element = ${data[0][column].toAnnotatedString()},  cofactorDet = ${cofactorDet.toAnnotatedString()}  subtract product = ${product.toAnnotatedString()}")
                        det = det.subtract(product)
                        println("new det = ${det.toAnnotatedString()}")
                    }
                    addIt = !addIt
                }

                println("det() returning ${det.toAnnotatedString()}: ${det::class.simpleName}")
                return det
            }
        }
    }


    /*
    The next two functions add or subtract the expression to/from whatever value is already in the
    specified row and column.  In the case of fractions the sum is placed over a common denominator.
    These functions are used to help group like terms from equations into their appropriate elements.
     */
    fun incrementElementByExpr(expr: Expr, row: Int, col: Int) {
        data[row][col] = addSubtract_cd(data[row][col], expr, ADD)
    }

    fun decrementElementByExpr(expr: Expr, row: Int, col: Int) {
        data[row][col] = addSubtract_cd(data[row][col], expr, SUBTRACT)
    }

   /*
   Utility function to print out the matrix in a semi-readable form
    */
    fun printOut(){
        println("-------------------------------------------------------------------------------------")
        for (i in 0 until data.size){
            for (j in 0 until data.size){
                if (j != 0) print(", ")
                print("${data[i][j].toAnnotatedString()}: ${data[i][j]::class.simpleName}")
            }
            println()
        }
        println("-------------------------------------------------------------------------------------")
    }

}
