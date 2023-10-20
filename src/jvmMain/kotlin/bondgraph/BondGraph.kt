package bondgraph

import androidx.compose.ui.geometry.Offset
import bondgraph.ElementTypes.*
import kotlin.math.*

enum class ElementTypes {
    ZERO_JUNCTION{
         override fun displayString () = "0"
         },
    ONE_JUNCTION{
        override fun displayString() = "1"
        },
    CAPACITOR{
        override fun displayString() = "C"
        },
    RESISTOR{
        override fun displayString() = "R"
        },
    INERTIA{
        override fun displayString() = "I"
        },
    TRANSFORMER{
        override fun displayString() = "TF"
        },
    GYRATOR{
        override fun displayString() = "GY"
        },
    MODULATED_TRANSFORMER{
        override fun displayString()  = "MTF"
        },
    INVALID {
        override fun displayString() = ""
        };

    abstract fun displayString(): String

    companion object {
        fun toEnum(value: String): ElementTypes {
            return when (value) {
                "0" -> ZERO_JUNCTION
                "1" -> ONE_JUNCTION
                "C" -> CAPACITOR
                "R" -> RESISTOR
                "I" -> INERTIA
                "TF" -> TRANSFORMER
                "GY" -> GYRATOR
                "MTF" -> MODULATED_TRANSFORMER

                else -> INVALID
            }
        }
    }
}

class GraphElementDisplayData (val id: Int, var text: String, var x: Float, var y: Float, val width: Float, val height: Float, var centerLocation: Offset)

class Bond(val id: Int, val element1: Element?, var offset1: Offset, val element2: Element?, var offset2: Offset, var powerToElement: Element?){
    var displayId: String = id.toString()
    var casualToElement: Element? = null


}
class BondGraph(var name: String) {
    companion object {
        fun getArrowOffsets(startOffset: Offset, endOffset: Offset): Offset{
            val arrowAngle = .7f
            val arrowLength = 15f
            val xLength = endOffset.x - startOffset.x
            val yLength = endOffset.y - startOffset.y
            val angle = atan(yLength/xLength)
            val sign = if (xLength < 0) 1f else -1f
            return Offset((endOffset.x + sign*(arrowLength * cos(angle - sign * arrowAngle).toFloat())) , endOffset.y + sign*(arrowLength * sin(angle - sign * arrowAngle).toFloat()))
        }

        fun getCausalOffsets(startOffset: Offset, endOffset: Offset): Pair<Offset, Offset> {
            val arrowAngle = .7f
            val arrowLength = 15f
            val xLength = endOffset.x - startOffset.x
            val yLength = endOffset.y - startOffset.y
            val angle = atan(yLength/xLength)
            val sign = if (xLength < 0) 1f else -1f
            val off1 = Offset((endOffset.x + sign*(arrowLength * cos(angle + sign * 3.14/2f).toFloat())) , endOffset.y + sign*(arrowLength * sin(angle + sign * 3.14/2f).toFloat()))
            val off2= Offset((endOffset.x + sign*(arrowLength * cos(angle - sign * 3.14/2f).toFloat())) , endOffset.y + sign*(arrowLength * sin(angle - sign * 3.14/2f).toFloat()))
            return Pair(off1, off2)
        }


        fun offsetFromCenter(offset1: Offset, offset2: Offset, width: Float, height: Float):Offset {
            val l = (width + height)/2f + 5f
            val d = sqrt((offset1.x - offset2.x ).pow(2) + (offset1.y - offset2.y).pow(2))
            Offset(11f, 1f)
            return Offset((offset1.x - (l * (offset1.x - offset2.x)/d)), offset1.y - (l * (offset1.y - offset2.y)/d))
        }
    }
    //private val graphElementsDisplayDataMap = linkedMapOf<Int, GraphElementDisplayData>()
    val elementsMap = linkedMapOf<Int, Element>()
    val bondsMap = linkedMapOf<Int, Bond>()
    fun addElement(id: Int, elementType: ElementTypes, x: Float, y: Float, centerOffset: Offset): Unit {
        if (elementsMap.contains(id)){
            elementsMap[id]?.displayData?.text = elementType.displayString()
            elementsMap[id]?.displayData?.x = x
            elementsMap[id]?.displayData?.y = y
            elementsMap[id]?.displayData?.centerLocation = Offset(x + centerOffset.x, y + centerOffset.y)
        } else {
            val elementClass = when (elementType) {
                ZERO_JUNCTION -> ::ZeroJunction
                ONE_JUNCTION -> ::OneJunction
                CAPACITOR -> ::Capacitor
                RESISTOR -> ::Resistor
                INERTIA -> ::Inertia
                TRANSFORMER -> ::Transformer
                GYRATOR -> ::Gyrator
                MODULATED_TRANSFORMER -> ::ModulatedTransformer
                INVALID -> null
            }
            if (elementClass != null) {
                elementsMap[id] = elementClass.invoke(
                    this,
                    id,
                    elementType,
                    GraphElementDisplayData(
                        id,
                        elementType.displayString(),
                        x,
                        y,
                        centerOffset.x * 2f,
                        centerOffset.y * 2f,
                        Offset(x + centerOffset.x, y + centerOffset.y)
                    )
                )
            }
        }
    }

