package egor.timoshenko.graph

import egor.timoshenko.dto.ChildV
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import java.util.*

internal class RuntimeChildrenGraphTest {

    @Test
    fun `should delete child`() {
        val childrenGraph: ChildrenGraph = RuntimeChildrenGraph()
        val child = ChildV(UUID.randomUUID(), "name")
        childrenGraph.addChild(child)
        assertTrue(childrenGraph.deleteChild(child.id))
    }

    @Test
    fun `shouldn't add duplicate`() {
        val childrenGraph: ChildrenGraph = RuntimeChildrenGraph()
        val child = ChildV(UUID.randomUUID(), "name")
        childrenGraph.addChild(child)
        assertFalse(childrenGraph.addChild(child))
    }

    @Test
    fun `shouldn't add children with same name`() {
        val childrenGraph: ChildrenGraph = RuntimeChildrenGraph()
        val child1 = ChildV(UUID.randomUUID(), "name")
        val child2 = ChildV(UUID.randomUUID(), "name")
        childrenGraph.addChild(child1)
        assertFalse(childrenGraph.addChild(child2))
    }

    @Test
    fun `should return unloved children`() {
        val childrenGraph: ChildrenGraph = RuntimeChildrenGraph()
        val child1 = ChildV(UUID.randomUUID(), "name1")
        val child2 = ChildV(UUID.randomUUID(), "name2")
        childrenGraph.addChild(child1)
        childrenGraph.addChild(child2)
        val testList = listOf(child1, child2)
        val answer = childrenGraph.getUnlovedChildren()
        assertTrue(answer.containsAll(testList) && testList.containsAll(answer))
    }

    @Test
    fun `should return sad children`() {
        val childrenGraph: ChildrenGraph = RuntimeChildrenGraph()
        val child1 = ChildV(UUID.randomUUID(), "name1")
        val child2 = ChildV(UUID.randomUUID(), "name2")
        childrenGraph.addChild(child1)
        childrenGraph.addChild(child2)
        childrenGraph.addRelation(child1.id, child2.id)
        val testList = listOf(child1)
        val answer = childrenGraph.getUnlovedChildren()
        assertTrue(answer.containsAll(testList) && testList.containsAll(answer))
    }

    @Test
    fun `should return loved children`() {
        val childrenGraph: ChildrenGraph = RuntimeChildrenGraph()
        val child1 = ChildV(UUID.randomUUID(), "name1")
        val child2 = ChildV(UUID.randomUUID(), "name2")
        childrenGraph.addChild(child1)
        childrenGraph.addChild(child2)
        childrenGraph.addRelation(child1.id, child2.id)
        val testList = listOf(child2)
        val answer = childrenGraph.getLovedChildren()
        assertTrue(answer.containsAll(testList) && testList.containsAll(answer))
    }

    @Test
    fun `should return the most loved one`() {
        val childrenGraph: ChildrenGraph = RuntimeChildrenGraph()
        val child1 = ChildV(UUID.randomUUID(), "name1")
        val child2 = ChildV(UUID.randomUUID(), "name2")
        val child3 = ChildV(UUID.randomUUID(), "name3")
        val child4 = ChildV(UUID.randomUUID(), "name4")

        childrenGraph.addChild(child1)
        childrenGraph.addChild(child2)
        childrenGraph.addChild(child3)
        childrenGraph.addChild(child4)

        childrenGraph.addRelation(child1.id, child2.id)
        childrenGraph.addRelation(child4.id, child1.id)
        childrenGraph.addRelation(child1.id, child3.id)
        childrenGraph.addRelation(child2.id, child3.id)
        childrenGraph.addRelation(child3.id, child1.id)
        childrenGraph.addRelation(child3.id, child4.id)
        childrenGraph.addRelation(child1.id, child4.id)

        childrenGraph.deleteChild(child4.id)

        val testList = listOf(child3)
        val answer = childrenGraph.getLovedChildren()
        assertTrue(answer.containsAll(testList) && testList.containsAll(answer))
    }

    @Test
    fun `should return sad in complicated situation`() {
        val childrenGraph: ChildrenGraph = RuntimeChildrenGraph()
        val child1 = ChildV(UUID.randomUUID(), "name1")
        val child2 = ChildV(UUID.randomUUID(), "name2")
        val child3 = ChildV(UUID.randomUUID(), "name3")
        val child4 = ChildV(UUID.randomUUID(), "name4")

        childrenGraph.addChild(child1)
        childrenGraph.addChild(child2)
        childrenGraph.addChild(child3)
        childrenGraph.addChild(child4)

        childrenGraph.addRelation(child2.id, child1.id)
        childrenGraph.addRelation(child4.id, child1.id)
        childrenGraph.addRelation(child1.id, child3.id)
        childrenGraph.addRelation(child2.id, child3.id)
        childrenGraph.addRelation(child3.id, child4.id)
        childrenGraph.addRelation(child1.id, child4.id)

        childrenGraph.deleteChild(child4.id)

        val testList = listOf(child2, child1)
        val answer = childrenGraph.getSadChildren()
        assertTrue(answer.containsAll(testList) && testList.containsAll(answer))
    }


}