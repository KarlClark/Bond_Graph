package algebra.operations

import algebra.*

/*fun reduce (sum: Sum): Expr {

    if (sum.plusTerms.size + sum.minusTerms.size == 0){
        return Number(0.0)
    }

    if (sum.plusTerms.size == 1 && sum.minusTerms.size == 0){
        return sum.plusTerms[0]
    }
}*/

fun reduce (term: Term): Expr{
    /*
    If there are no values in the numerator or denominator (maybe everything canceled) then return Number(1.0)
    If there is one expression  in the numerator and no denominators, then return the expression.  So for example
    a Term with just a Sum in the numerator would become just the Sum.
     */

    if (term.numerators.size + term.denominators.size == 0){
        return Number(1.0)
    }

    if (term.numerators.size == 1 && term.denominators.size == 0){
        return term.numerators[0]
    }

    return term
}