    fun getElementsMap():Map<Int, Element> = elementsMap

    fun getElement(id: Int): Element? {
        return elementsMap[id]
    }

    fun removeElement (id: Int) {
        elementsMap[id]?.getBondList()?.forEach{bondsMap.remove(it.id) }
        elementsMap.remove(id)
    }

    fun addBond(id: Int, elementId1: Int, offset1: Offset, elementId2: Int, offset2: Offset, powerToElementId: Int) {
        val bond = Bond(id, elementsMap[elementId1], offset1, elementsMap[elementId2], offset2, elementsMap[powerToElementId])
        bondsMap[id] = bond
        elementsMap[elementId1]?.addBond(bond)
        elementsMap[elementId2]?.addBond(bond)
    }

    fun getBond(id: Int): Bond? {
        return bondsMap[id]
    }

    fun removeBond(id: Int){
        elementsMap[bondsMap[id]?.element1?.id]?.removeBond(id)
        elementsMap[bondsMap[id]?.element2?.id]?.removeBond(id)
        bondsMap.remove(id)
    }

    fun elementRemoveBond(bond: Bond?){
        if (bond != null) {
            bondsMap.remove(bond.id)
        }
    }
    fun setPowerElement(id: Int, element: Element?){
        if (bondsMap[id] != null){
            if(bondsMap[id]?.element1 == element || bondsMap[id]?.element2 == element){
                bondsMap[id]?.powerToElement = element
            }
        }
    }
    fun setCasualElement(id: Int, element: Element?) {
        if (bondsMap[id] != null){
            if(bondsMap[id]?.element1 == element || bondsMap[id]?.element2 == element){
                bondsMap[id]?.casualToElement = element
            }
        }
    }

    fun updateBondsForElement(elementId: Int, newCenter: Offset)  {
        val width = elementsMap[elementId]?.displayData?.width
        val height = elementsMap[elementId]?.displayData?.height
        val bondsList = elementsMap[elementId]?.getBondList()
        if (width != null && height != null &&  ! bondsList.isNullOrEmpty()) {
            for (bond in bondsList){
                if (bond.element1?.id == elementId) {
                    val stableCenter = bond.element2?.displayData?.centerLocation
                    val stableWidth = bond.element2?.displayData?.width
                    val stableHeight = bond.element2?.displayData?.height
                    if (stableCenter != null && stableWidth != null && stableHeight != null) {
                        bond.offset2 = offsetFromCenter(stableCenter, newCenter, width, height)
                        bond.offset1 = offsetFromCenter(newCenter, stableCenter, stableWidth, stableHeight)
                    }
                } else {
                    val stableCenter = bond.element1?.displayData?.centerLocation
                    val stableWidth = bond.element1?.displayData?.width
                    val stableHeight = bond.element1?.displayData?.height
                    if (stableCenter != null && stableWidth != null && stableHeight != null) {
                        bond.offset1 = offsetFromCenter(stableCenter, newCenter, width, height)
                        bond.offset2 = offsetFromCenter(newCenter, stableCenter, stableWidth, stableHeight)
                    }
                }
            }
        }
    }
}